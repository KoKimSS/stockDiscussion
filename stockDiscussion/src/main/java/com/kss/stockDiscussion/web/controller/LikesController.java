package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.config.auth.PrincipalDetails;
import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.service.likesService.LikesService;
import com.kss.stockDiscussion.web.dto.request.likes.CreateLikesRequestDto;
import com.kss.stockDiscussion.web.dto.response.likes.CreateLikesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/create-likes")
    ResponseEntity<? super CreateLikesResponseDto> createLikes(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody CreateLikesRequestDto requestBody
    ) {
        Long userId = requestBody.getUserId();
        Long loginId = principalDetails.getUser().getId();
        if (loginId != userId) return CreateLikesResponseDto.certificationFail();

        ResponseEntity<? super CreateLikesResponseDto> response = likesService.createLikes(requestBody);
        return response;
    }

}
