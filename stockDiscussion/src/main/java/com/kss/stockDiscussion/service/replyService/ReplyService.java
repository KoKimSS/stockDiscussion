package com.kss.stockDiscussion.service.replyService;

import com.kss.stockDiscussion.web.dto.request.reply.CreateReplyRequestDto;
import com.kss.stockDiscussion.web.dto.response.reply.CreateReplyResponseDto;
import org.springframework.http.ResponseEntity;

public interface ReplyService {
    ResponseEntity<? super CreateReplyResponseDto> createReply(CreateReplyRequestDto dto);
}
