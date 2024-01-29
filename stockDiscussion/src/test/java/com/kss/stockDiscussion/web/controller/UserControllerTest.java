package com.kss.stockDiscussion.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.config.auth.PrincipalDetails;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.restDocs.AbstractRestDocsTests;
import com.kss.stockDiscussion.service.replyService.ReplyService;
import com.kss.stockDiscussion.service.userService.UserService;
import com.kss.stockDiscussion.web.dto.request.reply.CreateReplyRequestDto;
import com.kss.stockDiscussion.web.dto.request.user.UpdatePasswordRequestDto;
import com.kss.stockDiscussion.web.dto.request.user.UpdateProfileRequestDto;
import com.kss.stockDiscussion.web.dto.response.reply.CreateReplyResponseDto;
import com.kss.stockDiscussion.web.dto.response.user.UpdatePasswordResponseDto;
import com.kss.stockDiscussion.web.dto.response.user.UpdateProfileResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.kss.stockDiscussion.common.ResponseMessage.SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class})
public class UserControllerTest extends AbstractRestDocsTests {

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    private final Long loginUserId = 1L;
    @BeforeEach
    void setUp() {
        User user = mock(User.class);
        PrincipalDetails userDetails = mock(PrincipalDetails.class);
        when(user.getId()).thenReturn(loginUserId);
        when(userDetails.getUser()).thenReturn(user);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null,null));
    }

    @DisplayName("패스워드 업데이트")
    @Test
    public void updatePassword() throws Exception {
        //given
        UpdatePasswordRequestDto requestDto = UpdatePasswordRequestDto.builder()
                .userId(loginUserId)
                .password("oldPass123")
                .newPassword("newPass123")
                .build();

        BDDMockito.doReturn(UpdatePasswordResponseDto.success())
                .when(userService)
                .updatePassword(any(UpdatePasswordRequestDto.class));

        // when
        mockMvc.perform(
                        post("/api/user/update-password")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andDo(document("update-password",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("기존 패스워드"),
                                fieldWithPath("newPassword").type(JsonFieldType.STRING)
                                        .description("새로운 패스워드")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description(ResponseCode.SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description(SUCCESS)
                        )
                ));
    }

    @DisplayName("프로필 업데이트")
    @Test
    public void updateProfile() throws Exception {
        //given
        UpdateProfileRequestDto requestDto = UpdateProfileRequestDto.builder()
                .userId(loginUserId)
                .name("userName")
                .imgPath("imgPath")
                .introduction("introduction")
                .build();

        BDDMockito.doReturn(UpdateProfileResponseDto.success())
                .when(userService)
                .updateProfile(any(UpdateProfileRequestDto.class));

        // when
        mockMvc.perform(
                        post("/api/user/update-profile")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andDo(document("update-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("imgPath").type(JsonFieldType.STRING)
                                        .description("이미지 주소"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING)
                                        .description("자기소개")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description(ResponseCode.SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description(SUCCESS)
                        )
                ));
    }
}
