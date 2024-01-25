package com.kss.stockDiscussion.repository.userRepository;

import com.kss.stockDiscussion.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("이메일로 유저를 찾는다")
    @Test
    public void findByEmail() throws Exception {
        //given
        String email = "email@email.com";
        User user = User.builder().email(email).build();
        userRepository.save(user);

        //when
        User userByEmail = userRepository.findByEmail(email).get();

        //then
        assertThat(userByEmail).isEqualTo(user);
    }

    @DisplayName("이메일로 유저 존재유무를 찾는다")
    @Test
    public void existsByEmail() throws Exception {
        //given
        String email = "email@email.com";
        User user = User.builder().email(email).build();
        userRepository.save(user);

        //when
        boolean existsByEmail = userRepository.existsByEmail(email);

        //then
        assertTrue(existsByEmail);
    }
}