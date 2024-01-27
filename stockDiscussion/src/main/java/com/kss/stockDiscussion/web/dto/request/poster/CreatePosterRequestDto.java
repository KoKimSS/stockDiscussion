package com.kss.stockDiscussion.web.dto.request.poster;


import com.kss.stockDiscussion.common.ValidationMessage;
import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.kss.stockDiscussion.common.ValidationMessage.*;

@Data
@NoArgsConstructor
public class CreatePosterRequestDto extends RequestDto {
    @NotNull(message = NOT_NULL_USER)
    private Long userId;
    @NotBlank(message = NOT_BLANK_TITLE)
    private String title;
    @NotBlank(message = NOT_BLANK_CONTENTS)
    private String contents;

    @Builder
    private CreatePosterRequestDto(Long userId, String title, String contents) {
        this.userId = userId;
        this.title = title;
        this.contents = contents;
    }
}
