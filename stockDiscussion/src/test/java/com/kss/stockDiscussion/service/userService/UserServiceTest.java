package com.kss.stockDiscussion.service.userService;

import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.user.UpdatePasswordRequestDto;
import com.kss.stockDiscussion.web.dto.response.user.UpdatePasswordResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @DisplayName("기존과 매칭되는 비밀번호를 이용해 새로운 비밀번호를 업데이트 한다")
    @Test
    public void updatePasswordWithMatchedPassword() throws Exception {
        //given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder().name("user").password(encodedPassword).build();
        userRepository.save(user);

        String newPassword = "newpassword123";

        UpdatePasswordRequestDto requestDto = UpdatePasswordRequestDto.
                builder().userId(user.getId())
                .password(password)
                .newPassword(newPassword)
                .build();

        //when
        ResponseEntity<? super UpdatePasswordResponseDto> response = userService.updatePassword(requestDto);

        //then
        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @DisplayName("기존과 매칭되지 않는 비밀번호를 이용해 새로운 비밀번호를 업데이트를 하여 업데이트 실패 ")
    @Test
    public void updatePasswordWithUnmatchedPassword() throws Exception {
        //given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "password123";
        String wrongPassword = "password456";
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder().name("user").password(encodedPassword).build();
        userRepository.save(user);

        String newPassword = "newpassword123";

        UpdatePasswordRequestDto requestDto = UpdatePasswordRequestDto.
                builder().userId(user.getId())
                .password(wrongPassword)
                .newPassword(newPassword)
                .build();

        //when
        ResponseEntity<? super UpdatePasswordResponseDto> response = userService.updatePassword(requestDto);

        //then
        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);
    }


}