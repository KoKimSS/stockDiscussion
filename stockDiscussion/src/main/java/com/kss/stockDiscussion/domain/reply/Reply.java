package com.kss.stockDiscussion.domain.reply;

import com.kss.stockDiscussion.domain.baseEntity.BaseTimeEntity;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "reply_id")
    private Long id;
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private Poster poster;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private int likeCount;

    @Builder
    public Reply(String contents, Poster poster, User user) {
        this.contents = contents;
        this.poster = poster;
        this.user = user;
    }

    public void incrementLikeCount(){
        likeCount++;
    }
}
