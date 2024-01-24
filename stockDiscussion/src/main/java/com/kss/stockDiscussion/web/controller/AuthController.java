package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.service.authService.AuthService;
import com.kss.stockDiscussion.web.dto.request.auth.CheckCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.EmailCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.EmailCheckRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.SignUpRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.CheckCertificationResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.EmailCertificationResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.EmailCheckResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.SignUpResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/email-check")
    public ResponseEntity<? super EmailCheckResponseDto> emailCheck(
            @RequestBody @Valid EmailCheckRequestDto requestBody
    ) {
        ResponseEntity<? super EmailCheckResponseDto> response = authService.emailCheck(requestBody);
        return response;
    }

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(
            @RequestBody @Valid EmailCertificationRequestDto requestBody
    ) {
        ResponseEntity<? super EmailCertificationResponseDto> response = authService.emailCertification(requestBody);
        return response;
    }

    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(
            @RequestBody @Valid CheckCertificationRequestDto requestBody
    ) {
        ResponseEntity<? super CheckCertificationResponseDto> response = authService.checkCertification(requestBody);
        return response;
    }

    @PostMapping("/log-out")
    public ResponseEntity<? super ResponseDto> logOut(HttpServletRequest request) {
        ResponseEntity<? super ResponseDto> response = authService.logOut(request);
        return response;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<? super SignUpResponseDto> signUp(
            @RequestBody @Valid SignUpRequestDto requestBody
    ) {
        ResponseEntity<? super SignUpResponseDto> response = authService.singUp(requestBody);
        return response;
    }
}
