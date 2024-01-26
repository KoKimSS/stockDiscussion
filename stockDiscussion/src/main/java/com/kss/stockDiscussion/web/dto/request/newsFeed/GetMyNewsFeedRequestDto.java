package com.kss.stockDiscussion.web.dto.request.newsFeed;

import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class GetMyNewsFeedRequestDto extends RequestDto {
    @NotBlank
    private Long userId;
    @NotBlank
    private int page;
    @NotBlank
    private int size;

    @Builder
    private GetMyNewsFeedRequestDto(Long userId, int page, int size) {
        this.userId = userId;
        this.page = page;
        this.size = size;
    }
}
