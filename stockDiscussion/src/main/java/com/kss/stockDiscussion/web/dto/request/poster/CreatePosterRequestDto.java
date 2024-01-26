package com.kss.stockDiscussion.web.dto.request.poster;


import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.*;

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

    @Builder
    private CreatePosterRequestDto(Long userId, String title, String contents) {
        this.userId = userId;
        this.title = title;
        this.contents = contents;
    }
}
