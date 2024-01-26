package com.kss.stockDiscussion.domain.newsFeed;

import com.kss.stockDiscussion.domain.baseEntity.BaseTimeEntity;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"})
@Table(indexes = @Index(name = "idx_user_id", columnList = "user_id"))
public class NewsFeed extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "news_feed_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private NewsFeedType newsFeedType;  // 활동 타입
    @ManyToOne
    @JoinColumn(name = "activity_user_id")
    private User activityUser;
    @ManyToOne
    @JoinColumn(name = "related_user_id")
    private User relatedUser;
    @ManyToOne
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
