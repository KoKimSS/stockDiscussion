package com.kss.stockDiscussion.domain.activity;

import com.kss.stockDiscussion.domain.baseEntity.BaseTimeEntity;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"})
public class Activity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "activity_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;  // 활동 타입
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
    private Activity(User activityUser, User relatedUser, ActivityType activityType, Poster relatedPoster) {
        this.activityUser = activityUser;
        this.relatedUser = relatedUser;
        this.activityType = activityType;
        this.relatedPoster = relatedPoster;
    }
}
