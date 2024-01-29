package com.kss.stockDiscussion.service.likesService;

import com.kss.stockDiscussion.repository.likeRepository.LikesJpaRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LikeCountUpdater {

    private final LikesJpaRepository likesJpaRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public LikeCountUpdater(LikesJpaRepository likesJpaRepository, RedisTemplate<String, String> redisTemplate) {
        this.likesJpaRepository = likesJpaRepository;
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedDelay = 1000L*18L) // Run every 60 seconds (adjust as needed)
    public void updateDatabaseFromRedis() {
        // Get like counts from Redis and update the database
        // Implement the logic to fetch counts for each poster and update the database
        // ...

        // Clear Redis after updating the database if needed
        // redisTemplate.delete("likes:" + posterId);
    }
}
