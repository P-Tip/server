package com.ptip.auth.common;

public interface ResponseMessage {

    String SUCCESS = "Success.(성공)";

    String VALIDATION_FAIL = "Validation failed.(유효성 검사 실패)";
    String DUPLICATE_ID = "Duplicate Id.(아이디 중복)";

    String SIGN_IN_FAIL = "Login information mismatch.(로그인 정보 불일치)";
    String CERTIFICATION_FAIL = "Certification failed.(인증 실패)";

    String INVALID_TOKEN = "Invalid Token.(유효하지 않은 토큰)";

    String DATABASE_ERROR = "Database error.(데이터베이스 에러)";
}
