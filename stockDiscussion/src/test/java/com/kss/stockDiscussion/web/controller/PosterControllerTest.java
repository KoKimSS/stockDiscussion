package com.kss.stockDiscussion.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.common.ValidationMessage;
import com.kss.stockDiscussion.config.auth.PrincipalDetails;
import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.like.LikeType;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.service.posterService.PosterService;
import com.kss.stockDiscussion.web.controller.LikesController;
import com.kss.stockDiscussion.web.dto.request.likes.CreateLikesRequestDto;
import com.kss.stockDiscussion.web.dto.request.poster.CreatePosterRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.kss.stockDiscussion.common.ResponseCode.CERTIFICATION_FAIL;
import static com.kss.stockDiscussion.common.ResponseCode.VALIDATION_FAIL;
import static com.kss.stockDiscussion.common.ResponseMessage.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PosterController.class})
@WithMockUser
class PosterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PosterService posterService;


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

    @DisplayName("포스터 생성")
    @Test
    public void createPoster() throws Exception {
        //given
        CreatePosterRequestDto requestDto = CreatePosterRequestDto.builder()
                .userId(loginUserId)
                .title("제목")
                .contents("컨텐츠")
                .build();
        // when
        mockMvc.perform(
                        post("/api/poster/create-poster")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("포스터 생성시 시 로그인되어 있는 아이디와 요청 아이디가 같아야 합니다.")
    @Test
    public void createLikesWithMisMatchUserAndLoginUser() throws Exception {
        //given
        CreatePosterRequestDto requestDto = CreatePosterRequestDto.builder()
                .userId(loginUserId+1L)
                .title("제목")
                .contents("컨텐츠")
                .build();
        // when
        mockMvc.perform(
                        post("/api/poster/create-poster")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(CERTIFICATION_FAIL))
                .andExpect(jsonPath("$.message").value(ResponseMessage.CERTIFICATION_FAIL));
    }

    @DisplayName("포스터 생성시 시 포스터 제목은 필수 값 입니다.")
    @Test
    public void createLikesWithBlankTitle() throws Exception {
        //given
        CreatePosterRequestDto requestDto = CreatePosterRequestDto.builder()
                .userId(loginUserId)
                .title("")
                .contents("컨텐츠")
                .build();
        // when
        mockMvc.perform(
                        post("/api/poster/create-poster")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_BLANK_TITLE));
    }

    @DisplayName("포스터 생성시 시 포스터 내용은 필수 값 입니다.")
    @Test
    public void createLikesWithBlankContents() throws Exception {
        //given
        CreatePosterRequestDto requestDto = CreatePosterRequestDto.builder()
                .userId(loginUserId)
                .title("제목")
                .contents("")
                .build();
        // when
        mockMvc.perform(
                        post("/api/poster/create-poster")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_BLANK_CONTENTS));
    }
}
