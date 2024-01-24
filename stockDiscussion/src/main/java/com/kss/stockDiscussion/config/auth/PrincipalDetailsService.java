package com.kss.stockDiscussion.config.auth;

import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * http://localhost:8080/login 일 때 동작
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername 발동");
        System.out.println("loginId = " + loginId);
        User user = userRepository.findByLoginId(loginId).orElseThrow(()->new UsernameNotFoundException("유저네임이없습니다"));
        return new PrincipalDetails(user);
    }
}
