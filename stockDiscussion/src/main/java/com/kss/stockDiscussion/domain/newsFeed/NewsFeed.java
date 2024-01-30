package com.kss.stockDiscussion.domain.newsFeed;

import com.kss.stockDiscussion.domain.baseEntity.BaseTimeEntity;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"})
@Table
public class NewsFeed extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "news_feed_id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private NewsFeedType newsFeedType;  // 활동 타입
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "activity_user_id")
    private User activityUser;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "related_user_id")
    private User relatedUser;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Poster relatedPoster;  // 해당 활동이 포함된 글

    @Builder
    private NewsFeed(User user, NewsFeedType newsFeedType, User activityUser, User relatedUser, Poster relatedPoster) {
        this.user = user;
        this.newsFeedType = newsFeedType;
        this.activityUser = activityUser;
        this.relatedUser = relatedUser;
        this.relatedPoster = relatedPoster;
    }
}
