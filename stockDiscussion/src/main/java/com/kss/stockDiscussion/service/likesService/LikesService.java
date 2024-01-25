package com.kss.stockDiscussion.service.likesService;

import com.kss.stockDiscussion.web.dto.request.likes.CreateLikesRequestDto;
import com.kss.stockDiscussion.web.dto.response.likes.CreateLikesResponseDto;
import org.springframework.http.ResponseEntity;

public interface LikesService {

    ResponseEntity<? super CreateLikesResponseDto> createLikes(CreateLikesRequestDto dto);
}
