package com.kss.stockDiscussion.domain.reply;

import com.kss.stockDiscussion.domain.baseEntity.BaseTimeEntity;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "reply_id")
    private Long id;
    private String contents;
    @ManyToOne
    @JoinColumn(name = "parents_id")
    private Reply parentsReply;
    @OneToMany(mappedBy = "parentsReply")
    private List<Reply> childReplies;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private Poster poster;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;
}
