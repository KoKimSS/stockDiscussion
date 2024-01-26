package com.kss.stockDiscussion.service.replyService;

import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.posterRepository.PosterRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.reply.CreateReplyRequestDto;
import com.kss.stockDiscussion.web.dto.response.reply.CreateReplyResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReplyServiceTest {

    @Autowired
    ReplyService replyService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PosterRepository posterRepository;

    @DisplayName("댓글을 생성하는 서비스")
    @Test
    public void createReply() throws Exception {
        //given
        User user = User.builder().name("user").build();
        userRepository.save(user);
        Poster poster = Poster.builder().owner(user).title("poster").build();
        posterRepository.save(poster);

        CreateReplyRequestDto requestDto = CreateReplyRequestDto.builder()
                .userId(user.getId())
                .posterId(poster.getId())
                .contents("댓글")
                .build();

        //when
        ResponseEntity<? super CreateReplyResponseDto> response = replyService.createReply(requestDto);

        //then
        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

}