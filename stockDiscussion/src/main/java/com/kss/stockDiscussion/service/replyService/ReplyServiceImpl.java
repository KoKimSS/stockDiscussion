package com.kss.stockDiscussion.service.replyService;

import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.reply.Reply;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.posterRepository.PosterRepository;
import com.kss.stockDiscussion.repository.replyRepository.ReplyRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.reply.CreateReplyRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.reply.CreateReplyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final PosterRepository posterRepository;

    @Override
    public ResponseEntity<? super CreateReplyResponseDto> createReply(CreateReplyRequestDto dto) {
        try {
            Long requestUserId = dto.getUserId();
            Long loginId = JwtUtil.findUserFromAuth().getId();
            if(loginId!=requestUserId) return ResponseDto.certificationFail();

            Long posterId = dto.getPosterId();
            User user = userRepository.findById(requestUserId).get();
            Poster poster = posterRepository.findById(posterId).get();
            String contents = dto.getContents();

            Reply newReply = Reply.builder().owner(user)
                    .poster(poster)
                    .contents(contents).build();
            replyRepository.save(newReply);

        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CreateReplyResponseDto.success();
    }
}
