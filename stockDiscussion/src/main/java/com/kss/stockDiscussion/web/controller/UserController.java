package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

   
}
