package com.example.community.domain.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Data
public class PageMaker {
    private int pageNum; //현재페이지
    private int total;  //전체 item 개수
    private int amount; //한 페이지에 보일 item개수
    private int maxPage; //하단 리스트 페이지 개수
    private int endPage; //현재 페이지에 따른 마지막 페이지
    private int startPage;  //현재 페이지에 따른 시작 페이지
    private int realEndPage;           //찐 마지막 페이지
    private boolean prev=false;       //이전 유무
    private boolean next=false;       //이후 유무


    public PageMaker(int pageNum, Long total, int amount, int maxPage) {
        this.pageNum = pageNum;
        this.total = Long.valueOf(Optional.ofNullable(total).orElse(0L)).intValue();
        this.amount = amount;
        this.maxPage = maxPage;

        this.endPage = (int) (Math.ceil(this.pageNum/Double.valueOf(this.maxPage))*maxPage); //double형으로 바꿔주기
        this.startPage = this.endPage-this.maxPage+1;
        this.realEndPage = (int) (Math.ceil(this.total/Double.valueOf(this.amount))); //double형으로 바꿔주기

        //prev
        if(this.startPage>1){
            this.prev=true;
        }


        //next
        //pageNum=16
        //endPage=20
        //realEndPage=18
        if(this.endPage>this.realEndPage){
            this.endPage= this.realEndPage;
        }


        //next
        //pageNum=16
        //endPage=18
        //realEndPage=18

        if(this.endPage<this.realEndPage){
            this.next=true;
        }

        //this
        //pageMakerForm json ajax
        log.info("pageNum = {}", this.pageNum);
        log.info("total = {}", total);
        log.info("amount = {}", amount);
        log.info("maxPage = {}", maxPage);
        log.info("endPage = {}", endPage);
        log.info("startPage = {}", startPage);
        log.info("realEndPage = {}", realEndPage);
        log.info("prev = {}", prev);
        log.info("next = {}", next);
        log.info("maxPage.Double = {}", Double.valueOf(maxPage));
    }

}
