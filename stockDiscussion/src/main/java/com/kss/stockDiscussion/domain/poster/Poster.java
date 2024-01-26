package com.kss.stockDiscussion.domain.poster;

import com.kss.stockDiscussion.domain.baseEntity.BaseTimeEntity;
import com.kss.stockDiscussion.domain.like.Likes;
import com.kss.stockDiscussion.domain.reply.Reply;
import com.kss.stockDiscussion.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode(of = {"id","title"})
public class Poster extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "poster_id")
    private Long id;

    private String title;
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;
    private int likeCount;

    @OneToMany(mappedBy = "poster", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();
    @OneToMany(mappedBy = "poster", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @Builder
    public Poster(String title, String contents, User owner) {
        this.title = title;
        this.contents = contents;
        this.owner = owner;
        this.likeCount=0;
    }

    public void incrementLikeCount(){
        likeCount++;
    }
}
