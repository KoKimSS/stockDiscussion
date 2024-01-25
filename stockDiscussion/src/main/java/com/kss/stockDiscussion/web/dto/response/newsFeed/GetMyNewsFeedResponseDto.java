package com.kss.stockDiscussion.web.dto.response.newsFeed;


import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetMyNewsFeedResponseDto extends ResponseDto {
    private final Page<GetMyNewsFeedDto> newsFeedPage;
    public GetMyNewsFeedResponseDto(Page<GetMyNewsFeedDto> newsFeedPage) {
        this.newsFeedPage = newsFeedPage;
    }

    public static ResponseEntity<GetMyNewsFeedResponseDto> success(Page<GetMyNewsFeedDto> newsFeedPage) {
        GetMyNewsFeedResponseDto responseBody = new GetMyNewsFeedResponseDto(newsFeedPage);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
