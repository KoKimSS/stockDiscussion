package com.kss.stockDiscussion.service.userService;

import com.kss.stockDiscussion.web.dto.request.user.UpdatePasswordRequestDto;
import com.kss.stockDiscussion.web.dto.request.user.UpdateProfileRequestDto;
import com.kss.stockDiscussion.web.dto.response.user.UpdatePasswordResponseDto;
import com.kss.stockDiscussion.web.dto.response.user.UpdateProfileResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?super UpdatePasswordResponseDto> updatePassword(UpdatePasswordRequestDto dto);

    ResponseEntity<? super UpdateProfileResponseDto> updateProfile(UpdateProfileRequestDto dto);
}
