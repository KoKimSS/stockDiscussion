package com.kss.stockDiscussion.service.likesService;


import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.like.LikeType;
import com.kss.stockDiscussion.domain.like.Likes;
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
import com.kss.stockDiscussion.web.dto.request.likes.CreateLikesRequestDto;
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
    private final FollowRepository followRepository;
    private final NewsFeedRepository newsFeedRepository;

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
                Reply requestReply = replyRepository.findById(replyId).get();
                likesBuilder.reply(requestReply);
            }
            Likes newLikes = likesBuilder.build();
            likesRepository.save(newLikes);


            //나를 팔로우 하는 사람들의 뉴스피드 업데이트
            List<Follow> followerFollowList = followRepository.findByFollowingId(userId);
            List<NewsFeed> newsFeedList = followerFollowList.stream()
                    .map(followerFollow -> {
                        User newsFeedOwner = followerFollow.getFollower();
                        return NewsFeed.builder()
                                .newsFeedType(FOLLOWING_LIKE)
                                .user(newsFeedOwner)
                                .activityUser(user)
                                .relatedPoster(poster)
                                .relatedUser(poster.getOwner())
                                .build();
                    })
                    .collect(Collectors.toList());
            //내가 좋아요를 누른 게시글 소유자의 뉴스피드 업데이트
            NewsFeed newsFeed = NewsFeed.builder()
                    .newsFeedType(MY_LIKE)
                    .user(poster.getOwner())
                    .activityUser(user)
                    .relatedPoster(poster)
                    .build();
            newsFeedList.add(newsFeed);
            newsFeedRepository.saveAll(newsFeedList);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return CreateLikesResponseDto.success();
    }
}
