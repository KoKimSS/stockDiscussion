package com.kss.stockDiscussion.service.likesService;


import com.kss.stockDiscussion.domain.like.LikeType;
import com.kss.stockDiscussion.domain.like.Likes;
import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.reply.Reply;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.likeRepository.LikesJpaRepository;
import com.kss.stockDiscussion.repository.posterRepository.PosterJpaRepository;
import com.kss.stockDiscussion.repository.replyRepository.ReplyJpaRepository;
import com.kss.stockDiscussion.repository.userRepository.UserJpaRepository;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.likes.CreateLikesRequestDto;
import com.kss.stockDiscussion.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.likes.CreateLikesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Iterator;
import java.util.Set;

import static com.kss.stockDiscussion.domain.like.Likes.*;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService{
    private final LikesJpaRepository likesJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PosterJpaRepository posterJpaRepository;
    private final ReplyJpaRepository replyJpaRepository;
    private final NewsFeedService newsFeedService;
    private final RedisTemplate<String, String> redisTemplate; // Inject RedisTemplate


    @Transactional
    @Override
    public ResponseEntity<? super CreateLikesResponseDto> createLikes(CreateLikesRequestDto dto) {

        try {
            LikeType likeType = dto.getLikeType();
            Long posterId = dto.getPosterId(); //@NotBlank 이다
            Long replyId = dto.getReplyId();
            if (likeType == LikeType.REPLY && replyId == null) return CreateLikesResponseDto.validationFail();

            Long userId = dto.getUserId();
            User user = userJpaRepository.findById(userId).get();
            Poster poster =  posterJpaRepository.findById(posterId).get();

            LikesBuilder likesBuilder = builder().likeType(likeType)
                    .user(user)
                    .poster(poster);
            if(likeType == LikeType.REPLY){
                Reply reply = replyJpaRepository.findById(replyId).get();
                likesBuilder.reply(reply);
                addReplyLikesCntToRedis(replyId);
            }
            if(likeType == LikeType.POSTER){
                addPosterLikesCntToRedis(posterId);
            }
            Likes newLikes = likesBuilder.build();
            likesJpaRepository.save(newLikes);

            //뉴스피드 생성 서비스 호출 !
            CreateNewsFeedRequestDto createNewsFeedRequestDto = CreateNewsFeedRequestDto.builder()
                    .user(user)
                    .activityType(ActivityType.LIKE)
                    .relatedPoster(poster)
                    .relatedUser(poster.getOwner())
                    .build();

            newsFeedService.createNewsFeed(createNewsFeedRequestDto);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CreateLikesResponseDto.success();
    }

    @Transactional
    @Override
    public void addReplyLikesCntToRedis(Long replyId){
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        String key = "replyId::" + replyId;
        String hashkey = "replyLikes";
        System.out.println("reply hash값"+hashOperations.get(key,hashkey));
        if(hashOperations.get(key,hashkey) == null){
            hashOperations.put(key,hashkey,String.valueOf(replyJpaRepository.findById(replyId).get().getLikeCount()));
            hashOperations.increment(key,hashkey,1L);
            System.out.println(hashOperations.get(key,hashkey));
        }else {
            hashOperations.increment(key,hashkey,1L);
            System.out.println(hashOperations.get(key, hashkey));
        }
        System.out.println("Reply 추가");
    }

    @Transactional
    @Override
    public void addPosterLikesCntToRedis(Long posterId){
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        String key = "posterId::" + posterId;
        String hashkey = "posterLikes";
        System.out.println("poster hash값"+hashOperations.get(key,hashkey));
        if(hashOperations.get(key,hashkey) == null){
            int posterLikeCount = posterJpaRepository.findById(posterId).get().getLikeCount();
            System.out.println("저장된 poster "+posterLikeCount);
            hashOperations.put(key,hashkey,String.valueOf(posterLikeCount));
            hashOperations.increment(key,hashkey,1L);
            System.out.println(hashOperations.get(key,hashkey));
        }else {
            hashOperations.increment(key,hashkey,1L);
            System.out.println(hashOperations.get(key, hashkey));
        }
        System.out.println("Poster 추가");
    }

    @Scheduled(fixedDelay = 1000L*18L)
    @Transactional
    @Override
    public void updateLikesCountByRedis(){
        String hashkey = "posterLikes";
        Set<String> Rediskey = redisTemplate.keys("posterId*");
        Iterator<String> it = Rediskey.iterator();
        while (it.hasNext()) {
            String keyPattern = it.next();
            Long posterId = Long.parseLong(keyPattern.split("::")[1]);
            if (redisTemplate.opsForHash().get(keyPattern, hashkey) == null){
                break;
            }
            Integer likesCount = Integer.parseInt((String.valueOf(redisTemplate.opsForHash().get(keyPattern, hashkey))));
            System.out.println("Poster 생성 "+ likesCount);
            System.out.println("posterId = "+posterId);
            posterJpaRepository.addLikeCountFromRedis(posterId, likesCount);
            redisTemplate.opsForHash().delete(keyPattern, hashkey);
        }

        hashkey = "replyLikes";
        Rediskey = redisTemplate.keys("replyId*");
        it = Rediskey.iterator();
        while (it.hasNext()) {
            String keyPattern = it.next();
            Long replyId = Long.parseLong(keyPattern.split("::")[1]);
            if (redisTemplate.opsForHash().get(keyPattern, hashkey) == null){
                break;
            }
            int likesCount = Integer.parseInt((String.valueOf(redisTemplate.opsForHash().get(keyPattern, hashkey))));
            System.out.println("Reply 생성 " + likesCount);
            System.out.println("replyId = "+replyId);
            replyJpaRepository.addLikeCountFromRedis(replyId, likesCount);
            redisTemplate.opsForHash().delete(keyPattern, hashkey);
        }
    }
}
