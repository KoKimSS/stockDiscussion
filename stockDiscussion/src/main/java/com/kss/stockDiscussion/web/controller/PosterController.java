package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.config.auth.PrincipalDetails;
import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.service.posterService.PosterService;
import com.kss.stockDiscussion.web.dto.request.poster.CreatePosterRequestDto;
import com.kss.stockDiscussion.web.dto.response.poster.CreatePosterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.kss.stockDiscussion.config.jwt.JwtUtil.findUserFromAuth;

@RestController
@RequestMapping("/api/poster")
@RequiredArgsConstructor
public class PosterController {

    private final PosterService posterService;

    @PostMapping("/create-poster")
    ResponseEntity<?super CreatePosterResponseDto> createPoster(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid@RequestBody CreatePosterRequestDto requestBody
    ){
        Long loginId = principalDetails.getUser().getId();
        Long userId = requestBody.getUserId();
        if (loginId != userId) return CreatePosterResponseDto.certificationFail();

        ResponseEntity<? super CreatePosterResponseDto> response = posterService.createPoster(requestBody);
        return response;
    }

    @PostMapping("/get-poster")
    ResponseEntity getPoster(

    ) {
        return null;
    }
}
