package com.kss.stockDiscussion.domain.like;

import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Likes {

    @Id
    @GeneratedValue
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private Poster poster;  // 예시: 글에 대한 좋아요

    private boolean state;
}
