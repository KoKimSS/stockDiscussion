package com.kss.stockDiscussion.web.dto.request.auth;

import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
public class CheckCertificationRequestDto extends RequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String certificationNumber;
}
