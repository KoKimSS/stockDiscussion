package com.kss.stockDiscussion.web.dto.response.auth;

import com.kss.stockDiscussion.common.ResponseCode;
import com.kss.stockDiscussion.common.ResponseMessage;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class EmailCheckResponseDto extends ResponseDto {
    private EmailCheckResponseDto() {
        super();
    }

    public static ResponseEntity<EmailCheckResponseDto> success() {
        EmailCheckResponseDto responseBody = new EmailCheckResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> duplicateEmail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATE_EMAIL, ResponseMessage.DUPLICATE_EMAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
