package com.kss.stockDiscussion.service.followService;

import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.followRepository.FollowJpaRepository;
import com.kss.stockDiscussion.repository.userRepository.UserJpaRepository;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.follow.StartFollowRequestDto;
import com.kss.stockDiscussion.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.follow.StartFollowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowJpaRepository followJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final NewsFeedService newsFeedService;


    @Override
    public ResponseEntity<? super StartFollowResponseDto> follow(StartFollowRequestDto dto) {
        Long followingId = dto.getFollowingId();
        Long followerId = dto.getFollowerId();
        try {
            User follower = userJpaRepository.findById(followerId).get();
            User following = userJpaRepository.findById(followingId).get();

            Follow follow = Follow.builder().follower(follower).following(following).build();
            followJpaRepository.save(follow);

            //뉴스피드 생성 서비스 호출 !
            CreateNewsFeedRequestDto createNewsFeedRequestDto = CreateNewsFeedRequestDto.builder()
                    .user(follower)
                    .activityType(ActivityType.FOLLOW)
                    .relatedUser(following).build();
            newsFeedService.createNewsFeed(createNewsFeedRequestDto);

        } catch (Exception exception) {
            exception.printStackTrace();
            StartFollowResponseDto.databaseError();
        }

        return StartFollowResponseDto.success();
    }


}
