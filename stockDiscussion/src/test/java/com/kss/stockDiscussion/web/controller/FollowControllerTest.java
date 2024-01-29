package com.kss.stockDiscussion.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.common.ValidationMessage;
import com.kss.stockDiscussion.config.auth.PrincipalDetails;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.restDocs.AbstractRestDocsTests;
import com.kss.stockDiscussion.service.followService.FollowService;
import com.kss.stockDiscussion.web.dto.request.follow.StartFollowRequestDto;
import com.kss.stockDiscussion.web.dto.response.follow.StartFollowResponseDto;
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
import org.springframework.security.test.context.support.WithUserDetails;

import static com.kss.stockDiscussion.common.ResponseCode.CERTIFICATION_FAIL;
import static com.kss.stockDiscussion.common.ResponseCode.VALIDATION_FAIL;
import static com.kss.stockDiscussion.common.ResponseMessage.SUCCESS;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FollowController.class})
class FollowControllerTest extends AbstractRestDocsTests {
    private final Long loginUserId = 1L;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private FollowService followService;

    @BeforeEach
    void setUp() {
        User user = mock(User.class);
        PrincipalDetails userDetails = mock(PrincipalDetails.class);
        when(user.getId()).thenReturn(loginUserId);
        when(userDetails.getUser()).thenReturn(user);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, null));
    }

    @DisplayName("팔로우를 수행.")
    @WithUserDetails
    @Test
    public void startFollow() throws Exception {
        // Given
        StartFollowRequestDto requestDto = StartFollowRequestDto.builder()
                .followerId(loginUserId)
                .followingId(123L)
                .build();

        BDDMockito.doReturn(StartFollowResponseDto.success())
                .when(followService)
                .follow(any(StartFollowRequestDto.class));

        mockMvc.perform(
                        post("/api/follow/start-follow")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andDo(document("start-follow",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("followerId").type(JsonFieldType.NUMBER)
                                        .description("팔로워 아이디"),
                                fieldWithPath("followingId").type(JsonFieldType.NUMBER)
                                        .description("팔로잉 아이디")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description(ResponseCode.SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description(SUCCESS)
                        )
                ));
    }

    @DisplayName("팔로우를 수행시 로그인 아이디와 팔로워 아이디의 값이 같아야 합니다.")
    @WithUserDetails
    @Test
    public void startFollowWithMisMatchFollowerIdAndLoginUserId() throws Exception {
        // Given
        StartFollowRequestDto requestDto = StartFollowRequestDto.builder()
                .followerId(loginUserId + 1L)
                .followingId(123L)
                .build();

        mockMvc.perform(
                        post("/api/follow/start-follow")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(CERTIFICATION_FAIL))
                .andExpect(jsonPath("$.message").value(ResponseMessage.CERTIFICATION_FAIL));
    }

    @DisplayName("팔로우를 수행 시 팔로잉 아이디는 필수 값 입니다.")
    @Test
    public void startFollowWithNullFollowing() throws Exception {
        //given
        StartFollowRequestDto requestDto = StartFollowRequestDto.builder()
                .followerId(loginUserId)
                .build();

        // when
        mockMvc.perform(
                        post("/api/follow/start-follow")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_NULL_FOLLOWING));
    }

    @DisplayName("팔로우를 수행 시 팔로워 아이디는 필수 값 입니다.")
    @Test
    public void startFollowWithNullFollower() throws Exception {
        //given
        StartFollowRequestDto requestDto = StartFollowRequestDto.builder()
                .followingId(2L)
                .build();

        // when
        mockMvc.perform(
                        post("/api/follow/start-follow")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_NULL_FOLLOWER));
    }
}