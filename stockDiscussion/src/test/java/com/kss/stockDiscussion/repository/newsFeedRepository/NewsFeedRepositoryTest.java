package com.kss.stockDiscussion.repository.newsFeedRepository;

import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeedType;
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

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NewsFeedRepositoryTest {

    @Autowired
    NewsFeedRepository newsFeedRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PosterRepository posterRepository;

    //    MY_RELY("내 글에 리플을 담"),
    //    MY_LIKE("내 글에 좋아요를 누름"),
    //    MY_FOLLOW("나를 팔로우 함"),
    //    FOLLOWING_REPLY("팔로잉이 어떤 글에 리플을 담"),
    //    FOLLOWING_LIKE("팔로잉이 어떤 글에 좋아요를 함"),
    //    FOLLOWING_FOLLOW("팔로잉이 누군가를 팔로우 함"),
    //    FOLLOWING_POST("팔로잉이 글을 적음");
    @DisplayName("유저 아이디로 뉴스피드 페이지를 찾는다.")
    @Test
    public void findByUserId() throws Exception {
        //given
        User user = User.builder().name("유저").build();
        User activityUser1 = User.builder().name("활동유저1").build();
        User activityUser2 = User.builder().name("활동유저2").build();
        User activityUser3 = User.builder().name("활동유저3").build();
        userRepository.saveAll(asList(user, activityUser1, activityUser2, activityUser3));

        Poster userPoster = Poster.builder().owner(user)
                .title("userPoster").build();
        Poster activityUserPoster = Poster.builder().owner(activityUser1)
                .title("activityUserPoster").build();
        posterRepository.saveAll(asList(userPoster, activityUserPoster));

        NewsFeed newsFeed_MY_REPLY = NewsFeed.builder().user(user)
                .activityUser(activityUser1)
                .newsFeedType(NewsFeedType.MY_RELY)
                .relatedPoster(userPoster).build();
        NewsFeed newsFeed_MY_LIKE = NewsFeed.builder().user(user)
                .activityUser(activityUser1)
                .newsFeedType(NewsFeedType.MY_LIKE)
                .relatedPoster(userPoster).build();
        NewsFeed newsFeed_MY_FOLLOW = NewsFeed.builder().user(user)
                .activityUser(activityUser1)
                .newsFeedType(NewsFeedType.MY_FOLLOW).build();
        NewsFeed newsFeed_FOLLOWING_REPLY = NewsFeed.builder().user(user)
                .activityUser(activityUser1)
                .newsFeedType(NewsFeedType.FOLLOWING_REPLY)
                .relatedUser(activityUserPoster.getOwner())
                .relatedPoster(activityUserPoster).build();
        //when

        newsFeedRepository.saveAll(asList(newsFeed_MY_REPLY, newsFeed_MY_LIKE, newsFeed_MY_FOLLOW, newsFeed_FOLLOWING_REPLY));
        int size = 2;
        Pageable pageable1 = PageRequest.of(0, size);
        Pageable pageable2 = PageRequest.of(1, size);
        Page<NewsFeed> byUserId1 = newsFeedRepository.findByUserId(user.getId(), pageable1);
        Page<NewsFeed> byUserId2 = newsFeedRepository.findByUserId(user.getId(), pageable2);

        //then
        Assertions.assertThat(byUserId1.getTotalElements()).isEqualTo(4);
        Assertions.assertThat(byUserId1.getContent()).containsExactly(newsFeed_MY_REPLY, newsFeed_MY_LIKE);
        Assertions.assertThat(byUserId2.getTotalElements()).isEqualTo(4);
        Assertions.assertThat(byUserId2.getContent()).containsExactly(newsFeed_MY_FOLLOW, newsFeed_FOLLOWING_REPLY);


    }

}