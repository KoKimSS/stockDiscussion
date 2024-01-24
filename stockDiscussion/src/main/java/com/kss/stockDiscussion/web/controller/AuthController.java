package com.kss.stockDiscussion.web.controller;


import com.kss.stockDiscussion.service.authService.AuthService;
import com.kss.stockDiscussion.web.dto.request.EmailCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.EmailCheckRequestDto;
import com.kss.stockDiscussion.web.dto.response.EmailCertificationResponseDto;
import com.kss.stockDiscussion.web.dto.response.EmailCheckResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestBody@Valid EmailCertificationRequestDto requestBody
            ) {
        ResponseEntity<? super EmailCertificationResponseDto> response = authService.emailCertification(requestBody);
        return response;
    }

}
