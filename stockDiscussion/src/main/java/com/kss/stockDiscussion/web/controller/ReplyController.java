package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.service.replyService.ReplyService;
import com.kss.stockDiscussion.web.dto.request.reply.CreateReplyRequestDto;
import com.kss.stockDiscussion.web.dto.response.reply.CreateReplyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            @Valid @RequestBody CreateReplyRequestDto requestBody
    ){
        ResponseEntity<? super CreateReplyResponseDto> response = replyService.createReply(requestBody);
        return response;
    }

}
