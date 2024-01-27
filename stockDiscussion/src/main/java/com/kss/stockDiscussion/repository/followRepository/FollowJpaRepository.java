package com.kss.stockDiscussion.repository.followRepository;

import com.kss.stockDiscussion.domain.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowJpaRepository extends JpaRepository<Follow,Long> {
    List<Follow> findByFollowingId(Long followingId);
    List<Follow> findByFollowerId(Long followerId);
}
