package com.kss.stockDiscussion.web.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistForm {
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String introduction;
    private String img_path;
}
