package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping("user")
    String roleUserTest() {
        return "Role_User";
    }
}
