package com.kss.stockDiscussion.web.dto.request.auth;

import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class SignUpRequestDto extends RequestDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$")
    private String password;

    @NotBlank
    private String name;
    @NotBlank
    private String imgPath;

    @NotBlank
    private String introduction;

    @NotBlank
    private String certificationNumber;

    @Builder
    private SignUpRequestDto(String email, String password, String name, String imgPath, String introduction, String certificationNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imgPath = imgPath;
        this.introduction = introduction;
        this.certificationNumber = certificationNumber;
    }
}
