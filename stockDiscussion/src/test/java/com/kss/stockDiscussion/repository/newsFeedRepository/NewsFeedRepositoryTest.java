package com.kss.stockDiscussion.repository.newsFeedRepository;

import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.posterRepository.PosterRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.List;

import static com.kss.stockDiscussion.domain.newsFeed.ActivityType.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NewsFeedRepositoryTest {

    @Autowired
    private NewsFeedRepository newsFeedRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PosterRepository posterRepository;

    @DisplayName("연관된 유저 아이디로 뉴스피드 찾기")
    @Test
    public void findByRelatedUserId() throws Exception {
        //given
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        User relatedUser = User.builder().name("relatedUser").build();
        User user1 = User.builder().name("user1").build();
        User user2 = User.builder().name("user2").build();
        User user3 = User.builder().name("user3").build();
        userRepository.saveAll(asList(relatedUser,user1,user2,user3));
        Poster poster = Poster.builder().owner(relatedUser)
                .title("poster").build();

        posterRepository.save(poster);
        // 유저1 이 관련유저의 글에 댓글을 담
        // 유저2 이 관련유저의 글에 좋아요를 누름
        // 유저3 이 관련유저를 팔로우 함
        NewsFeed replyNewsFeed = NewsFeed.builder().relatedPoster(poster)
                .activityUser(user1)
                .relatedUser(relatedUser)
                .activityType(REPLY).build();
        NewsFeed likeNewsFeed = NewsFeed.builder().relatedPoster(poster)
                .activityUser(user2)
                .relatedUser(relatedUser)
                .activityType(LIKE).build();
        NewsFeed followNewsFeed = NewsFeed.builder()
                .activityUser(user3)
                .relatedUser(relatedUser)
                .activityType(FOLLOW).build();

        newsFeedRepository.saveAll(asList(replyNewsFeed,likeNewsFeed,followNewsFeed));


        //when
        Page<NewsFeed> byFollowingId = newsFeedRepository.findByRelatedUserId(relatedUser.getId(),pageable);

        //then
        //시간 순서대로 paging 되기 때문에 먼저 저장한 reply, like 가 첫번째 페이지에 나와야 한다
        //총 개수는 3개
        Assertions.assertThat(byFollowingId.getContent()).containsExactlyInAnyOrder(replyNewsFeed, likeNewsFeed);
        Assertions.assertThat(byFollowingId.getTotalElements()).isEqualTo(3);
    }

    @DisplayName("활동 유저 리스트로 모든 NewsFeed 를 가져옴")
    @Test
    public void findByActivityUserIn() throws Exception {
        //given
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);
        User createPosterUser = User.builder().name("createPosterUser").build();
        User user1 = User.builder().name("user1").build();
        User user2 = User.builder().name("user2").build();
        User user3 = User.builder().name("user3").build();
        List<User> userList = asList(createPosterUser, user1, user2, user3);
        userRepository.saveAll(userList);
        Poster poster = Poster.builder().owner(createPosterUser)
                .title("poster").build();

        posterRepository.save(poster);
        // 유저1 이 관련유저의 글에 댓글을 담
        // 유저2 이 관련유저의 글에 좋아요를 누름
        // 유저3 이 관련유저를 팔로우 함
        NewsFeed postNewsFeed = NewsFeed.builder().relatedPoster(poster)
                .activityUser(createPosterUser)
                .activityType(POST).build();
        NewsFeed replyNewsFeed = NewsFeed.builder().relatedPoster(poster)
                .activityUser(user1)
                .relatedUser(createPosterUser)
                .activityType(REPLY).build();
        NewsFeed likeNewsFeed = NewsFeed.builder().relatedPoster(poster)
                .activityUser(user2)
                .relatedUser(createPosterUser)
                .activityType(LIKE).build();
        NewsFeed followNewsFeed = NewsFeed.builder()
                .activityUser(user3)
                .relatedUser(user1)
                .activityType(FOLLOW).build();

        newsFeedRepository.saveAll(asList(postNewsFeed,replyNewsFeed,likeNewsFeed,followNewsFeed));

        //when
        Page<NewsFeed> byActivityUserIn = newsFeedRepository.findByActivityUserIn(userList, pageable);

        List<NewsFeed> contents = byActivityUserIn.getContent();
        long totalElements = byActivityUserIn.getTotalElements();
        //then
        Assertions.assertThat(contents).containsExactlyInAnyOrder(postNewsFeed, replyNewsFeed);
        Assertions.assertThat(totalElements).isEqualTo(4);
    }


}