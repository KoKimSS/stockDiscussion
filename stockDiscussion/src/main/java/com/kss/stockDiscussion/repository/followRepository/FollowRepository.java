package com.kss.stockDiscussion.repository.followRepository;

import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    List<Follow> findByFollowingId(Long followingId);
    List<Follow> findByFollowerId(Long followerId);
}
