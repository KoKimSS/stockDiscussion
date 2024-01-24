package com.kss.stockDiscussion.web.dto.request.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class EmailCertificationRequestDto {
    @NotBlank
    @Email
    private String email;
}
