package com.kss.stockDiscussion.service.newsFeedService;

import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeedType;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.followRepository.FollowJpaRepository;
import com.kss.stockDiscussion.repository.newsFeedRepository.NewsFeedJpaRepository;
import com.kss.stockDiscussion.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.CreateNewsFeedResponseDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.GetMyNewsFeedDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.GetMyNewsFeedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.kss.stockDiscussion.domain.newsFeed.NewsFeedType.*;
import static com.kss.stockDiscussion.domain.newsFeed.NewsFeedType.FOLLOWING_LIKE;
import static com.kss.stockDiscussion.domain.newsFeed.NewsFeedType.MY_LIKE;


@Service
@RequiredArgsConstructor
@Transactional
public class NewsFeedServiceImpl implements NewsFeedService {

    private final NewsFeedMapper newsFeedMapper;
    private final NewsFeedJpaRepository newsFeedJpaRepository;
    private final FollowJpaRepository followJpaRepository;

    @Override
    public ResponseEntity<? super GetMyNewsFeedResponseDto> getMyNewsFeeds(GetMyNewsFeedRequestDto dto) {
        Page<GetMyNewsFeedDto> newsFeedDtoPage;
        try {
            Long userId = dto.getUserId();
            int page = dto.getPage();
            int size = dto.getSize();
            Pageable pageable = PageRequest.of(page, size);
            Page<NewsFeed> newsFeedPage = newsFeedJpaRepository.findByUserId(userId, pageable);
            long totalElements = newsFeedPage.getTotalElements();
            int totalPages = newsFeedPage.getTotalPages();
            System.out.println("totalElements = " + totalElements);
            System.out.println("totalPages = " + totalPages);
            System.out.println("뉴스피드 컨텐츠"+newsFeedPage.getContent());
            newsFeedDtoPage = newsFeedPage.map(newsFeed->newsFeedMapper.toGetMyNewsFeedDto(newsFeed));
            System.out.println("뉴스피드가져옴");
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetMyNewsFeedResponseDto.success(newsFeedDtoPage);
    }

    @Override
    public ResponseEntity<? super CreateNewsFeedResponseDto> createNewsFeed(
            CreateNewsFeedRequestDto dto) {
        try {
            //나를 팔로우 하는 사람들의 뉴스피드 리스트 생성
            User user = dto.getUser();
            ActivityType activityType = dto.getActivityType();
            NewsFeedType followersNewsFeedType = followersTypeBy(activityType);
            Poster poster = dto.getRelatedPoster();
            User relatedUser = dto.getRelatedUser();
            if (isValidRequestDto(activityType, relatedUser,poster)) {
                return CreateNewsFeedResponseDto.validationFail();
            }
            List<Follow> followList = followJpaRepository.findByFollowingId(user.getId());
            List<NewsFeed> newsFeedList = createFollowersNewsFeedList(user, followersNewsFeedType, followList, poster, relatedUser);

            //내가 한 활동의 관련된 사람 뉴스피드 추가 ( POST 인 경우 관련유저 없음)
            if (activityType != ActivityType.POST ) {
                NewsFeed newsFeed = NewsFeed.builder()
                        .newsFeedType(ownerTypeBy(activityType))
                        .user(relatedUser)
                        .activityUser(user)
                        .relatedPoster(poster)
                        .build();
                newsFeedList.add(newsFeed);
            }
            newsFeedJpaRepository.saveAll(newsFeedList);
        } catch (Exception exception) {
            exception.printStackTrace();
            CreateNewsFeedResponseDto.databaseError();
        }
        return CreateNewsFeedResponseDto.success();
    }

    private static boolean isValidRequestDto(ActivityType activityType, User relatedUser, Poster poster) {
        return (activityType != ActivityType.POST && relatedUser == null)
                || (activityType != ActivityType.FOLLOW && poster == null);
    }

    private static List<NewsFeed> createFollowersNewsFeedList(User user, NewsFeedType newsFeedType, List<Follow> followList, Poster poster, User relatedUser) {
        return followList.stream()
                .map(follow -> {
                    User follower = follow.getFollower();
                    return NewsFeed.builder()
                            .newsFeedType(newsFeedType)
                            .user(follower)
                            .activityUser(user)
                            .relatedPoster(poster)
                            .relatedUser(relatedUser)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private NewsFeedType followersTypeBy(ActivityType activityType) {
        if (activityType == ActivityType.POST) {
            return FOLLOWING_POST;
        }
        if (activityType == ActivityType.FOLLOW) {
            return FOLLOWING_FOLLOW;
        }
        if (activityType == ActivityType.REPLY) {
            return FOLLOWING_REPLY;
        }
        if (activityType == ActivityType.LIKE) {
            return FOLLOWING_LIKE;
        }
        throw new IllegalArgumentException("Activity 타입이 NewsFeedType 과 매칭이 안됩니다");
    }

    private NewsFeedType ownerTypeBy(ActivityType activityType) {
        if (activityType == ActivityType.FOLLOW) {
            return MY_FOLLOW;
        }
        if (activityType == ActivityType.REPLY) {
            return MY_REPLY;
        }
        if (activityType == ActivityType.LIKE) {
            return MY_LIKE;
        }
        throw new IllegalArgumentException("Activity 타입이 NewsFeedType 과 매칭이 안됩니다");
    }
}
