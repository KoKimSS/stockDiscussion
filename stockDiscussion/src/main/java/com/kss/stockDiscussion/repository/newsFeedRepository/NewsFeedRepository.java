package com.kss.stockDiscussion.repository.newsFeedRepository;

import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsFeedRepository extends JpaRepository<NewsFeed,Long> {

    // 나와 관련된 정보를 찾는다. 즉 나를 팔로우 하거나 , 나의 글에 좋아요 댓글을 다는 경우
    Page<NewsFeed> findByRelatedUserId(Long relatedUserId,Pageable pageable);
    // 팔로잉 들의 활동 기록을 찾는다 . 즉 활동 유저가 팔로잉인 뉴스피드를 찾는다.
    Page<NewsFeed> findByActivityUserIn(List<User> activityUsers, Pageable pageable);

}
