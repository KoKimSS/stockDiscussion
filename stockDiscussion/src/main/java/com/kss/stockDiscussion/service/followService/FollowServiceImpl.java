package com.kss.stockDiscussion.service.followService;

import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeedType;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.followRepository.FollowRepository;
import com.kss.stockDiscussion.repository.newsFeedRepository.NewsFeedRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.follow.StartFollowRequestDto;
import com.kss.stockDiscussion.web.dto.response.follow.StartFollowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.kss.stockDiscussion.domain.newsFeed.NewsFeedType.MY_FOLLOW;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final NewsFeedService newsFeedService;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NewsFeedRepository newsFeedRepository;


    @Override
    public ResponseEntity<? super StartFollowResponseDto> follow(StartFollowRequestDto dto) {
        Long followingId = dto.getFollowingId();
        Long followerId = dto.getFollowerId();
        try {
            User user = JwtUtil.findUserFromAuth();
            if (user.getId() != followerId) {
                return StartFollowResponseDto.certificationFail();
            }

            User follower = userRepository.findById(followerId).get();
            User following = userRepository.findById(followingId).get();

            Follow follow = Follow.builder().follower(follower).following(following).build();
            followRepository.save(follow);

            //나를 팔로우 하는 사람들의 뉴스피드 업데이트
            List<Follow> followerFollowList = followRepository.findByFollowingId(followerId);
            List<NewsFeed> newsFeedList = followerFollowList.stream()
                    .map(followerFollow -> {
                        User newsFeedOwner = followerFollow.getFollower();
                        return NewsFeed.builder()
                                .newsFeedType(NewsFeedType.FOLLOWING_FOLLOW)
                                .user(newsFeedOwner)
                                .activityUser(follower)
                                .relatedUser(following)
                                .build();
                    })
                    .collect(Collectors.toList());
            //내가 팔로우 하는 사람의 뉴스피드 업데이트
            NewsFeed newsFeed = NewsFeed.builder()
                    .newsFeedType(MY_FOLLOW)
                    .user(following)
                    .activityUser(follower)
                    .build();
            newsFeedList.add(newsFeed);
            newsFeedRepository.saveAll(newsFeedList);
        } catch (Exception exception) {
            exception.printStackTrace();
            StartFollowResponseDto.databaseError();
        }


        return StartFollowResponseDto.success();
    }


}
