package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.service.userService.UserService;
import com.kss.stockDiscussion.service.userService.UserServiceImpl;
import com.kss.stockDiscussion.web.dto.request.user.UpdatePasswordRequestDto;
import com.kss.stockDiscussion.web.dto.response.user.UpdatePasswordResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping("user")
    String roleUserTest() {
        return "Role_User";
    }

    @PostMapping("/update-password")
    ResponseEntity<? super UpdatePasswordResponseDto> updatePassword(
            @RequestBody@Valid UpdatePasswordRequestDto requestBody) {
        ResponseEntity<? super UpdatePasswordResponseDto> response = userService.updatePassword(requestBody);
        return response;
    }

    @PostMapping("/update-profile")
    ResponseEntity<? super UpdatePasswordResponseDto> updateProfile(
            @RequestBody@Valid UpdatePasswordRequestDto requestBody) {
        ResponseEntity<? super UpdatePasswordResponseDto> response = userService.updateProfile(requestBody);
        return response;
    }

}
