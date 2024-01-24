package com.kss.stockDiscussion.web.controller;

import com.kss.stockDiscussion.service.UserService;
import com.kss.stockDiscussion.web.form.RegistForm;
import com.kss.stockDiscussion.web.form.ResponseRegist;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("regist")
    ResponseRegist registUser(@ModelAttribute@Validated RegistForm registForm){
        //이메일이 중복이면
        if(userService.isExistEmail(registForm.getEmail())){
            return new ResponseRegist("이메일 중복");
        }
//        userService.


        return new ResponseRegist("성공");
    }

}
