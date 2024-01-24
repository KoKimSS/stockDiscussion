package com.kss.stockDiscussion.service.authService;

import com.kss.stockDiscussion.domain.certification.Certification;
import com.kss.stockDiscussion.provider.EmailProvider;
import com.kss.stockDiscussion.repository.certificationRepository.CertificationRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.EmailCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.EmailCheckRequestDto;
import com.kss.stockDiscussion.web.dto.response.EmailCertificationResponseDto;
import com.kss.stockDiscussion.web.dto.response.EmailCheckResponseDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final EmailProvider emailProvider;
    private final CertificationRepository certificationRepository;
    @Override
    public ResponseEntity<? super EmailCheckResponseDto> emailCheck(EmailCheckRequestDto dto) {
        try {
            String email = dto.getEmail();
            boolean isExistEmail = userRepository.existsByEmail(email);
            if(isExistEmail) return EmailCheckResponseDto.duplicateEmail();
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return EmailCheckResponseDto.success();
    }

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {
            String email = dto.getEmail();
            // email 이 존재한다면?을 해야 할까?
            String certificationNumber = getCertificationNumber();
            boolean isSuccess = emailProvider.sendCertificationMail(email, certificationNumber);
            if(!isSuccess) return EmailCertificationResponseDto.mailSendFail();

            Certification certification = Certification.builder().email(email).certificationNumber(certificationNumber).build();
            certificationRepository.save(certification);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return EmailCertificationResponseDto.success();
    }

    private String getCertificationNumber() {
        String certificationNumber = "";
        //4자리 숫자로 구성된 번호
        for (int count = 0; count < 4; count++) certificationNumber += (int) (Math.random() * 10);
        return certificationNumber;
    }
}
