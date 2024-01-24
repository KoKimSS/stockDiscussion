package com.kss.stockDiscussion.repository.followRepository;

import com.kss.stockDiscussion.domain.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long> {
}
