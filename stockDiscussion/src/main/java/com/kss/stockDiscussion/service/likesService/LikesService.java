package com.kss.stockDiscussion.service.likesService;

import com.kss.stockDiscussion.web.dto.request.likes.CreateLikesRequestDto;
import com.kss.stockDiscussion.web.dto.response.likes.CreateLikesResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;

public interface LikesService {

    ResponseEntity<? super CreateLikesResponseDto> createLikes(CreateLikesRequestDto dto);

    @Transactional
    void addReplyLikesCntToRedis(Long replyId);

    @Transactional
    void addPosterLikesCntToRedis(Long posterId);

    @Scheduled(fixedDelay = 1000L*18L)
    @Transactional
    void updateLikesCountByRedis();
}
