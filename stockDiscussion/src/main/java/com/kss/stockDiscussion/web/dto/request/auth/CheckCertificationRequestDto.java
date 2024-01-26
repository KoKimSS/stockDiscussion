package com.kss.stockDiscussion.web.dto.request.auth;

import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class CheckCertificationRequestDto extends RequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String certificationNumber;
    @NotBlank
    private LocalDateTime certificateTime;

    @Builder
    public CheckCertificationRequestDto(String email, String certificationNumber, LocalDateTime certificateTime) {
        this.email = email;
        this.certificationNumber = certificationNumber;
        this.certificateTime = certificateTime;
    }
}
