package com.kss.stockDiscussion.service.authService;

import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.config.jwt.JwtProperties;
import com.kss.stockDiscussion.domain.certification.Certification;
import com.kss.stockDiscussion.domain.jwtBlackList.JwtBlackList;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.provider.EmailProvider;
import com.kss.stockDiscussion.repository.certificationRepository.CertificationRepository;
import com.kss.stockDiscussion.repository.jwtBlackListRepository.JwtBlackListRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.auth.CheckCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.EmailCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.EmailCheckRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.SignUpRequestDto;
import com.kss.stockDiscussion.web.dto.response.auth.CheckCertificationResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.EmailCertificationResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.EmailCheckResponseDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.SignUpResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final EmailProvider emailProvider;
    private final CertificationRepository certificationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtBlackListRepository jwtBlackListRepository;
    public static final int TimeValid = 2;
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

            boolean isExistEmail = userRepository.existsByEmail(email);
            if(isExistEmail) return EmailCertificationResponseDto.duplicateEmail();

            //기존에 인증시도 기록이 있으면 삭제하고 생성
            Optional<Certification> existByEmail = certificationRepository.findByEmail(email);
            if(existByEmail.isPresent()){
                certificationRepository.delete(existByEmail.get());
            }
            String certificationNumber = createCertificationNumber();
            boolean isEmailSendSuccessful = emailProvider.sendCertificationMail(email, certificationNumber);
            if(!isEmailSendSuccessful) return EmailCertificationResponseDto.mailSendFail();

            Certification certification = Certification.builder().email(email).certificationNumber(certificationNumber).build();
            certificationRepository.save(certification);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try {
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();
            LocalDateTime certificateTime = dto.getCertificateTime();

            Optional<Certification> byUserEmail = certificationRepository.findByEmail(email);
            if(!byUserEmail.isPresent()) return CheckCertificationResponseDto.certificationFail();

            Certification certification = byUserEmail.get();

            boolean isMatch = isDtoMatchCertification(email, certificationNumber, certification);
            if(!isMatch) return CheckCertificationResponseDto.certificationFail();

            boolean timeValid = isCertificationTimeValid(certification,certificateTime);
            if(!timeValid) return CheckCertificationResponseDto.certificationExpired();
            certification.certificated();
            System.out.println("certification = " + certification.getIsCertified());

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return CheckCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignUpResponseDto> singUp(SignUpRequestDto dto) {

        try {
            String email = dto.getEmail();
            boolean existsByEmail = userRepository.existsByEmail(email);
            if(existsByEmail) return SignUpResponseDto.duplicateEmail();

            String certificationNumber = dto.getCertificationNumber();
            Optional<Certification> optionalCertificationByEmail = certificationRepository.findByEmail(email);

            if(!optionalCertificationByEmail.isPresent()) SignUpResponseDto.certificationFail();
            Certification certificationByEmail = optionalCertificationByEmail.get();

            if(!isDtoMatchCertification(email,certificationNumber,certificationByEmail))
                return SignUpResponseDto.certificationFail();

            String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
            User user = User.builder()
                    .email(email)
                    .password(encodedPassword)
                    .name(dto.getName())
                    .introduction(dto.getIntroduction())
                    .imgPath(dto.getImgPath())
                    .build();

            userRepository.save(user);
            certificationRepository.deleteByEmail(email);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignUpResponseDto.success();
    }

    @Override
    public ResponseEntity<?super ResponseDto> logOut(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");
        boolean existsByToken = jwtBlackListRepository.existsByToken(token);
        if(existsByToken) return ResponseDto.validationFail();
        JwtBlackList jwtBlackList = JwtBlackList.builder().token(token).build();
        jwtBlackListRepository.save(jwtBlackList);
        ResponseDto responseBody = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    private static boolean isDtoMatchCertification(String email, String certificationNumber, Certification certification) {
        return certification.getEmail().equals(email) && certificationNumber.equals(certification.getCertificationNumber());
    }

    private String createCertificationNumber() {
        String certificationNumber = "";
        //4자리 숫자로 구성된 번호
        for (int count = 0; count < 4; count++) certificationNumber += (int) (Math.random() * 10);
        return certificationNumber;
    }

    private boolean isCertificationTimeValid(Certification certification,LocalDateTime certificateTime) {
        LocalDateTime certificationCreationTime = certification.getCreatedDate();
        Duration duration = Duration.between(certificationCreationTime, certificateTime);
        return duration.toMinutes() <= TimeValid;
    }
}
