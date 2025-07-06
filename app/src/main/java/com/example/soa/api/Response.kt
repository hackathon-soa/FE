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

//응답 양식 중 이미지 리턴
data class UploadImageResult(
    val userId: Long,
    val verificationImageUrl: String
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


///////////////////////////////////////

// 1) 페이징된 결과 전체
data class PageResult<T>(
    val totalElements: Int,
    val totalPages: Int,
    val size: Int,
    val content: List<T>,
    val number: Int,
    val sort: Sort,
    val numberOfElements: Int,
    val pageable: Pageable,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
)

// 2) 스토리 아이템 하나
data class StoryItem(
    val memberId: Long,
    val nickname: String,
    val profileImageUrl: String?,
    val imageUrl: String?
)

// 3) 정렬 정보
data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)

// 4) 페이지 정보
data class Pageable(
    val offset: Long,
    val sort: Sort,
    val paged: Boolean,
    val pageNumber: Int,
    val pageSize: Int,
    val unpaged: Boolean
)

class EmptyResult


/** `/api/segment/{courseId}` 결과의 최상위 result */
data class CourseDetailResult(
    val courseId: Long,
    val isOwner: Boolean,
    val segments: List<CourseSegment>
)

/** 각 세그먼트의 공통 속성 */
data class CourseSegment(
    val segmentOrder: Int,
    val segmentType: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val staySegment: StaySegment?,    // 숙박 세그먼트일 때
    val moveSegment: MoveSegment?     // 이동 세그먼트일 때
)

data class StaySegment(
    val segmentId: Long,
    val locationName: String,
    val locationAddress: String
)

data class MoveSegment(
    val segmentId: Long,
    val movementType: String,
    val movementDistanceKm: Double
)


/**
 * 최종 /api/search/region 응답의 result 부분
 */
data class FoundCoursesResponse(
    val foundCourses: List<FoundCourse>
)

/**
 * 각 코스 검색 결과 아이템
 */
data class FoundCourse(
    val startDate: String,
    val endDate: String,
    val region: String,
    val gender: String,
    val userName: String,
    val disabilityType: String,
    val liked: Boolean
)

/**
 * GET /api/home/my-courses 의 result
 */
data class MyCoursesResponse(
    val myCourses: List<MyCourse>
)

/**
 * 내 코스 리스트 아이템
 */
data class MyCourse(
    val startDate: String,
    val endDate: String,
    val region: String,
    val gender: String,
    val userName: String,
    val disabilityType: String
)

/**
 * POST /courses 요청 바디
 */
data class CourseCreateRequest(
    val title: String,
    val region: String,
    val startTime: String,          // ISO 8601 포맷 예: "2025-07-05T21:59:29.377Z"
    val endTime: String,
    val interests: String,
    val specialNote: String,
    val preferredGender: String,    // "MALE" 또는 "FEMALE"
    val travelThemes: List<String>,
    val segments: List<CourseSegmentRequest>
)

data class CourseSegmentRequest(
    val startTime: String,
    val endTime: String,
    val moving: Boolean,
    val movementType: String,
    val movementDistanceKm: Double,
    val locationName: String,
    val locationAddress: String
)

/**
 * POST /courses 응답 바디를 그대로 매핑
 */
data class CreateCourseResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Int          // 새로 생성된 코스의 ID
)

typealias LoginResponse = Response<LoginResult>
typealias SignUpResponse = Response<SignUpResult>
typealias IdCheckResponse = Response<IdCheckResult>
typealias ImageUploadResponse = Response<UploadImageResult>