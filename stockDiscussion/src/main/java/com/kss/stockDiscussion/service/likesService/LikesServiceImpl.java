package com.kss.stockDiscussion.service.likesService;


import com.kss.stockDiscussion.domain.like.LikeType;
import com.kss.stockDiscussion.domain.like.Likes;
import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.reply.Reply;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.likeRepository.LikesJpaRepository;
import com.kss.stockDiscussion.repository.posterRepository.PosterJpaRepository;
import com.kss.stockDiscussion.repository.replyRepository.ReplyJpaRepository;
import com.kss.stockDiscussion.repository.userRepository.UserJpaRepository;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.likes.CreateLikesRequestDto;
import com.kss.stockDiscussion.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.likes.CreateLikesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.kss.stockDiscussion.domain.like.Likes.*;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService{
    private final LikesJpaRepository likesJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PosterJpaRepository posterJpaRepository;
    private final ReplyJpaRepository replyJpaRepository;
    private final NewsFeedService newsFeedService;

    @Override
    public ResponseEntity<? super CreateLikesResponseDto> createLikes(CreateLikesRequestDto dto) {

        try {
            LikeType likeType = dto.getLikeType();
            Long posterId = dto.getPosterId(); //@NotBlank 이다
            Long replyId = dto.getReplyId();
            if (likeType == LikeType.REPLY && replyId == null) return CreateLikesResponseDto.validationFail();

            Long userId = dto.getUserId();
            User user = userJpaRepository.findById(userId).get();
            Poster poster =  posterJpaRepository.findById(posterId).get();

            LikesBuilder likesBuilder = builder().likeType(likeType)
                    .user(user)
                    .poster(poster);
            if(likeType == LikeType.REPLY){
                Reply reply = replyJpaRepository.findById(replyId).get();
                likesBuilder.reply(reply);
                reply.incrementLikeCount();
            }
            if(likeType == LikeType.POSTER){
                poster.incrementLikeCount();
            }
            Likes newLikes = likesBuilder.build();
            likesJpaRepository.save(newLikes);

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
