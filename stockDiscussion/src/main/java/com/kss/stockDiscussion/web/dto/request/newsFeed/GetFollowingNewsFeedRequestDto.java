package com.kss.stockDiscussion.web.dto.request.newsFeed;

import com.kss.stockDiscussion.web.dto.request.RequestDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class GetFollowingNewsFeedRequestDto extends RequestDto {
    @NotBlank
    private Long userId;
    @NotBlank
    private int page;
    @NotBlank
    private int size;
}
