package com.kss.stockDiscussion.web.dto.request.user;

import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class UpdatePasswordRequestDto extends RequestDto {
    private Long userId;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$")
    private String password;
}
