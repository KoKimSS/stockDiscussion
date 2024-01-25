package com.kss.stockDiscussion.service.authService;

import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.config.jwt.JwtProperties;
import com.kss.stockDiscussion.domain.certification.Certification;
import com.kss.stockDiscussion.domain.jwtBlackList.JwtBlackList;
import com.kss.stockDiscussion.domain.user.Role;
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
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final EmailProvider emailProvider;
    private final CertificationRepository certificationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtBlackListRepository jwtBlackListRepository;
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
            Optional<Certification> existEmail = certificationRepository.findByEmail(email);
            if(existEmail.isPresent()){
                certificationRepository.delete(existEmail.get());
            }
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

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try {

            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            Optional<Certification> byUserEmail = certificationRepository.findByEmail(email);
            if(!byUserEmail.isPresent()) return CheckCertificationResponseDto.certificationFail();

            Certification certification = byUserEmail.get();
            boolean isMatch = isDtoMatchCertification(email, certificationNumber, certification);
            if(!isMatch) return CheckCertificationResponseDto.certificationFail();

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

    private String getCertificationNumber() {
        String certificationNumber = "";
        //4자리 숫자로 구성된 번호
        for (int count = 0; count < 4; count++) certificationNumber += (int) (Math.random() * 10);
        return certificationNumber;
    }
}
