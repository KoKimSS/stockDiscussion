package com.kss.stockDiscussion.service.newsFeedService;

import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeedType;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.reply.Reply;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.followRepository.FollowRepository;
import com.kss.stockDiscussion.repository.newsFeedRepository.NewsFeedRepository;
import com.kss.stockDiscussion.repository.posterRepository.PosterRepository;
import com.kss.stockDiscussion.repository.replyRepository.ReplyRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;

import java.util.List;

import static com.kss.stockDiscussion.domain.newsFeed.NewsFeedType.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@SpringBootTest
@Transactional
class NewsFeedServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PosterRepository posterRepository;
    @Autowired
    NewsFeedRepository repository;
    @Autowired
    NewsFeedService newsFeedService;
    @Autowired
    NewsFeedRepository newsFeedRepository;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    ReplyRepository replyRepository;

    @BeforeEach
    void setUp() {
        User user = createUser("user", "user@email");
        User follower1 = createUser("follower1", "follower1@email");
        User follower2 = createUser("follower2", "follower2@email");
        User posterOwner = createUser("posterOwner", "posterOwner@email");
        userRepository.saveAll(List.of(user, follower1,follower2,posterOwner));
        Follow follow1 = getFollow(follower1, user);
        Follow follow2 = getFollow(follower2, user);
        followRepository.saveAll(List.of(follow1, follow2));
    }

    @DisplayName("나의 뉴스피드 가져오기")
    @Test
    public void getMyNewsFeeds() throws Exception {
        //given
        User user = userRepository.findByEmail("user@email").get();
        String name = user.getName();
        User follower = userRepository.findByEmail("follower1@email").get();
        NewsFeed newsFeed1 = getNewsFeed(follower, FOLLOWING_POST ,user);
        NewsFeed newsFeed2 = getNewsFeed(follower, FOLLOWING_LIKE ,user);
        NewsFeed newsFeed3 = getNewsFeed(follower, FOLLOWING_REPLY ,user);
        NewsFeed newsFeed4 = getNewsFeed(follower, FOLLOWING_FOLLOW ,user);

        newsFeedRepository.saveAll(List.of(newsFeed1, newsFeed2, newsFeed3, newsFeed4));

        int pageSize = 2;
        GetMyNewsFeedRequestDto requestDto0 = getRequestDto(follower, 0, pageSize);
        GetMyNewsFeedRequestDto requestDto1 = getRequestDto(follower, 1, pageSize);

        //when
        GetMyNewsFeedResponseDto page0Response = (GetMyNewsFeedResponseDto) newsFeedService.getMyNewsFeeds(requestDto0).getBody();
        GetMyNewsFeedResponseDto page1Response = (GetMyNewsFeedResponseDto) newsFeedService.getMyNewsFeeds(requestDto1).getBody();

        //then
        Assertions.assertThat(page0Response)
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        Assertions.assertThat(page1Response)
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        Assertions.assertThat(page0Response.getNewsFeedPage().getContent())
                .extracting("newsFeedType", "activityUserName")
                .containsExactly(
                        tuple(FOLLOWING_POST, name), tuple(FOLLOWING_LIKE, name)
                );
        Assertions.assertThat(page1Response.getNewsFeedPage().getContent())
                .extracting("newsFeedType", "activityUserName")
                .containsExactly(
                        tuple(FOLLOWING_REPLY, name), tuple(FOLLOWING_FOLLOW, name)
                );

    }

    private static GetMyNewsFeedRequestDto getRequestDto(User follower, int page, int size) {
        return GetMyNewsFeedRequestDto.builder()
                .page(page)
                .size(size)
                .userId(follower.getId()).build();
    }

    private static NewsFeed getNewsFeed(User user,NewsFeedType newsFeedType ,User activityUser) {
        return NewsFeed.builder().user(user)
                .newsFeedType(newsFeedType)
                .activityUser(activityUser).build();
    }

    @DisplayName("게시글을 작성시 유저의 팔로워들의 뉴스피드 생성")
    @Test
    public void createNewsFeedWithPOST() throws Exception {
        //given
        ActivityType activityType = ActivityType.POST;
        User user = userRepository.findByEmail("user@email").get();
        User follower1 = userRepository.findByEmail("follower1@email").get();
        User follower2 = userRepository.findByEmail("follower2@email").get();
        Poster poster = getPoster(user, "userPoster");
        posterRepository.save(poster);

        CreateNewsFeedRequestDto createNewsFeedRequestDto = getCreateNewsFeedRequestDto(user, activityType, null, poster);
        //when
        newsFeedService.createNewsFeed(createNewsFeedRequestDto);
        List<NewsFeed> follower1NewsFeed = newsFeedRepository.findAllByUserId(follower1.getId());
        List<NewsFeed> follower2NewsFeed = newsFeedRepository.findAllByUserId(follower2.getId());
        List<NewsFeed> all = newsFeedRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(2);
        assertThat(follower1NewsFeed)
                .extracting("newsFeedType", "activityUser", "relatedPoster")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_POST, user, poster));
        assertThat(follower2NewsFeed)
                .extracting("newsFeedType", "activityUser", "relatedPoster")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_POST, user, poster));
    }

    @DisplayName("댓글 작성시 팔로워들과 포스터 주인의 뉴스피드 생성")
    @Test
    public void createNewsFeedWithREPLY() throws Exception {
        //given
        ActivityType activityType = ActivityType.REPLY;
        User user = userRepository.findByEmail("user@email").get();
        User follower1 = userRepository.findByEmail("follower1@email").get();
        User follower2 = userRepository.findByEmail("follower2@email").get();
        User posterOwner = userRepository.findByEmail("posterOwner@email").get();
        Poster poster = getPoster(posterOwner, "Poster");
        posterRepository.save(poster);
        Reply reply = getReply(user, poster,"댓글");
        replyRepository.save(reply);

        CreateNewsFeedRequestDto createNewsFeedRequestDto = getCreateNewsFeedRequestDto(user, activityType, poster.getOwner(), poster);
        //when
        newsFeedService.createNewsFeed(createNewsFeedRequestDto);
        List<NewsFeed> follower1NewsFeed = newsFeedRepository.findAllByUserId(follower1.getId());
        List<NewsFeed> follower2NewsFeed = newsFeedRepository.findAllByUserId(follower2.getId());
        List<NewsFeed> posterOwnerNewsFeed = newsFeedRepository.findAllByUserId(posterOwner.getId());
        List<NewsFeed> all = newsFeedRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(3);
        assertThat(follower1NewsFeed)
                .extracting("newsFeedType", "activityUser", "relatedPoster")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_REPLY, user, poster));
        assertThat(follower2NewsFeed)
                .extracting("newsFeedType", "activityUser", "relatedPoster")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_REPLY, user, poster));
        assertThat(posterOwnerNewsFeed)
                .extracting("newsFeedType", "activityUser", "relatedPoster")
                .containsExactlyInAnyOrder(tuple(MY_REPLY, user, poster));
    }

    @DisplayName("유저가 팔로우시 그 팔로잉과 유저의 팔로워들에게 뉴스피드 생성 // 유저가 포스터 주인을 팔로우 했다고 가정")
    @Test
    public void createNewsFeedWithFOLLOW() throws Exception {
        //given
        ActivityType activityType = ActivityType.FOLLOW;
        User user = userRepository.findByEmail("user@email").get();
        User follower1 = userRepository.findByEmail("follower1@email").get();
        User follower2 = userRepository.findByEmail("follower2@email").get();
        User posterOwner = userRepository.findByEmail("posterOwner@email").get();
        Follow follow = getFollow(user, posterOwner);
        followRepository.save(follow);

        CreateNewsFeedRequestDto createNewsFeedRequestDto = getCreateNewsFeedRequestDto(user, activityType, posterOwner , null);
        //when
        newsFeedService.createNewsFeed(createNewsFeedRequestDto);
        List<NewsFeed> follower1NewsFeed = newsFeedRepository.findAllByUserId(follower1.getId());
        List<NewsFeed> follower2NewsFeed = newsFeedRepository.findAllByUserId(follower2.getId());
        List<NewsFeed> posterOwnerNewsFeed = newsFeedRepository.findAllByUserId(posterOwner.getId());
        List<NewsFeed> all = newsFeedRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(3);
        assertThat(follower1NewsFeed)
                .extracting("newsFeedType", "activityUser")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_FOLLOW, user));
        assertThat(follower2NewsFeed)
                .extracting("newsFeedType", "activityUser")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_FOLLOW, user));
        assertThat(posterOwnerNewsFeed)
                .extracting("newsFeedType", "activityUser")
                .containsExactlyInAnyOrder(tuple(MY_FOLLOW, user));
    }

    @DisplayName("유저가 좋아요를 누를 시 관련 게시물의 소유자와 팔로워들에게 뉴스피드 생성")
    @Test
    public void createNewsFeedWithLIKE() throws Exception {
        //given
        ActivityType activityType = ActivityType.LIKE;
        User user = userRepository.findByEmail("user@email").get();
        User follower1 = userRepository.findByEmail("follower1@email").get();
        User follower2 = userRepository.findByEmail("follower2@email").get();
        User posterOwner = userRepository.findByEmail("posterOwner@email").get();
        Poster poster = getPoster(posterOwner, "Poster");
        posterRepository.save(poster);

        CreateNewsFeedRequestDto createNewsFeedRequestDto = getCreateNewsFeedRequestDto(user, activityType, posterOwner , poster);

        //when
        newsFeedService.createNewsFeed(createNewsFeedRequestDto);
        List<NewsFeed> follower1NewsFeed = newsFeedRepository.findAllByUserId(follower1.getId());
        List<NewsFeed> follower2NewsFeed = newsFeedRepository.findAllByUserId(follower2.getId());
        List<NewsFeed> posterOwnerNewsFeed = newsFeedRepository.findAllByUserId(posterOwner.getId());
        List<NewsFeed> all = newsFeedRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(3);
        assertThat(follower1NewsFeed)
                .extracting("newsFeedType", "activityUser")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_LIKE, user));
        assertThat(follower2NewsFeed)
                .extracting("newsFeedType", "activityUser")
                .containsExactlyInAnyOrder(tuple(FOLLOWING_LIKE, user));
        assertThat(posterOwnerNewsFeed)
                .extracting("newsFeedType", "activityUser")
                .containsExactlyInAnyOrder(tuple(MY_LIKE, user));

    }
    private static Follow getFollow(User user, User posterOwner) {
        return Follow.builder().follower(user).following(posterOwner).build();
    }

    private static CreateNewsFeedRequestDto getCreateNewsFeedRequestDto(User user, ActivityType activityType , User relatedUser, Poster relatedPoster) {
        return CreateNewsFeedRequestDto.builder()
                .user(user)
                .activityType(activityType)
                .relatedUser(relatedUser)
                .relatedPoster(relatedPoster)
                .build();
    }

    private static User createUser(String name,String email) {
        return User.builder().name(name).email(email).build();
    }

    private static Poster getPoster(User user, String userPoster) {
        return Poster.builder().owner(user)
                .title(userPoster).build();
    }

    private static Reply getReply(User user, Poster poster, String contents) {
        return Reply.builder().user(user).poster(poster).contents(contents).build();
    }

}