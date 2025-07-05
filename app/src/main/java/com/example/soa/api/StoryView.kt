package com.example.soa.api

interface StoryView {
    /**
     * Stories 목록을 성공적으로 받아왔을 때 호출됩니다.
     * @param stories 페이지 결과의 content 리스트
     */
    fun onStoriesLoaded(stories: List<StoryItem>)

    /**
     * API 호출에 실패했거나, 서버에서 에러를 return 했을 때 호출됩니다.
     * @param errorMsg 에러 메시지
     */
    fun onStoriesError(errorMsg: String)


    /** 동행 신청 성공 시 호출 */
    fun onRegisterSuccess()
    /** 신청 실패 시 호출 (네트워크 오류 또는 서버 에러 메시지) */
    fun onRegisterFailure(errorMsg: String)


    /** 코스 상세 조회 성공 */
    fun onCourseDetailLoaded(detail: CourseDetailResult)

    /** 코스 상세 조회 실패 */
    fun onCourseDetailError(errorMsg: String)

    /** 지역 검색 결과 성공 - now returns List<FoundCourse> */
    fun onSearchResultsLoaded(results: List<FoundCourse>)

    /** 지역 검색 실패 */
    fun onSearchError(errorMsg: String)

    /** 내 코스 목록 로드 성공 */
    fun onMyCoursesLoaded(courses: List<MyCourse>)

    /** 내 코스 목록 로드 실패 */
    fun onMyCoursesError(errorMsg: String)

}