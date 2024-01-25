package com.kss.stockDiscussion.web.dto.response.newsFeed;


import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetFollowingNewsFeedResponseDto extends ResponseDto {
    private final Page<GetFollowingNewsFeedDto> newsFeedPage;
    public GetFollowingNewsFeedResponseDto(Page<GetFollowingNewsFeedDto> newsFeedPage) {
        this.newsFeedPage = newsFeedPage;
    }

    public static ResponseEntity<GetFollowingNewsFeedResponseDto> success(Page<GetFollowingNewsFeedDto> newsFeedPage) {
        GetFollowingNewsFeedResponseDto responseBody = new GetFollowingNewsFeedResponseDto(newsFeedPage);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
