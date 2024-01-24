package com.kss.stockDiscussion.service.authService;

import com.kss.stockDiscussion.web.dto.request.auth.CheckCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.EmailCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.EmailCheckRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.SignUpRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.CheckCertificationResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.EmailCertificationResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.EmailCheckResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.SignUpResponseDto;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    ResponseEntity<? super EmailCheckResponseDto> emailCheck(EmailCheckRequestDto dto);

    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);

    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);

    ResponseEntity<? super SignUpResponseDto> singUp(SignUpRequestDto dto);

    ResponseEntity<? super ResponseDto> logOut(HttpServletRequest request);
}
