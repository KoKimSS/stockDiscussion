package com.kss.stockDiscussion.repository.jwtBlackListRepository;

import com.kss.stockDiscussion.domain.jwtBlackList.JwtBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtBlackListJpaRepository extends JpaRepository<JwtBlackList,Long> {
    boolean existsByToken(String token);
}
