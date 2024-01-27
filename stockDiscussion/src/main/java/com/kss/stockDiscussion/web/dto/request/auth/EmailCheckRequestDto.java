package com.kss.stockDiscussion.web.dto.request.auth;

import com.kss.stockDiscussion.common.ValidationMessage;
import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class EmailCheckRequestDto extends RequestDto {
    @NotBlank(message = ValidationMessage.NOT_BLANK_EMAIL)
    @Email(message = ValidationMessage.NOT_EMAIL)
    private String email;


    @Builder
    private EmailCheckRequestDto(String email) {
        this.email = email;
    }
}
