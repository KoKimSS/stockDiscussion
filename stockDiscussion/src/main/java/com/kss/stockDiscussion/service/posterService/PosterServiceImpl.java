package com.kss.stockDiscussion.service.posterService;

import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.followRepository.FollowRepository;
import com.kss.stockDiscussion.repository.newsFeedRepository.NewsFeedRepository;
import com.kss.stockDiscussion.repository.posterRepository.PosterRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.request.poster.CreatePosterRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.poster.CreatePosterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.kss.stockDiscussion.config.jwt.JwtUtil.findUserFromAuth;
import static com.kss.stockDiscussion.domain.newsFeed.NewsFeedType.*;

@Service
@RequiredArgsConstructor
public class PosterServiceImpl implements PosterService {

    private final PosterRepository posterRepository;
    private final UserRepository userRepository;
    private final NewsFeedService newsFeedService;
    @Override
    public ResponseEntity<? super CreatePosterResponseDto> createPoster(CreatePosterRequestDto dto) {

        try {
            Long userId = dto.getUserId();
            if (findUserFromAuth().getId() != userId) {
                return CreatePosterResponseDto.certificationFail();
            }
            User user = userRepository.findById(userId).get();
            Poster poster = Poster.builder().title(dto.getTitle())
                    .contents(dto.getContents())
                    .owner(user)
                    .build();

            posterRepository.save(poster);

            //나를 팔로우 하는 사람들의 뉴스피드 업데이트
            //뉴스피드 생성 서비스 호출 !
            CreateNewsFeedRequestDto createNewsFeedRequestDto = CreateNewsFeedRequestDto.builder()
                    .user(user)
                    .activityType(ActivityType.POST)
                    .relatedPoster(poster)
                    .relatedUser(poster.getOwner())
                    .build();

            newsFeedService.createNewsFeed(createNewsFeedRequestDto);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CreatePosterResponseDto.success();
    }
}
