package com.kss.stockDiscussion.web.dto.request.auth;

import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class EmailCheckRequestDto extends RequestDto {
    @NotBlank
    private String email;
}
