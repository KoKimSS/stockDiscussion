package com.kss.stockDiscussion.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ValidationMessage;
import com.kss.stockDiscussion.service.authService.AuthService;
import com.kss.stockDiscussion.web.dto.request.auth.CheckCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.EmailCertificationRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.EmailCheckRequestDto;
import com.kss.stockDiscussion.web.dto.request.auth.SignUpRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.security.RunAs;

import static com.kss.stockDiscussion.common.ResponseCode.*;
import static com.kss.stockDiscussion.common.ValidationMessage.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@WithMockUser
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @DisplayName("이메일 중복을 확인한다.")
    @Test
    public void emailCheck() throws Exception {
        //given
        EmailCheckRequestDto requestDto = EmailCheckRequestDto.builder()
                .email("seungsu@gmail.com")
                .build();

        // when
        mockMvc.perform(
                        post("/api/auth/email-check")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("이메일 중복을 확인할 때 이메일은 필수 값이다.")
    @Test
    public void emailCheckWithBlank() throws Exception {
        //given
        EmailCheckRequestDto requestDto = EmailCheckRequestDto.builder()
                .email("")
                .build();

        // when
        mockMvc.perform(
                        post("/api/auth/email-check")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_EMAIL));
    }

    @DisplayName("이메일 중복을 확인할 때 이메일형식에 맞아야 한다.")
    @Test
    public void emailCheckWithNotEmail() throws Exception {
        //given
        EmailCheckRequestDto requestDto = EmailCheckRequestDto.builder()
                .email("JustName")
                .build();

        // when
        mockMvc.perform(
                        post("/api/auth/email-check")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_EMAIL));
    }

    @DisplayName("이메일로 인증코드 발급 한다.")
    @Test
    public void emailCertification() throws Exception {
        //given
        EmailCertificationRequestDto requestDto = EmailCertificationRequestDto.builder()
                .email("seungsu@gmail.com")
                .build();

        // when
        mockMvc.perform(
                        post("/api/auth/email-certification")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("이메일로 인증코드 발급 할 때 이메일은 필수 값이다.")
    @Test
    public void emailCertificationWithBlank() throws Exception {
        //given
        EmailCertificationRequestDto requestDto = EmailCertificationRequestDto.builder()
                .email("")
                .build();

        // when
        mockMvc.perform(
                        post("/api/auth/email-certification")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_EMAIL));
    }

    @DisplayName("이메일로 인증코드 발급 할 때 이메일형식에 맞아야 한다.")
    @Test
    public void emailCertificationWithNotEmail() throws Exception {
        //given
        EmailCertificationRequestDto requestDto = EmailCertificationRequestDto.builder()
                .email("JustName")
                .build();

        // when
        mockMvc.perform(
                        post("/api/auth/email-certification")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_EMAIL));
    }

    @DisplayName("인증코드 확인")
    @Test
    public void checkCertification() throws Exception {
        //given
        CheckCertificationRequestDto requestDto = CheckCertificationRequestDto.builder()
                .certificationNumber("1234")
                .email("seungsu@gmail.com")
                .build();

        // when
        mockMvc.perform(
                        post("/api/auth/check-certification")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("인증 코드 확인 할 때 이메일은 필수 값이다.")
    @Test
    public void checkCertificationWithBlankEmail() throws Exception {
        //given
        CheckCertificationRequestDto requestDto = CheckCertificationRequestDto.builder()
                .certificationNumber("1234")
                .email("")
                .build();

        // when
        mockMvc.perform(
                        post("/api/auth/check-certification")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_EMAIL));
    }

    @DisplayName("인증 코드 확인 할 때 이메일형식에 맞아야 한다.")
    @Test
    public void checkCertificationWithNotEmail() throws Exception {
        //given
        CheckCertificationRequestDto requestDto = CheckCertificationRequestDto.builder()
                .certificationNumber("1234")
                .email("JustName")
                .build();

        // when
        mockMvc.perform(
                        post("/api/auth/check-certification")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_EMAIL));
    }
    @DisplayName("인증 코드 확인 할 때 인증번호는 필수 값 입니다.")
    @Test
    public void checkCertificationWithBlankNumber() throws Exception {
        //given
        CheckCertificationRequestDto requestDto = CheckCertificationRequestDto.builder()
                .certificationNumber("")
                .email("email@naver.com")
                .build();

        // when
        mockMvc.perform(
                        post("/api/auth/check-certification")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_TOKEN));
    }

    @DisplayName("로그아웃")
    @Test
    public void Logout() throws Exception {
        //given
        String validToken = "your-valid-token";

        //when,then
        mockMvc.perform(post("/api/auth/log-out")
                        .header("Authorization", "Bearer " + validToken)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("로그아웃 수행시 헤더의 토큰값은 필수 이다.")
    @Test
    public void LogoutWithBlankToken() throws Exception {
        //given
        String validToken = "your-valid-token";

        //when,then
        mockMvc.perform(post("/api/auth/log-out")
                        .header("Authorization", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("회원가입 수행.")
    @Test
    public void SignUp() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "이름",
                "123123kim",
                "introduction",
                "imgPath",
                "1234",
                "email@email.com");

        //when,then
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입 수행시 이름은 필수 값이다.")
    @Test
    public void SignUpWithBlankName() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "",
                "123123kim",
                "introduction",
                "imgPath",
                "1234",
                "email@email.com");

        //when,then
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_NAME))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 수행시 비밀번호 형식에 맞아야 합니다.")
    @Test
    public void SignUpWithBlankPassword() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "김승수",
                "123",
                "introduction",
                "imgPath",
                "1234",
                "email@email.com");

        //when,then
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_PASSWORD))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 수행시 자기소개는 필수 값 입니다.")
    @Test
    public void SignUpWithBlankIntro() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "김승수",
                "123123kim",
                "",
                "imgPath",
                "1234",
                "email@email.com");

        //when,then
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_INTRO))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 수행시 이미지는 필수 값 입니다.")
    @Test
    public void SignUpWithBlankImg() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "김승수",
                "123123kim",
                "안녕하세요",
                "",
                "1234",
                "email@email.com");

        //when,then
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_IMAGE))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 수행시 토큰은 필수 값 입니다.")
    @Test
    public void SignUpWithBlankNumber() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "김승수",
                "123123kim",
                "안녕하세요",
                "img/path",
                "",
                "email@email.com");

        //when,then
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_BLANK_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 수행시 이메일 형식에 맞아야 합니다.")
    @Test
    public void SignUpWithNotEmail() throws Exception {
        //given
        SignUpRequestDto requestDto = getBuild(
                "김승수",
                "123123kim",
                "안녕하세요",
                "img/path",
                "1234",
                "emailemail.com");

        //when,then
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(NOT_EMAIL))
                .andExpect(status().isBadRequest());
    }

    private static SignUpRequestDto getBuild(String name, String password, String introduction, String imgPath, String certificationNumber, String email) {
        return SignUpRequestDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .introduction(introduction)
                .imgPath(imgPath)
                .certificationNumber(certificationNumber).build();
    }
}