package com.kss.stockDiscussion.repository.newsFeedRepository;

import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsFeedJpaRepository extends JpaRepository<NewsFeed,Long> {
    // 유저 아이디와 뉴스피드 타입을 이용하여 페이징으로 뉴스피드 찾기
    Page<NewsFeed> findByUserId(Long userId,Pageable pageable);

    List<NewsFeed> findAllByUserId(Long userId);
}
