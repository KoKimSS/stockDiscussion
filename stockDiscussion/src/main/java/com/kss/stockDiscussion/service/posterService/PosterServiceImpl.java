package com.kss.stockDiscussion.service.posterService;

import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
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

import static com.kss.stockDiscussion.config.jwt.JwtUtil.findUserFromAuth;

@Service
@RequiredArgsConstructor
public class PosterServiceImpl implements PosterService {

    private final PosterRepository posterRepository;
    private final UserRepository userRepository;
    private final NewsFeedService newsFeedService;

    @Override
    public ResponseEntity<? super CreatePosterResponseDto> createPoster(CreatePosterRequestDto dto) {

        try {
            Long requestUserId = dto.getUserId();
            if (findUserFromAuth().getId() != requestUserId) {
                return CreatePosterResponseDto.certificationFail();
            }
            User owner = userRepository.findById(requestUserId).get();
            Poster newPoster = Poster.builder().title(dto.getTitle())
                    .contents(dto.getContents())
                    .owner(owner)
                    .build();

            Poster savedPoster = posterRepository.save(newPoster);

            CreateNewsFeedRequestDto createNewsFeedRequestDto = CreateNewsFeedRequestDto.builder()
                    .posterId(savedPoster.getId())
                    .activityType(ActivityType.POST)
                    .activityUserId(requestUserId)
                    .build();
            newsFeedService.createNewsFeed(createNewsFeedRequestDto);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CreatePosterResponseDto.success();
    }
}
