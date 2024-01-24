package com.kss.stockDiscussion.domain.follow;

import com.kss.stockDiscussion.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id"})
@Getter
public class Follow {
    @Id
    @GeneratedValue
    @Column(name = "follow_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User following;
}
