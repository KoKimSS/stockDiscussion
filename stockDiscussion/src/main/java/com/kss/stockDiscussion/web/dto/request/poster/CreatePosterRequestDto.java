package com.kss.stockDiscussion.web.dto.request.poster;


import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class CreatePosterRequestDto extends RequestDto {
    @NotBlank
    private Long userId;
    @NotBlank
    private String title;
    @NotBlank
    private String contents;
}
