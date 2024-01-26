package com.kss.stockDiscussion.service.posterService;

import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.poster.CreatePosterRequestDto;
import com.kss.stockDiscussion.web.dto.response.poster.CreatePosterResponseDto;
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
class PosterServiceTest {

    @Autowired
    private PosterService posterService;
    @Autowired
    private UserRepository userRepository;

    @DisplayName("포스터를 생성하는 서비스 이다")
    @Test
    public void createPoster() throws Exception {
        //given
        User user = getUser();
        userRepository.save(user);
        Long userId = user.getId();
        String title = "title";
        String contents = "contents";
        CreatePosterRequestDto requestDto = CreatePosterRequestDto.
                builder().title(title).contents(contents).userId(userId).build();

        //when
        ResponseEntity<? super CreatePosterResponseDto> response = posterService.createPoster(requestDto);

        //then
        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private static User getUser() {
        return User.builder().name("user1").build();
    }
}