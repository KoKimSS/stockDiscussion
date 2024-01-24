package com.kss.stockDiscussion.domain.newsFeed;

import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NewsFeed {

    @Id
    @GeneratedValue
    @Column(name = "news_feed_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;  // 활동 타입

    @ManyToOne
    @JoinColumn(name = "related_user_id")
    private User relatedUser;  // 해당 활동과 관련된 사용자

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Poster poster;  // 해당 활동이 포함된 글
}
