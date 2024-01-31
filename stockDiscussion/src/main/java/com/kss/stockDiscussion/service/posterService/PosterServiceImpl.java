package com.kss.stockDiscussion.service.posterService;

import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.posterRepository.PosterJpaRepository;
import com.kss.stockDiscussion.repository.userRepository.UserJpaRepository;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.request.poster.CreatePosterRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.poster.CreatePosterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PosterServiceImpl implements PosterService {

    private final PosterJpaRepository posterJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final NewsFeedService newsFeedService;
    @Override
    @Transactional
    public ResponseEntity<? super CreatePosterResponseDto> createPoster(CreatePosterRequestDto dto) {

        try {
            Long userId = dto.getUserId();
            User user = userJpaRepository.findById(userId).get();
            Poster poster = Poster.builder().title(dto.getTitle())
                    .contents(dto.getContents())
                    .owner(user)
                    .build();

            posterJpaRepository.save(poster);

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
