package com.kss.stockDiscussion.service.replyService;

import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.reply.Reply;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.posterRepository.PosterJpaRepository;
import com.kss.stockDiscussion.repository.replyRepository.ReplyJpaRepository;
import com.kss.stockDiscussion.repository.userRepository.UserJpaRepository;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.request.reply.CreateReplyRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.reply.CreateReplyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyJpaRepository replyJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PosterJpaRepository posterJpaRepository;
    private final NewsFeedService newsFeedService;

    @Override
    public ResponseEntity<? super CreateReplyResponseDto> createReply(CreateReplyRequestDto dto) {
        try {
            Long userId = dto.getUserId();

            Long posterId = dto.getPosterId();
            User user = userJpaRepository.findById(userId).get();
            Poster poster = posterJpaRepository.findById(posterId).get();
            String contents = dto.getContents();

            Reply newReply = Reply.builder().user(user)
                    .poster(poster)
                    .contents(contents).build();
            replyJpaRepository.save(newReply);

            //나를 팔로우 하는 사람들의 뉴스피드 업데이트
            //뉴스피드 생성 서비스 호출 !
            CreateNewsFeedRequestDto createNewsFeedRequestDto = CreateNewsFeedRequestDto.builder()
                    .user(user)
                    .activityType(ActivityType.POST)
                    .relatedPoster(poster)
                    .relatedUser(poster.getOwner())
                    .build();

            newsFeedService.createNewsFeed(createNewsFeedRequestDto);
        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CreateReplyResponseDto.success();
    }
}
