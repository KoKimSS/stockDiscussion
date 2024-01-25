package com.kss.stockDiscussion.service.followService;

import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.follow.Follow;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.followRepository.FollowRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.follow.FollowRequestDto;
import com.kss.stockDiscussion.web.dto.response.follow.FollowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<? super FollowResponseDto> follow(FollowRequestDto dto) {

        Long followingId = dto.getFollowingId();
        Long followerId = dto.getFollowerId();
        User user = JwtUtil.findUserFromAuth();
        if(user.getId()!=followerId){
            return FollowResponseDto.certificationFail();
        }
        try{
            User follower = userRepository.findById(followerId).get();
            User following = userRepository.findById(followingId).get();

            Follow follow = Follow.builder().follower(follower).following(following).build();
            followRepository.save(follow);
        }catch (Exception exception){
            exception.printStackTrace();
            FollowResponseDto.databaseError();
        }

        return FollowResponseDto.success();
    }
}
