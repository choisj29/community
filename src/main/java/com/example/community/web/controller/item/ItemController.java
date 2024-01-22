package com.example.community.web.controller.item;

import com.example.community.SessionConst;
import com.example.community.domain.common.PageMaker;
import com.example.community.domain.item.Item;
import com.example.community.domain.keyword.Keyword;
import com.example.community.domain.member.Member;
import com.example.community.domain.rankLog.RankLog;
import com.example.community.service.item.ItemService;
import com.example.community.service.keyword.KeywordService;
import com.example.community.service.member.MemberService;
import com.example.community.service.rankLog.RankLogService;
import com.example.community.web.dto.item.*;
import com.example.community.web.dto.rankLog.RenewForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.time.LocalDateTime.now;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final KeywordService keywordService;
    private final MemberService memberService;
    private final RankLogService rankLogService;

    @GetMapping("/item/list")
    public String itemList(Model model, ItemSearchReq searchReq,
                           @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member,
                           @PageableDefault(size=5) Pageable pageable) {
        int pageNum = pageable.getPageNumber()+1;
        int pageSize = pageable.getPageSize();
        searchReq.setMemberId(member.getId());
        Page<ListItemDto> paging = itemService.search(searchReq, pageable);
        log.info("paging {}",paging);
        PageMaker pageMaker = new PageMaker(pageNum, paging.getTotalElements(), pageSize, 10);

        model.addAttribute("paging", paging);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("searchReq", searchReq);
        return "item/list";
    }

    @GetMapping("/item/save")
    public String createItemSave(Model model){
        model.addAttribute("itemForm", new SaveItemDto());
        return "item/save";
    }

    @PostMapping("/item/save")
    @ResponseBody
    public Long itemSave(@RequestBody SaveItemDto form,
                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member) throws IOException, InterruptedException {
        log.info("item form {} {}", form.getMid(), form.getKeywords());

        Item savedItem = null;
        Member loginMember = memberService.findById(member.getId()); //session?? failed to lazily initialize a collection of role: com.example.community.domain.member.Member.items, could not initialize proxy
        List<String> keywords = form.getKeywords();

        if(StringUtils.hasText(form.getMid()) && !keywords.isEmpty()) {
            //save item
            Item item = new Item(form.getMid(), loginMember);
            savedItem = itemService.save(item);
            //make keywords
            for(String k:keywords){
                Keyword keyword = new Keyword(k, savedItem);
                keywordService.save(keyword);
            }
        }

        if(savedItem == null){
            return 0L;
        }
        return savedItem.getId();
    }

    @PostMapping("/item/renewRank")
    @ResponseBody
    public String renewRank(@RequestBody RenewForm form,
                            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) throws IOException, InterruptedException {

        log.info("renewRank in ---");
        log.info("form.getItemId = {}", form.getItemId());
        log.info("form.getMid = {}", form.getMid());
        form.setMemberId(loginMember.getId());

        //transaction proxy
        return itemService.renewRank(form);
    }

    @GetMapping("/item/{itemId}")
    public String itemView(Model model, @PathVariable("itemId") Long itemId,
                           @RequestParam(value="page", defaultValue="1") int page,
                           RedirectAttributes redirectAttributes,
                           @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember ){
        log.info("view page param = {}", page);
        Item item = itemService.findById(itemId);
        if(!item.getMember().getId().equals(loginMember.getId())){
            return "redirect:/";
        }
        model.addAttribute("item", item);
        redirectAttributes.addAttribute("page", page);
        return "item/view";
    }


    //specification
    @PostMapping("/item/rankLogRange")
    @ResponseBody
    public String rankLogsRange(@RequestBody KeywordForm form) throws IOException {

        log.info("keyword form {} {} {} {}", form.getStrDate(), form.getEndDate(), form.getStrDateString(), form.getEndDateString());
        ObjectMapper mapper = new ObjectMapper();
        List<Object> returnJsons = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime formatEndDate = LocalDateTime.now();
        LocalDateTime formatStrDate = formatEndDate.minusDays(7);
        //시작일
        if(form.getStrDateString() != ""){
            LocalDate sd = LocalDate.parse(form.getStrDateString(), dateFormatter);
            formatStrDate = sd.atStartOfDay();
            log.info("formatStrDate = {}", formatStrDate);
        }else{
            Keyword keyword = keywordService.findById(form.getKeywordId());
            if(!keyword.getRankLogs().isEmpty()){ //log가 없어서 IndexOutOfBoundsException
                formatStrDate = keyword.getRankLogs().get(0).getCreatedTime();
                log.info("else formatStrDate = {}", formatStrDate) ;
            }
        }
        //종료일
        if(form.getEndDateString() != "") {
            LocalDate ed = LocalDate.parse(form.getEndDateString(), dateFormatter);
            formatEndDate = ed.plusDays(1).atStartOfDay(); //datetime between 마지막 날짜 포함하기 위함
            log.info("formatEndDate = {}", formatEndDate);
        }

        List<RankLog> rankLogs = rankLogService.findByRange(form.getKeywordId(), formatStrDate, formatEndDate);

        for (RankLog rankLog : rankLogs) {
            log.info("rankLogId is = {}", rankLog.getId());
            KeywordForm newForm = new KeywordForm();
            String parsedDate = rankLog.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            newForm.setKeywordId(form.getKeywordId());
            newForm.setRank(rankLog.getItemRank());
            newForm.setDate(parsedDate);
            newForm.setItemName(rankLog.getKeyword().getItem().getItemName());
            newForm.setKeyword(rankLog.getKeyword().getKeyword());
            newForm.setStrDateString(form.getStrDateString());
            newForm.setEndDateString(form.getEndDateString());
            newForm.setRankLogId(rankLog.getId());
            returnJsons.add(newForm);
        }
        String json = mapper.writeValueAsString(returnJsons);
        log.info("json = {}", json);
        return json;
    }

    //querydsl
    @PostMapping("/item/range")
    @ResponseBody
    public String rankLogsRange(@RequestBody RangeKeywordRequest req) throws IOException {
        log.info("req check {} {} {}", req.getKeywordId(), req.getStart(), req.getEnd());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        List<RangeKeywordResponse> results = rankLogService.findWithRange(req);
        String json = mapper.writeValueAsString(results);
        log.info("json = {}", json);
        return json;
    }


    //itemId로 키워드 list 가져오기
    @PostMapping("/item/getKeyword")
    @ResponseBody
    public String getKeyword(@RequestBody HashMap<String,Long> param) throws JsonProcessingException {
        log.info("getKeyword in ---");
        log.info("getKeyword itemId {}", param.get("itemId"));
        ObjectMapper mapper = new ObjectMapper();
        List<Object> returnJsons = new ArrayList<>();
        List<Keyword> keywords = itemService.findById(param.get("itemId")).getKeywords();
        for (Keyword keyword : keywords) {
            GetKeywordForm form = new GetKeywordForm(keyword);
            returnJsons.add(form);
        }
        String json = mapper.writeValueAsString(returnJsons);
        log.info("getKeyword json = {}", json);
        return json;
    }

    @PostMapping("/item/{itemId}/delete")
    public String deleteItem(@PathVariable("itemId") Long itemId,
                             ItemSearchReq searchReq,
                             RedirectAttributes redirectAttributes,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        log.info("deleteItem in ---");
        Item findItem = itemService.findById(itemId);
        if(findItem.getMember().getId().equals(loginMember.getId())){ //본인 아이템 확인
            itemService.delete(itemId);
        }
        redirectAttributes.addFlashAttribute("searchReq", searchReq);
        return "redirect:/item/list";
    }
}
