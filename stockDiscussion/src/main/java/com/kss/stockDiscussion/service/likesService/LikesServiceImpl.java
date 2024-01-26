package com.kss.stockDiscussion.service.likesService;


import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.like.LikeType;
import com.kss.stockDiscussion.domain.like.Likes;
import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.reply.Reply;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.followRepository.FollowRepository;
import com.kss.stockDiscussion.repository.likeRepository.LikesRepository;
import com.kss.stockDiscussion.repository.newsFeedRepository.NewsFeedRepository;
import com.kss.stockDiscussion.repository.posterRepository.PosterRepository;
import com.kss.stockDiscussion.repository.replyRepository.ReplyRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.likes.CreateLikesRequestDto;
import com.kss.stockDiscussion.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.likes.CreateLikesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.kss.stockDiscussion.domain.like.Likes.*;
import static com.kss.stockDiscussion.domain.newsFeed.NewsFeedType.*;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService{
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final PosterRepository posterRepository;
    private final ReplyRepository replyRepository;
    private final NewsFeedService newsFeedService;

    @Override
    public ResponseEntity<? super CreateLikesResponseDto> createLikes(CreateLikesRequestDto dto) {

        try {
            LikeType likeType = dto.getLikeType();
            Long posterId = dto.getPosterId(); //@NotBlank 이다
            Long replyId = dto.getReplyId();
            if (likeType == LikeType.REPLY && replyId == null) return CreateLikesResponseDto.validationFail();

            Long userId = dto.getUserId();
            User user = userRepository.findById(userId).get();
            Poster poster =  posterRepository.findById(posterId).get();

            LikesBuilder likesBuilder = builder().likeType(likeType)
                    .user(user)
                    .poster(poster);
            if(likeType == LikeType.REPLY){
                Reply reply = replyRepository.findById(replyId).get();
                likesBuilder.reply(reply);
                reply.incrementLikeCount();
            }
            if(likeType == LikeType.POSTER){
                poster.incrementLikeCount();
            }
            Likes newLikes = likesBuilder.build();
            likesRepository.save(newLikes);

            //뉴스피드 생성 서비스 호출 !
            CreateNewsFeedRequestDto createNewsFeedRequestDto = CreateNewsFeedRequestDto.builder()
                    .user(user)
                    .activityType(ActivityType.LIKE)
                    .relatedPoster(poster)
                    .relatedUser(poster.getOwner())
                    .build();

            newsFeedService.createNewsFeed(createNewsFeedRequestDto);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CreateLikesResponseDto.success();
    }
}
