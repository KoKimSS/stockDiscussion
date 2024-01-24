package com.kss.stockDiscussion.domain.poster;

import com.kss.stockDiscussion.domain.like.Like;
import com.kss.stockDiscussion.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Poster {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "poster", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();
}
