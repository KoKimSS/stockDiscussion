package com.kss.stockDiscussion.web.dto.request.user;

import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class UpdatePasswordRequestDto extends RequestDto {
    private Long userId;
    private String password;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$")
    private String newPassword;

    @Builder
    private UpdatePasswordRequestDto(Long userId, String password, String newPassword) {
        this.userId = userId;
        this.password = password;
        this.newPassword = newPassword;
    }
}
