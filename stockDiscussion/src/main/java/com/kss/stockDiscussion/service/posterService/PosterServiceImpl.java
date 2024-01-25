package com.kss.stockDiscussion.service.posterService;

import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.poster.Poster;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.posterRepository.PosterRepository;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.poster.CreatePosterRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.poster.CreatePosterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.kss.stockDiscussion.config.jwt.JwtUtil.*;

@Service
@RequiredArgsConstructor
public class PosterServiceImpl implements PosterService{

    private final PosterRepository posterRepository;
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<? super CreatePosterResponseDto> createPoster(CreatePosterRequestDto dto) {
        Long requestUserId = dto.getUserId();
        if(findUserFromAuth().getId()!= requestUserId){
            return CreatePosterResponseDto.certificationFail();
        }


        try {
            User owner = userRepository.findById(requestUserId).get();
            Poster newPoster = Poster.builder().title(dto.getTitle())
                    .contents(dto.getContents())
                    .owner(owner)
                    .build();

            posterRepository.save(newPoster);
        }catch (Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CreatePosterResponseDto.success();
    }
}
