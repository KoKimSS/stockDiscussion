package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.config.auth.PrincipalDetails;
import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.service.replyService.ReplyService;
import com.kss.stockDiscussion.web.dto.request.reply.CreateReplyRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.reply.CreateReplyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/create-reply")
    ResponseEntity<?super CreateReplyResponseDto> createReply(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody CreateReplyRequestDto requestBody
    ){
        Long loginId = principalDetails.getUser().getId();
        Long userId = requestBody.getUserId();
        if(loginId!= userId) return ResponseDto.certificationFail();

        ResponseEntity<? super CreateReplyResponseDto> response = replyService.createReply(requestBody);
        return response;
    }

}
