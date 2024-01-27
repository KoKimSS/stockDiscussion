package com.kss.stockDiscussion.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.common.ValidationMessage;
import com.kss.stockDiscussion.config.auth.PrincipalDetails;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.kss.stockDiscussion.common.ResponseCode.VALIDATION_FAIL;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {NewsFeedController.class})
@WithMockUser
class NewsFeedControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NewsFeedService newsFeedService;

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

    @DisplayName("나의 뉴스피드 가져오기")
    @Test
    public void getMyNewsFeed() throws Exception {
        //given
        GetMyNewsFeedRequestDto requestDto = GetMyNewsFeedRequestDto.builder()
                .userId(loginUserId)
                .page(2)
                .size(2)
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/newsFeed/get-myNewsFeed")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("뉴스피드 가져올 시 로그인되어 있는 아이디와 요청 아이디가 같아야 합니다.")
    @Test
    public void getMyNewsFeedWithMisMatchUserAndPrincipalUser() throws Exception {
        //given
        GetMyNewsFeedRequestDto requestDto = GetMyNewsFeedRequestDto.builder()
                .userId(loginUserId +1L)
                .page(2)
                .size(2)
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/newsFeed/get-myNewsFeed")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.CERTIFICATION_FAIL))
                .andExpect(jsonPath("$.message").value(ResponseMessage.CERTIFICATION_FAIL));
    }

    @DisplayName("나의 뉴스피드를 가져올 땐 아이디 값은 필수 입니다.")
    @Test
    public void getMyNewsFeedWithNullUser() throws Exception {
        //given
        GetMyNewsFeedRequestDto requestDto = GetMyNewsFeedRequestDto.builder()
                .page(0)
                .size(2)
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/newsFeed/get-myNewsFeed")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NOT_NULL_USER));
    }
    @DisplayName("나의 뉴스피드를 가져올 때 페이지 수는 0이상입니다.")
    @Test
    public void getMyNewsFeedWithNegativePage() throws Exception {
        //given
        GetMyNewsFeedRequestDto requestDto = GetMyNewsFeedRequestDto.builder()
                .userId(loginUserId)
                .page(-1)
                .size(2)
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/newsFeed/get-myNewsFeed")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.PAGE_MIN_VALUE_0));
    }

    @DisplayName("나의 뉴스피드를 가져올 때 사이즈는 양수입니다.")
    @Test
    public void getMyNewsFeedWithNotPositive() throws Exception {
        //given
        GetMyNewsFeedRequestDto requestDto = GetMyNewsFeedRequestDto.builder()
                .userId(loginUserId)
                .page(0)
                .size(0)
                .build();

        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/newsFeed/get-myNewsFeed")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(VALIDATION_FAIL))
                .andExpect(jsonPath("$.message").value(ValidationMessage.PAGE_SIZE_POSITIVE));
    }
}