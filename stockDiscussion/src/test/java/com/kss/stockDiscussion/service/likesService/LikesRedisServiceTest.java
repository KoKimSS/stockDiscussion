package com.kss.stockDiscussion.service.likesService;

import com.kss.stockDiscussion.domain.like.LikeType;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.reply.Reply;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.likeRepository.LikesJpaRepository;
import com.kss.stockDiscussion.repository.posterRepository.PosterJpaRepository;
import com.kss.stockDiscussion.repository.replyRepository.ReplyJpaRepository;
import com.kss.stockDiscussion.repository.userRepository.UserJpaRepository;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.likes.CreateLikesRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.kss.stockDiscussion.service.likesService.LikesServiceTest.getCreateLikesRequestDtoBuilder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LikesRedisServiceTest {
    @Autowired
    private LikesService likesService;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PosterJpaRepository posterJpaRepository;
    @Autowired
    private ReplyJpaRepository replyJpaRepository;
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private EntityManager em;

    @Test
    void testCreateLikes() {
        //given
        User user = User.builder().name("User").build();
        userJpaRepository.save(user);
        Poster poster = Poster.builder().title("title").build();
        posterJpaRepository.save(poster);
        Reply reply = Reply.builder().contents("난댓글").poster(poster).build();
        replyJpaRepository.save(reply);
        Long userId = user.getId();
        Long posterId = poster.getId();
        Long replyId = reply.getId();

        CreateLikesRequestDto requestDto1 = getCreateLikesRequestDtoBuilder(LikeType.POSTER, userId, null, posterId);
        CreateLikesRequestDto requestDto2 = getCreateLikesRequestDtoBuilder(LikeType.POSTER, userId, null, posterId);
        CreateLikesRequestDto requestDto3 = getCreateLikesRequestDtoBuilder(LikeType.REPLY, userId, replyId, posterId);
        System.out.println("poster.getLikeCount() = " + poster.getLikeCount());
        System.out.println("reply.getLikeCount() = " + reply.getLikeCount());
        likesService.createLikes(requestDto1);
        likesService.createLikes(requestDto2);
        likesService.createLikes(requestDto3);

        CountDownLatch latch = new CountDownLatch(1);
        Trigger trigger = new PeriodicTrigger(18000L); // 18 seconds
        taskScheduler.schedule(() -> {
            // Call your scheduled method here
            likesService.updateLikesCountByRedis();
            latch.countDown();
        }, trigger);

        // Wait for the scheduled task to complete
        try {
            latch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Verify repository calls
        System.out.println(posterId);
        System.out.println(replyId);
        System.out.println(getPosterLikeCount(posterId));
        System.out.println(getReplyLikeCount(replyId));
    }

    public int getPosterLikeCount(Long posterId) {
        return posterJpaRepository.findById(posterId).get().getLikeCount();
    }

    public int getReplyLikeCount(Long replyId) {
        return replyJpaRepository.findById(replyId).get().getLikeCount();
    }
}
