package com.kss.stockDiscussion.service.replyService;

import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.reply.Reply;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.followRepository.FollowRepository;
import com.kss.stockDiscussion.repository.newsFeedRepository.NewsFeedRepository;
import com.kss.stockDiscussion.repository.posterRepository.PosterRepository;
import com.kss.stockDiscussion.repository.replyRepository.ReplyRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.request.reply.CreateReplyRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.reply.CreateReplyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.kss.stockDiscussion.domain.newsFeed.NewsFeedType.*;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final PosterRepository posterRepository;
    private final NewsFeedService newsFeedService;

    @Override
    public ResponseEntity<? super CreateReplyResponseDto> createReply(CreateReplyRequestDto dto) {
        try {
            Long userId = dto.getUserId();

            Long posterId = dto.getPosterId();
            User user = userRepository.findById(userId).get();
            Poster poster = posterRepository.findById(posterId).get();
            String contents = dto.getContents();

            Reply newReply = Reply.builder().user(user)
                    .poster(poster)
                    .contents(contents).build();
            replyRepository.save(newReply);

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
