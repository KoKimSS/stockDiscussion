package com.kss.stockDiscussion.service.likesService;

import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.domain.like.LikeType;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.reply.Reply;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.posterRepository.PosterRepository;
import com.kss.stockDiscussion.repository.replyRepository.ReplyRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.likes.CreateLikesRequestDto;
import com.kss.stockDiscussion.web.dto.response.likes.CreateLikesResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;


@SpringBootTest
@Transactional
class LikesServiceTest {

    @Autowired
    private LikesService likesService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PosterRepository posterRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @DisplayName("라이크 종류가 포스터인 경우")
    @Test
    public void createLikesWithTypePoster() throws Exception {
        //given
        User user = createUser("user");
        userRepository.save(user);
        Poster poster = createPoster("포스터");
        posterRepository.save(poster);
        Reply reply = createReply("리플", user, poster);
        replyRepository.save(reply);
        LikeType type = LikeType.POSTER;
        CreateLikesRequestDto requestDto = getCreateLikesRequestDtoBuilder(type, user.getId(), null, poster.getId());

        //when
        ResponseEntity<? super CreateLikesResponseDto> response = likesService.createLikes(requestDto);
        int likeCount = poster.getLikeCount();

        //then
        Assertions.assertThat(likeCount).isEqualTo(1);
        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @DisplayName("라이크 종류가 댓글인 경우")
    @Test
    public void createLikesWithTypeReply() throws Exception {
        //given
        User user = createUser("user");
        userRepository.save(user);
        Poster poster = createPoster("포스터");
        posterRepository.save(poster);
        Reply reply = createReply("리플", user, poster);
        replyRepository.save(reply);
        LikeType type = LikeType.REPLY;
        CreateLikesRequestDto requestDto = getCreateLikesRequestDtoBuilder(type, user.getId(), reply.getId(), poster.getId());

        //when
        ResponseEntity<? super CreateLikesResponseDto> response = likesService.createLikes(requestDto);
        int likeCount = reply.getLikeCount();

        //then
        Assertions.assertThat(likeCount).isEqualTo(1);
        Assertions.assertThat(response.getBody())
                .extracting("code", "message")
                .containsExactly(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private static Poster createPoster(String title) {
        return Poster.builder().title(title).build();
    }

    private static Reply createReply(String contents,User user,Poster poster) {
        return Reply.builder()
                .user(user)
                .contents(contents)
                .poster(poster).build();
    }

    private static CreateLikesRequestDto getCreateLikesRequestDtoBuilder(LikeType type,Long userId,Long replyId,Long posterId) {
        return CreateLikesRequestDto.builder()
                .likeType(type)
                .userId(userId)
                .replyId(replyId)
                .posterId(posterId).build();
    }

    private static User createUser(String name) {
        return User.builder().name(name).build();
    }
}