package com.kss.stockDiscussion.service.newsFeedService;

import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.followRepository.FollowRepository;
import com.kss.stockDiscussion.repository.newsFeedRepository.NewsFeedRepository;
import com.kss.stockDiscussion.repository.posterRepository.PosterRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.request.newsFeed.GetFollowingNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.kss.stockDiscussion.domain.newsFeed.ActivityType.*;
import static java.util.Arrays.asList;


@Service
@RequiredArgsConstructor
public class NewsFeedServiceImpl implements NewsFeedService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final PosterRepository posterRepository;
    private final NewsFeedRepository newsFeedRepository;
    private final NewsFeedMapper newsFeedMapper;

    @Override
    public ResponseEntity<? super CreateNewsFeedResponseDto> createNewsFeed(@Valid CreateNewsFeedRequestDto dto) {
        try {
            ActivityType activityType = dto.getActivityType();
            //팔로우 활동이 아닌경우 poster Id 필수
            Long posterId = dto.getPosterId();
            if (activityType != FOLLOW && posterId == null) return ResponseDto.validationFail();
            //글작성 활동이 아닌경우 activityFollowing Id 필수
            Long relatedUserId = dto.getRelatedUserId();
            if (activityType != POST && relatedUserId == null) return ResponseDto.validationFail();

            Poster poster = posterRepository.findById(posterId).get();
            Long activityUserId = dto.getActivityUserId();
            User activityUser = userRepository.findById(activityUserId).get();
            User relatedUser = userRepository.findById(relatedUserId).get();
            NewsFeed.NewsFeedBuilder newsFeedBuilder = NewsFeed.builder()
                    .activityUser(activityUser)
                    .activityType(activityType)
                    .relatedUser(relatedUser);

            if (activityType != FOLLOW) newsFeedBuilder.relatedPoster(poster);
            NewsFeed newsFeed = newsFeedBuilder.build();
            newsFeedRepository.save(newsFeed);
        } catch (Exception exception) {
            exception.printStackTrace();
            ResponseDto.databaseError();
        }
        return CreateNewsFeedResponseDto.success();
}

    @Override
    public ResponseEntity<? super GetMyNewsFeedResponseDto> getMyNewsFeeds(GetMyNewsFeedRequestDto dto) {
        Page<GetMyNewsFeedDto> myNewsFeedPage;
        try {
            Long userId = dto.getUserId();
            int page = dto.getPage();
            int size = dto.getSize();
            Pageable pageable = PageRequest.of(page, size);
            Page<NewsFeed> byRelatedUserId = newsFeedRepository.findByRelatedUserId(userId,pageable);
            myNewsFeedPage = byRelatedUserId.map(newsFeedMapper::toGetMyNewsFeedDto);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetMyNewsFeedResponseDto.success(myNewsFeedPage);
    }

    @Override
    public ResponseEntity<? super GetFollowingNewsFeedResponseDto> getFollowingNewsFeeds(GetFollowingNewsFeedRequestDto dto) {
        Page<GetFollowingNewsFeedDto> followingNewsFeedPage;
        try {
            Long userId = dto.getUserId();
            int page = dto.getPage();
            int size = dto.getSize();
            Pageable pageable = PageRequest.of(page, size);
            List<Follow> followsByFollower = followRepository.findByFollowerId(userId);
            List<User> followings = followsByFollower.stream().map(Follow::getFollowing).toList();
            Page<NewsFeed> newsFeedPage = newsFeedRepository.findByActivityUserIn(followings, pageable);
            followingNewsFeedPage = newsFeedPage.map(newsFeedMapper::toGetFollowingNewsFeedDto);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetFollowingNewsFeedResponseDto.success(followingNewsFeedPage);
    }

    private List<User> findFollowersByFollowingId(Long followingId) {
        List<Follow> followList = followRepository.findByFollowingId(followingId);
        List<User> followerList = followList.stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());
        return followerList;
    }
}
