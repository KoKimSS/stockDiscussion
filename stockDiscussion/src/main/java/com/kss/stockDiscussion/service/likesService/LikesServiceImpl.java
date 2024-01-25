package com.kss.stockDiscussion.service.likesService;


import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.like.LikeType;
import com.kss.stockDiscussion.domain.like.Likes;
import com.kss.stockDiscussion.domain.newsFeed.ActivityType;
import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.reply.Reply;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.likeRepository.LikesRepository;
import com.kss.stockDiscussion.repository.posterRepository.PosterRepository;
import com.kss.stockDiscussion.repository.replyRepository.ReplyRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.service.newsFeedService.NewsFeedService;
import com.kss.stockDiscussion.web.dto.request.likes.CreateLikesRequestDto;
import com.kss.stockDiscussion.web.dto.request.newsFeed.CreateNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.likes.CreateLikesResponseDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.GetMyNewsFeedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.kss.stockDiscussion.domain.like.Likes.*;

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

            Long loginId = JwtUtil.findUserFromAuth().getId();
            Long requestUserId = dto.getUserId();
            if (loginId != requestUserId) return CreateLikesResponseDto.certificationFail();
            User requestUser = userRepository.findById(loginId).get();
            Poster requestPoster =  posterRepository.findById(posterId).get();

            LikesBuilder likesBuilder = builder().likeType(likeType)
                    .user(requestUser)
                    .poster(requestPoster);

            if(likeType == LikeType.REPLY){
                Reply requestReply = replyRepository.findById(replyId).get();
                likesBuilder.reply(requestReply);
            }
            Likes newLikes = likesBuilder.build();
            likesRepository.save(newLikes);

            CreateNewsFeedRequestDto createNewsFeedRequestDto = CreateNewsFeedRequestDto.builder()
                    .posterId(posterId)
                    .activityType(ActivityType.LIKE)
                    .activityUserId(requestUserId)
                    .relatedUserId(requestPoster.getOwner().getId())
                    .build();

            newsFeedService.createNewsFeed(createNewsFeedRequestDto);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return null;
    }


}
