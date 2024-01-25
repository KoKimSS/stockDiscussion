package com.kss.stockDiscussion.domain.newsFeed;

import com.kss.stockDiscussion.domain.activity.ActivityType;
import com.kss.stockDiscussion.domain.baseEntity.BaseTimeEntity;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"})
public class NewsFeed extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "news_feed_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private com.kss.stockDiscussion.domain.newsFeed.NewsFeedType newsFeedType;  // 활동 타입
    @ManyToOne
    @JoinColumn(name = "activity_user_id")
    private User activityUser;
    @ManyToOne
    @JoinColumn(name = "related_user_id")
    private User relatedUser;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Poster relatedPoster;  // 해당 활동이 포함된 글

    private String message;

    @Builder
    private NewsFeed(User user, NewsFeedType newsFeedType, User activityUser, User relatedUser, Poster relatedPoster) {
        this.user = user;
        this.newsFeedType = newsFeedType;
        this.activityUser = activityUser;
        this.relatedUser = relatedUser;
        this.relatedPoster = relatedPoster;
        this.message = MessageMapper();
    }

    private String MessageMapper() {
        if(newsFeedType== NewsFeedType.MY_FOLLOW){
            return activityUser.getName() + "님이 "+user.getName()+"를 팔로우 합니다";
        }
        if(newsFeedType== NewsFeedType.FOLLOWING_REPLY){
            return activityUser.getName() + "님이 "+relatedUser.getName()+" 님의 " + relatedPoster.getTitle() + " 글에 답글을 달았습니다.";
        }
        if(newsFeedType== NewsFeedType.FOLLOWING_LIKE){
            return activityUser.getName() + "님이 "+relatedUser.getName()+" 님의 " + relatedPoster.getTitle() + " 글을 좋아합니다.";
        }
        if(newsFeedType== NewsFeedType.FOLLOWING_POST){
            return activityUser.getName() + "님이 글 " + relatedPoster.getTitle() + "을 작성하셨습니다.";
        }
        if(newsFeedType== NewsFeedType.MY_FOLLOW){
            return activityUser.getName() + "님이 나를 팔로우 합니다";
        }
        if(newsFeedType== NewsFeedType.MY_RELY){
            return activityUser.getName() + "님이 나의 " + relatedPoster.getTitle() + " 글에 답글을 달았습니다.";
        }
        if(newsFeedType== NewsFeedType.MY_LIKE){
            return activityUser.getName() + "님이 나의 " + relatedPoster.getTitle() + " 글에 좋아요를 눌렀습니다.";
        }

        return null;
    }
}
