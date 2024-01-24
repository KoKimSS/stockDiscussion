package com.kss.stockDiscussion.web.dto.request.user;

import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UpdateProfileRequestDto extends RequestDto {
    @NotBlank
    private Long userId;
    @NotBlank
    private String name;
    @NotBlank
    private String imgPath;
    @NotBlank
    private String introduction;
}
