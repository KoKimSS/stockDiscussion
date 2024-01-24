package com.kss.stockDiscussion.service.authService;

import com.kss.stockDiscussion.web.dto.request.EmailCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.EmailCheckRequestDto;
import com.kss.stockDiscussion.web.dto.response.EmailCertificationResponseDto;
import com.kss.stockDiscussion.web.dto.response.EmailCheckResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<? super EmailCheckResponseDto> emailCheck(EmailCheckRequestDto dto);

    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
}
