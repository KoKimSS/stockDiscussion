package com.kss.stockDiscussion.web.dto.response.newsFeed;

import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.likes.CreateLikesResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CreateNewsFeedResponseDto extends ResponseDto {
    public CreateNewsFeedResponseDto() {
        super();
    }
    public static ResponseEntity<CreateNewsFeedResponseDto> success() {
        CreateNewsFeedResponseDto responseBody = new CreateNewsFeedResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseEntity<ResponseDto> response = ResponseDto.databaseError();
        return response;
    }
}
