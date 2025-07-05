package com.example.soa.api

//여기서 정의한 데이터 클래스를  ApiService.kt에서 사용

//회원가입 요청 보낼 때 양식
data class SignUpRequest(
    val appId: String,
    val password: String,
    val name: String,
    val nickname: String,
    val phoneNumber: String,
    val birth: String,
    val gender: String,
    val disabilityType: String
)

//로그인 요청 보낼 때 양식
data class LoginRequest(
    val appId: String,
    val password: String
)

//아이디 체크 보낼 때 양식
data class IdCheckRequest(
    val appId: String
)

//응답 양식
data class Response<T> (
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T?
)

//응답 양식 중 회원가입
data class SignUpResult(
    val userId: Long,
    val appId: String
)

//응답 양식 중 로그인
data class LoginResult(
    val memberId: Long,
    val accessToken: String
)

//응답 양식 중 아이디 체크
data class IdCheckResult(
    val isAvailable: Boolean,
    val message: String
)

typealias LoginResponse = Response<LoginResult>
typealias SignUpResponse = Response<SignUpResult>
typealias IdCheckResponse = Response<IdCheckResult>