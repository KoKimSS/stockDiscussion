package com.kss.stockDiscussion.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.common.ValidationMessage;
import com.kss.stockDiscussion.config.auth.PrincipalDetails;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeedType;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.restDocs.AbstractRestDocsTests;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.likes.CreateLikesRequestDto;
import com.kss.stockDiscussion.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.follow.StartFollowResponseDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.GetMyNewsFeedDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static com.kss.stockDiscussion.common.ResponseCode.VALIDATION_FAIL;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {NewsFeedController.class})
class NewsFeedControllerTest extends AbstractRestDocsTests {

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

        Page<GetMyNewsFeedDto> mockedPage = createMockedPage();

        BDDMockito.doReturn(GetMyNewsFeedResponseDto.success(mockedPage))
                .when(newsFeedService)
                .getMyNewsFeeds(any(GetMyNewsFeedRequestDto.class));

        String NEWS_FEED_PAGE_PATH = "newsFeedPage";
        // when
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/newsFeed/get-myNewsFeed")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(SUCCESS))
                .andDo(document("get-my-newsFeed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("page").type(JsonFieldType.NUMBER)
                                        .description("포스터 아이디"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("라이크 타입")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description(ResponseCode.SUCCESS),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description(SUCCESS),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".content[]").description("뉴스피드"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".content[].userId").description("유저 아이디"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".content[].userName").description("유저 이름"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".content[].relatedUserId").description("관련 유저 아이디"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".content[].relatedUserName").description("관련 유저 이름"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".content[].relatedPosterId").description("관련 포스터 아이디"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".content[].relatedPosterName").description("관련 포스터 이름"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".content[].newsFeedType").description("뉴스피드 타입"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".content[].message").description("뉴스피드 메시지"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".content[].activityUserId").description("활동한 유저의 아이디"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".content[].activityUserName").description("활동한 유저의 이름"),
                                // ... continue with other fields in GetMyNewsFeedDto
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".pageable").description("Pageable 정보"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".totalPages").description("Total number of pages"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".totalElements").description("Total number of elements"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".last").description("Is this the last page?"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".size").description("Number of elements in the current page"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".number").description("Current page number"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".sort").description("Sorting information"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".numberOfElements").description("Number of elements in the current page"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".first").description("Is this the first page?"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".empty").description("Is the page empty?"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".sort.empty").description("Is the sort empty?"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".sort.sorted").description("Is the sort sorted?"),
                                fieldWithPath(NEWS_FEED_PAGE_PATH + ".sort.unsorted").description("Is the sort unsorted")
                        )
                ));
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
    public static List<GetMyNewsFeedDto> createMockedDtoList() {
        GetMyNewsFeedDto dto1 = GetMyNewsFeedDto.builder()
                .userId(1L)
                .userName("User")
                .newsFeedType(NewsFeedType.FOLLOWING_POST)
                .activityUserId(2L)
                .activityUserName("ActivityUser")
                .relatedUserId(3L)
                .relatedUserName("RelatedUser")
                .relatedPosterId(1L)
                .relatedPosterName("Poster")
                .build();

        GetMyNewsFeedDto dto2 = GetMyNewsFeedDto.builder()
                .userId(2L)
                .userName("User2")
                .newsFeedType(NewsFeedType.FOLLOWING_REPLY)
                .activityUserId(3L)
                .activityUserName("ActivityUser2")
                .relatedUserId(1L)
                .relatedUserName("RelatedUser2")
                .relatedPosterId(4L)
                .relatedPosterName("Poster2")
                .build();

        // Add more GetMyNewsFeedDto objects as needed

        return Arrays.asList(dto1, dto2);
    }

    private Page<GetMyNewsFeedDto> createMockedPage() {
        // Create a list of GetMyNewsFeedDto using the utility method
        List<GetMyNewsFeedDto> mockedDtoList = createMockedDtoList();

        // Create a Page<GetMyNewsFeedDto> using PageImpl
        return new PageImpl<>(mockedDtoList);
    }
}