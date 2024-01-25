package com.kss.stockDiscussion.service.posterService;

import com.kss.stockDiscussion.web.dto.request.poster.CreatePosterRequestDto;
import com.kss.stockDiscussion.web.dto.response.poster.CreatePosterResponseDto;
import org.springframework.http.ResponseEntity;

public interface PosterService {

    ResponseEntity<? super CreatePosterResponseDto> createPoster(CreatePosterRequestDto dto);
}
