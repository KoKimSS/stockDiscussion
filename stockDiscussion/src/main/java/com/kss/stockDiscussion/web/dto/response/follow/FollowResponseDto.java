package com.kss.stockDiscussion.web.dto.response.follow;

import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class FollowResponseDto extends ResponseDto {
    public FollowResponseDto() {
        super();
    }
    public static ResponseEntity<FollowResponseDto> success() {
        FollowResponseDto responseBody = new FollowResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseEntity<ResponseDto> response = ResponseDto.databaseError();
        return response;
    }

    public static ResponseEntity<ResponseDto> certificationFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
