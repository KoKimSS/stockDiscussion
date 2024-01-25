package com.kss.stockDiscussion.repository.jwtBlackListRepository;

import com.kss.stockDiscussion.domain.jwtBlackList.JwtBlackList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtBlackListRepositoryTest {
    @Autowired
    JwtBlackListRepository jwtBlackListRepository;

    @DisplayName("토큰이 블랙리스트에 포함되었는지 찾기")
    @Test
    public void existsByToken() throws Exception {
        //given
        String token1 = "abcd";
        String token2 = "defh";
        JwtBlackList jwtBlackList1 = JwtBlackList.builder().token(token1).build();
        jwtBlackListRepository.save(jwtBlackList1);
        //when
        boolean existsByToken1 = jwtBlackListRepository.existsByToken(token1);
        boolean existsByToken2 = jwtBlackListRepository.existsByToken(token2);

        //then
        Assertions.assertTrue(existsByToken1);
        Assertions.assertFalse(existsByToken2);
    }

}