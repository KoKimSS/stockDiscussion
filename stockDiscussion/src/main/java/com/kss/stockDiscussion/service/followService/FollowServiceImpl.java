package com.kss.stockDiscussion.service.followService;

import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.followRepository.FollowRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.follow.StartFollowRequestDto;
import com.kss.stockDiscussion.web.dto.response.follow.StartFollowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<? super StartFollowResponseDto> follow(StartFollowRequestDto dto) {

        Long followingId = dto.getFollowingId();
        Long followerId = dto.getFollowerId();
        User user = JwtUtil.findUserFromAuth();
        if(user.getId()!=followerId){
            return StartFollowResponseDto.certificationFail();
        }
        try{
            User follower = userRepository.findById(followerId).get();
            User following = userRepository.findById(followingId).get();

            Follow follow = Follow.builder().follower(follower).following(following).build();
            followRepository.save(follow);
        }catch (Exception exception){
            exception.printStackTrace();
            StartFollowResponseDto.databaseError();
        }

        return StartFollowResponseDto.success();
    }
}
