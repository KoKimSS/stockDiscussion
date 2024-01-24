package com.kss.stockDiscussion.domain.like;

import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;

import javax.persistence.*;

@Entity
public class Like {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "poster_id")
    private Poster poster;  // 예시: 글에 대한 좋아요

    private boolean state;
}
