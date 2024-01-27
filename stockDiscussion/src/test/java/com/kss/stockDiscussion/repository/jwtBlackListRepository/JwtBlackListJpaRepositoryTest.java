package com.kss.stockDiscussion.repository.jwtBlackListRepository;

import com.kss.stockDiscussion.domain.jwtBlackList.JwtBlackList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class JwtBlackListJpaRepositoryTest {
    @Autowired
    JwtBlackListJpaRepository jwtBlackListJpaRepository;

    @DisplayName("저장된 토큰 번호로 존재 유무 확인")
    @Test
    public void existsBySavedToken() throws Exception {
        //given
        String savedToken = "1234";
        JwtBlackList jwtBlackList1 = JwtBlackList.builder().token(savedToken).build();
        jwtBlackListJpaRepository.save(jwtBlackList1);

        //when
        boolean existsBySavedToken = jwtBlackListJpaRepository.existsByToken(savedToken);

        //then
        Assertions.assertTrue(existsBySavedToken);
    }

    @DisplayName("저장되지 않은 토큰 번호로 존재 유무 확인")
    @Test
    public void existsByUnknownToken() throws Exception {
        //given
        String savedToken = "1234";
        String unknownToken = "5678";
        JwtBlackList jwtBlackList1 = JwtBlackList.builder().token(savedToken).build();
        jwtBlackListJpaRepository.save(jwtBlackList1);
        //when
        boolean existsByUnknownToken = jwtBlackListJpaRepository.existsByToken(unknownToken);

        //then
        Assertions.assertFalse(existsByUnknownToken);
    }

}