package com.kss.stockDiscussion.service.authService;

import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.domain.certification.Certification;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.certificationRepository.CertificationJpaRepository;
import com.kss.stockDiscussion.repository.jwtBlackListRepository.JwtBlackListJpaRepository;
import com.kss.stockDiscussion.repository.userRepository.UserJpaRepository;
import com.kss.stockDiscussion.web.dto.request.auth.CheckCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.EmailCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.EmailCheckRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.SignUpRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.CheckCertificationResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.EmailCertificationResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.EmailCheckResponseDto;
import com.kss.stockDiscussion.web.dto.response.auth.SignUpResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private CertificationJpaRepository certificationJpaRepository;
    @Autowired
    private JwtBlackListJpaRepository jwtBlackListJpaRepository;

    @DisplayName("이메일이 중복되어 있는 경우")
    @Test
    public void emailCheckWithDuplicatedMail() throws Exception {
        //given
        String email = "email@email.com";
        User user = createUser(email);
        userJpaRepository.save(user);
        EmailCheckRequestDto requestDto = createEmailCheckRequestDto(email);

        //when
        ResponseEntity<? super EmailCheckResponseDto> response = authService.emailCheck(requestDto);

        //then
        assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactlyInAnyOrder(ResponseCode.DUPLICATE_EMAIL, ResponseMessage.DUPLICATE_EMAIL);
    }

    @DisplayName("이메일이 중복되지 않은 경우")
    @Test
    public void emailCheckWithNewMail() throws Exception {
        //given
        String email = "email@email.com";
        String newEmail = "newEmail@gmail.com";
        User user = createUser(email);
        userJpaRepository.save(user);
        EmailCheckRequestDto requestDto = createEmailCheckRequestDto(newEmail);

        //when
        ResponseEntity<? super EmailCheckResponseDto> response = authService.emailCheck(requestDto);

        //then
        assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactlyInAnyOrder(
                        ResponseCode.SUCCESS, ResponseMessage.SUCCESS
                );
    }

    @DisplayName("중복된 이메일인 경우 이메일 인증 코드 발행 실패")
    @Test
    public void emailCertificationWithDuplicateEmail() throws Exception {
        //given
        String email = "email@email.com";
        User user = createUser(email);
        userJpaRepository.save(user);
        EmailCertificationRequestDto duplicatedRequestDto = getEmailCertificationRequestDto(email);

        //when
        ResponseEntity<? super EmailCertificationResponseDto> duplicatedResponse = authService.emailCertification(duplicatedRequestDto);

        //then
        assertThat(duplicatedResponse.getBody())
                .extracting("code", "message")
                .containsExactlyInAnyOrder(
                        ResponseCode.DUPLICATE_EMAIL, ResponseMessage.DUPLICATE_EMAIL
                );
    }

    @DisplayName("새로운 이메일인 경우 이메일 인증 코드 발행 성공")
    @Test
    public void emailCertification_with_newEmail() throws Exception {
        //given
        String email = "email@email.com";
        String newEmail = "newEmail@email.com";
        User user = createUser(email);
        userJpaRepository.save(user);
        EmailCertificationRequestDto successRequestDto = getEmailCertificationRequestDto(newEmail);

        //when
        ResponseEntity<? super EmailCertificationResponseDto> successResponse = authService.emailCertification(successRequestDto);

        //then
        assertThat(successResponse.getBody())
                .extracting("code", "message")
                .containsExactlyInAnyOrder(
                        ResponseCode.SUCCESS, ResponseMessage.SUCCESS
                );
    }

    @DisplayName("올바른 토큰, 이메일, 요청시간 으로 인증 성공")
    @Test
    public void checkCertificationWithValidRequest() throws Exception {
        //given
        String email = "email@naver.com";
        String number = "1234";
        LocalDateTime createdTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime successTime = LocalDateTime.of(2024, 1, 1, 0, AuthServiceImpl.TimeValid);

        Certification certification = createCertification(email, number, createdTime);
        certificationJpaRepository.save(certification);

        CheckCertificationRequestDto successRequest = getCheckCertificationRequestDto(email, number, successTime);
        //when
        ResponseEntity<? super CheckCertificationResponseDto> successResponse = authService.checkCertification(successRequest);
        Boolean isCertified = certificationJpaRepository.findByEmail(email).get().getIsCertified();

        //then
        assertThat(isCertified).isTrue();
        assertThat(successResponse.getBody())
                .extracting("code", "message")
                .containsExactlyInAnyOrder(
                        ResponseCode.SUCCESS, ResponseMessage.SUCCESS
                );
    }

    @DisplayName("시간 초과로 인증 실패")
    @Test
    public void checkCertificationWithExceedTime() throws Exception {
        //given
        String email = "email@naver.com";
        String number = "1234";
        LocalDateTime createdTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime failTime = LocalDateTime.of(2024, 1, 1, 0, AuthServiceImpl.TimeValid + 1);

        Certification certification = createCertification(email, number, createdTime);
        certificationJpaRepository.save(certification);

        CheckCertificationRequestDto timeExceedRequest = getCheckCertificationRequestDto(email, number, failTime);
        //when
        ResponseEntity<? super CheckCertificationResponseDto> timeExceedResponse = authService.checkCertification(timeExceedRequest);
        Boolean isCertified = certificationJpaRepository.findByEmail(email).get().getIsCertified();

        //then
        assertThat(isCertified).isFalse();
        assertThat(timeExceedResponse.getBody())
                .extracting("code", "message")
                .containsExactlyInAnyOrder(
                        ResponseCode.CERTIFICATION_EXPIRED, ResponseMessage.CERTIFICATION_EXPIRED
                );
    }
    @DisplayName("잘못된 메일로 인증 실패")
    @Test
    public void checkCertificationWithWrongEmail() throws Exception {
        //given
        String email = "email@naver.com";
        String number = "1234";
        String wrongEmail = "wrong@naver.com";
        LocalDateTime createdTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime successTime = LocalDateTime.of(2024, 1, 1, 0, AuthServiceImpl.TimeValid);

        Certification certification = createCertification(email, number, createdTime);
        certificationJpaRepository.save(certification);

        CheckCertificationRequestDto wrongEmailRequest = getCheckCertificationRequestDto(wrongEmail, number, successTime);
        //when
        ResponseEntity<? super CheckCertificationResponseDto> wrongEmailResponse = authService.checkCertification(wrongEmailRequest);
        Boolean isCertified = certificationJpaRepository.findByEmail(email).get().getIsCertified();

        //then
        assertThat(isCertified).isFalse();

        assertThat(wrongEmailResponse.getBody())
                .extracting("code", "message")
                .containsExactlyInAnyOrder(
                        ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL
                );
    }
    @DisplayName("잘못된 번호로 인증 실패")
    @Test
    public void checkCertificationWithWrongNumber() throws Exception {
        //given
        String email = "email@naver.com";
        String number = "1234";
        String wrongNumber = "5678";
        LocalDateTime createdTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime successTime = LocalDateTime.of(2024, 1, 1, 0, AuthServiceImpl.TimeValid);

        Certification certification = createCertification(email, number, createdTime);
        certificationJpaRepository.save(certification);

        CheckCertificationRequestDto wrongNumberRequest = getCheckCertificationRequestDto(email, wrongNumber, successTime);

        //when
        ResponseEntity<? super CheckCertificationResponseDto> wrongNumberResponse = authService.checkCertification(wrongNumberRequest);
        Boolean isCertified = certificationJpaRepository.findByEmail(email).get().getIsCertified();

        //then
        assertThat(isCertified).isFalse();
        assertThat(wrongNumberResponse.getBody())
                .extracting("code", "message")
                .containsExactlyInAnyOrder(
                        ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL
                );
    }

    @DisplayName("유효한 이메일, 토큰 번호, 토큰 인증 성공 상태로 회원가입 성공")
    @Test
    public void singUpWithValidRequest() throws Exception {
        //given
        String number = "1234";
        String email = "email@naver.com";
        Certification certification = createCertification(email, number, LocalDateTime.now());
        certificationJpaRepository.save(certification);
        certification.certificated();
        SignUpRequestDto successRequestDto = getSignUpRequestDto(number, email);

        //when
        ResponseEntity<? super SignUpResponseDto> successResponse = authService.singUp(successRequestDto);

        //then
        assertThat(successResponse.getBody())
                .extracting("code", "message")
                .containsExactlyInAnyOrder(
                        ResponseCode.SUCCESS, ResponseMessage.SUCCESS
                );

    }

    @DisplayName("인증된 메일과 다른 이메일로 회원가입 실패")
    @Test
    public void singUpWithWrongEmail() throws Exception {
        //given
        String number = "1234";
        String email = "email@naver.com";
        String wrongEmail = "wrong@naver.com";
        Certification certification = createCertification(email, number, LocalDateTime.now());
        certification.certificated();
        certificationJpaRepository.save(certification);
        SignUpRequestDto wrongEmailRequestDto = getSignUpRequestDto(number, wrongEmail);

        //when
        ResponseEntity<? super SignUpResponseDto> wrongEmailResponse = authService.singUp(wrongEmailRequestDto);

        //then
        assertThat(wrongEmailResponse.getBody())
                .extracting("code", "message")
                .containsExactlyInAnyOrder(
                        ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR
                );
    }
    @DisplayName("인증된 토큰 번호와 다른 토큰으로 회원가입 실패")
    @Test
    public void singUp() throws Exception {
        //given
        String number = "1234";
        String email = "email@naver.com";
        String wrongNumber = "5678";
        Certification certification = createCertification(email, number, LocalDateTime.now());
        certificationJpaRepository.save(certification);
        certification.certificated();
        SignUpRequestDto wrongNumberRequestDto = getSignUpRequestDto(wrongNumber, email);

        //when
        ResponseEntity<? super SignUpResponseDto> wrongNumberResponse = authService.singUp(wrongNumberRequestDto);

        //then
        assertThat(wrongNumberResponse.getBody())
                .extracting("code", "message")
                .containsExactlyInAnyOrder(
                        ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL
                );
    }

    @DisplayName("로그인이 되어 있는 경우 JWT 블랙리스트에 현재 토큰 추가")
    @Test
    public void logOut() throws Exception {
        //given
        String token = "1234";

        //when
        ResponseEntity<? super ResponseDto> response = authService.logOut(token);
        boolean existsByToken = jwtBlackListJpaRepository.existsByToken(token);
        //then
        Assertions.assertThat(existsByToken).isTrue();
        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }
    private static User createUser(String email) {
        return User.builder().name("user").email(email).build();
    }
    private Certification createCertification(String email, String number, LocalDateTime createdTime) {
        return Certification.builder().email(email)
                .certificationNumber(number)
                .localDateTime(createdTime).build();
    }

    private static EmailCheckRequestDto createEmailCheckRequestDto(String email) {
        return EmailCheckRequestDto.builder().email(email).build();
    }

    private static EmailCertificationRequestDto getEmailCertificationRequestDto(String email) {
        return EmailCertificationRequestDto.builder().email(email).build();
    }

    private static CheckCertificationRequestDto getCheckCertificationRequestDto(String email, String number, LocalDateTime successTime) {
        return CheckCertificationRequestDto.builder().email(email)
                .certificationNumber(number).certificateTime(successTime).build();
    }
    private static SignUpRequestDto getSignUpRequestDto(String number, String email) {
        return SignUpRequestDto.builder()
                .email(email)
                .password("12345678")
                .name("유저")
                .introduction("안녕하세요")
                .imgPath("img_path")
                .certificationNumber(number).build();
    }


}