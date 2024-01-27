package com.kss.stockDiscussion.repository.newsFeedRepository;

import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeedType;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.posterRepository.PosterJpaRepository;
import com.kss.stockDiscussion.repository.userRepository.UserJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

import static java.util.Arrays.asList;

@SpringBootTest
@Transactional
class NewsFeedJpaRepositoryTest {

    @Autowired
    NewsFeedJpaRepository newsFeedJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PosterJpaRepository posterJpaRepository;

    @DisplayName("유저 아이디로 뉴스피드 페이지를 찾는다.")
    @Test
    public void findByUserId() throws Exception {
        //given
        User user = User.builder().name("유저").build();
        User activityUser1 = getUser("활동 유저1");
        User activityUser2 = getUser("활동 유저2");
        User activityUser3 = getUser("활동 유저3");
        userJpaRepository.saveAll(asList(user, activityUser1, activityUser2, activityUser3));

        Poster userPoster = getPoster(user, "userPoster");
        Poster activityUserPoster = getPoster(activityUser1, "activityUserPoster");
        posterJpaRepository.saveAll(asList(userPoster, activityUserPoster));

        NewsFeed newsFeed_MY_REPLY = getNewsFeed(user, activityUser1, userPoster,NewsFeedType.MY_REPLY,null);
        NewsFeed newsFeed_MY_LIKE = getNewsFeed(user, activityUser1, userPoster,NewsFeedType.MY_LIKE,null);
        NewsFeed newsFeed_MY_FOLLOW = getNewsFeed(user, activityUser1, null, NewsFeedType.MY_FOLLOW,null);
        NewsFeed newsFeed_FOLLOWING_REPLY = getNewsFeed(user, activityUser1, userPoster,NewsFeedType.FOLLOWING_REPLY,userPoster.getOwner());

        int size = 2;
        Pageable pageable0 = PageRequest.of(0, size);
        Pageable pageable1 = PageRequest.of(1, size);

        //when
        newsFeedJpaRepository.saveAll(asList(newsFeed_MY_REPLY, newsFeed_MY_LIKE, newsFeed_MY_FOLLOW, newsFeed_FOLLOWING_REPLY));
        Page<NewsFeed> page1 = newsFeedJpaRepository.findByUserId(user.getId(), pageable0);
        Page<NewsFeed> page2 = newsFeedJpaRepository.findByUserId(user.getId(), pageable1);

        //then
        Assertions.assertThat(page1.getTotalElements()).isEqualTo(4);
        Assertions.assertThat(page1.getContent()).containsExactly(newsFeed_MY_REPLY, newsFeed_MY_LIKE);
        Assertions.assertThat(page2.getTotalElements()).isEqualTo(4);
        Assertions.assertThat(page2.getContent()).containsExactly(newsFeed_MY_FOLLOW, newsFeed_FOLLOWING_REPLY);
    }

    private static NewsFeed getNewsFeed(User user, User activityUser1, Poster userPoster,NewsFeedType newsFeedType,User relatedUser) {
        return NewsFeed.builder().user(user)
                .activityUser(activityUser1)
                .newsFeedType(newsFeedType)
                .relatedPoster(userPoster)
                .relatedUser(relatedUser)
                .build();
    }

    private static Poster getPoster(User user, String userPoster) {
        return Poster.builder().owner(user)
                .title(userPoster).build();
    }

    private static User getUser(String name) {
        return User.builder().name(name).build();
    }

}