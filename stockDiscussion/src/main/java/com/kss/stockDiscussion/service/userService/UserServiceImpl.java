package com.kss.stockDiscussion.service.userService;

import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.domain.user.User;
import com.kss.stockDiscussion.repository.userRepository.UserRepository;
import com.kss.stockDiscussion.web.dto.request.user.UpdatePasswordRequestDto;
import com.kss.stockDiscussion.web.dto.request.user.UpdateProfileRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.user.UpdatePasswordResponseDto;
import com.kss.stockDiscussion.web.dto.response.user.UpdateProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    @Transactional
    public ResponseEntity<? super UpdatePasswordResponseDto> updatePassword(UpdatePasswordRequestDto dto) {
        Long requestUserId = dto.getUserId();

        Long loginId = JwtUtil.findUserFromAuth().getId();
        if (loginId != requestUserId) {
            return UpdatePasswordResponseDto.validationFail();
        }

        try {
            Optional<User> userById = userRepository.findById(requestUserId);
            if (!userById.isPresent()) {
                return UpdatePasswordResponseDto.databaseError();
            }

            User user = userById.get();
            // 새로운 비밀번호 해싱
            String newPassword = dto.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.updatePassword(hashedPassword);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return UpdatePasswordResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<? super UpdateProfileResponseDto> updateProfile(UpdateProfileRequestDto dto) {
        Long requestUserId = dto.getUserId();

        Long loginId = JwtUtil.findUserFromAuth().getId();
        if (loginId != requestUserId) {
            return UpdateProfileResponseDto.validationFail();
        }

        try {
            Optional<User> userById = userRepository.findById(requestUserId);
            if (!userById.isPresent()) {
                return UpdateProfileResponseDto.databaseError();
            }

            User user = userById.get();
            user.updateProfile(dto.getName(), dto.getImgPath(), dto.getIntroduction());
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return UpdateProfileResponseDto.success();
    }
}
