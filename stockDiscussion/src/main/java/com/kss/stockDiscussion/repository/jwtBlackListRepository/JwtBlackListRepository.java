package com.kss.stockDiscussion.repository.jwtBlackListRepository;

import com.kss.stockDiscussion.domain.jwtBlackList.JwtBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtBlackListRepository extends JpaRepository<JwtBlackList,Long> {
    
}
