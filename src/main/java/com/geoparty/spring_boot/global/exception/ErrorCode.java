package com.geoparty.spring_boot.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    BAD_REQUEST(400, "잘못된 요청입니다."),
    UNAUTHORIZED(401,"잘못된 토큰입니다."),
    INVALID_TOKEN(401, "토큰이 유효하지 않습니다."),
    NOT_PRIVIEGED(403, "접근 권한이 없습니다."),
    NOT_FOUND_DATA(404, "데이터를 찾지 못했습니다."),
    NOT_FOUND(404, "페이지를 찾지 못했습니다."),
    NOT_ENOUGH_MILEAGE(403,"마일리지가 부족합니다." ),

    MEMBER_NOT_FOUND(404, "존재하지 않는 유저입니다."),
    ORGANIZATION_NOT_FOUND(404, "존재하지 않는 환경단체입니다.");


    private final int errorCode;
    private final String errorMsg;

}
