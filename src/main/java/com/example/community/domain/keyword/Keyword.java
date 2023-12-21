package com.example.community.domain.keyword;

import com.example.community.domain.item.Item;
import com.example.community.domain.rankLog.RankLog;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long id;

    private String keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.REMOVE)
    private List<RankLog> rankLogs = new ArrayList<>();

    public void setItem(Item item) {
        this.item = item;
        item.getKeywords().add(this);
    }

    public Keyword(String keyword, Item item) {
        this.keyword = keyword;
        setItem(item);
    }
}
