package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.config.auth.PrincipalDetails;
import com.kss.stockDiscussion.config.jwt.JwtUtil;
import com.kss.stockDiscussion.service.userService.UserService;
import com.kss.stockDiscussion.web.dto.request.user.UpdatePasswordRequestDto;
import com.kss.stockDiscussion.web.dto.request.user.UpdateProfileRequestDto;
import com.kss.stockDiscussion.web.dto.response.user.UpdatePasswordResponseDto;
import com.kss.stockDiscussion.web.dto.response.user.UpdateProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/update-password")
    ResponseEntity<? super UpdatePasswordResponseDto> updatePassword(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody@Valid UpdatePasswordRequestDto requestBody
    ) {
        Long loginId = principalDetails.getUser().getId();
        Long userId = requestBody.getUserId();
        if (loginId != userId) return UpdatePasswordResponseDto.certificationFail();

        ResponseEntity<? super UpdatePasswordResponseDto> response = userService.updatePassword(requestBody);
        return response;
    }

    @PostMapping("/update-profile")
    ResponseEntity<? super UpdateProfileResponseDto> updateProfile(
            @RequestBody@Valid UpdateProfileRequestDto requestBody
    ) {
        ResponseEntity<? super UpdateProfileResponseDto> response = userService.updateProfile(requestBody);
        return response;
    }

}
