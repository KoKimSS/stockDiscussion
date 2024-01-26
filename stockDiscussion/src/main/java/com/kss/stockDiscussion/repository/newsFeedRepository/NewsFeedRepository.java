package com.kss.stockDiscussion.repository.newsFeedRepository;

import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeedType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsFeedRepository extends JpaRepository<NewsFeed,Long> {
    // 유저 아이디와 뉴스피드 타입을 이용하여 페이징으로 뉴스피드 찾기
    Page<NewsFeed> findByUserId(Long userId,Pageable pageable);

    List<NewsFeed> findAllByUserId(Long userId);
}
