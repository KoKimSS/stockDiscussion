package com.kss.stockDiscussion.service.replyService;

import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.reply.Reply;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.followRepository.FollowRepository;
import com.kss.stockDiscussion.repository.newsFeedRepository.NewsFeedRepository;
import com.kss.stockDiscussion.repository.posterRepository.PosterRepository;
import com.kss.stockDiscussion.repository.replyRepository.ReplyRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
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
    private final FollowRepository followRepository;
    private final NewsFeedRepository newsFeedRepository;

    @Override
    public ResponseEntity<? super CreateReplyResponseDto> createReply(CreateReplyRequestDto dto) {
        try {
            Long userId = dto.getUserId();
            Long loginId = JwtUtil.findUserFromAuth().getId();
            if(loginId!=userId) return ResponseDto.certificationFail();

            Long posterId = dto.getPosterId();
            User user = userRepository.findById(userId).get();
            Poster poster = posterRepository.findById(posterId).get();
            String contents = dto.getContents();

            Reply newReply = Reply.builder().owner(user)
                    .poster(poster)
                    .contents(contents).build();
            replyRepository.save(newReply);

            //나를 팔로우 하는 사람들의 뉴스피드 업데이트
            List<Follow> followerFollowList = followRepository.findByFollowingId(userId);
            List<NewsFeed> newsFeedList = followerFollowList.stream()
                    .map(followerFollow -> {
                        User newsFeedOwner = followerFollow.getFollower();
                        return NewsFeed.builder()
                                .newsFeedType(FOLLOWING_REPLY)
                                .user(newsFeedOwner)
                                .activityUser(user)
                                .relatedPoster(poster)
                                .relatedUser(poster.getOwner())
                                .build();
                    })
                    .collect(Collectors.toList());
            //내가 좋아요를 누른 게시글 소유자의 뉴스피드 업데이트
            NewsFeed newsFeed = NewsFeed.builder()
                    .newsFeedType(MY_RELY)
                    .user(poster.getOwner())
                    .activityUser(user)
                    .relatedPoster(poster)
                    .build();
            newsFeedList.add(newsFeed);
            newsFeedRepository.saveAll(newsFeedList);
        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CreateReplyResponseDto.success();
    }
}
