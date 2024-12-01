package com.geoparty.spring_boot.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
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
    ORGANIZATION_NOT_FOUND(404, "존재하지 않는 환경단체입니다."),
    USERPARTY_NOT_FOUND(404, "존재하지 않는 유저 혹은 파티입니다."),
    PARTY_NOT_FOUND(404, "존재하지 않는 파티입니다."),
    PAYMENT_NOT_FOUND(404, "결제내역을 찾을 수 없습니다."),
    PARTY_IS_FULL(409, "파티가 가득 찼습니다."),
    USER_ALREADY_IN_PARTY(409, "파티에 이미 존재하는 멤버입니다.");


    private final int errorCode;
    private final String errorMsg;

}
