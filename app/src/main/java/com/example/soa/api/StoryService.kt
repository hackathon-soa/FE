package com.example.soa.api

import android.content.Context
import com.example.soa.api.Response as ApiResponse                // 여러분의 래퍼
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response as RetrofitResponse                    // Retrofit 의 Response

class StoryService(
    private val view: StoryView,
    context: Context
) {
    private val api = NetworkManager.retrofit.create(ApiService::class.java)
    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    //스토리 접근
    fun fetchStories(page: Int = 0, size: Int = 10) {
        val rawToken = prefs.getString("accessToken", "") ?: ""
        val bearer = "Bearer $rawToken"

        api.getStories(bearer, page, size)
            .enqueue(object : Callback<ApiResponse<PageResult<StoryItem>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PageResult<StoryItem>>>,
                    response: RetrofitResponse<ApiResponse<PageResult<StoryItem>>>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && body.isSuccess && body.result != null) {
                            view.onStoriesLoaded(body.result.content)
                        } else {
                            view.onStoriesError(body?.message ?: "빈 응답 혹은 실패")
                        }
                    } else {
                        view.onStoriesError("HTTP 에러: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<PageResult<StoryItem>>>,
                    t: Throwable
                ) {
                    view.onStoriesError("네트워크 오류: ${t.message}")
                }
            })
    }

    //동행 신청
    fun registerSegment(segmentId: Long) {
        val rawToken = prefs.getString("accessToken", "") ?: ""
        val bearer = "Bearer $rawToken"

        api.registerSegment(bearer, segmentId)
            .enqueue(object : Callback<ApiResponse<EmptyResult>> {
                override fun onResponse(
                    call: Call<ApiResponse<EmptyResult>>,
                    response: RetrofitResponse<ApiResponse<EmptyResult>>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && body.isSuccess) {
                            view.onRegisterSuccess()
                        } else {
                            view.onRegisterFailure(body?.message ?: "동행 신청에 실패했습니다.")
                        }
                    } else {
                        view.onRegisterFailure("HTTP 에러: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<EmptyResult>>,
                    t: Throwable
                ) {
                    view.onRegisterFailure("네트워크 오류: ${t.message}")
                }
            })
    }


    //코스 상세 정보
    fun fetchCourseDetail(courseId: Long) {
        val rawToken = prefs.getString("accessToken", "") ?: ""
        val bearer = "Bearer $rawToken"

        api.getCourseDetail(bearer, courseId)
            .enqueue(object : Callback<ApiResponse<CourseDetailResult>> {
                override fun onResponse(
                    call: Call<ApiResponse<CourseDetailResult>>,
                    response: RetrofitResponse<ApiResponse<CourseDetailResult>>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && body.isSuccess && body.result != null) {
                            view.onCourseDetailLoaded(body.result)
                        } else {
                            view.onCourseDetailError(body?.message ?: "상세 조회 실패")
                        }
                    } else {
                        view.onCourseDetailError("HTTP 에러: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<CourseDetailResult>>,
                    t: Throwable
                ) {
                    view.onCourseDetailError("네트워크 오류: ${t.message}")
                }
            })
    }

    //지역 검색
    /**
     * region 으로 검색해서 result.foundCourses 를 뽑아 StoryView 에 전달
     */
    fun searchRegion(region: String) {
        val rawToken = prefs.getString("accessToken", "") ?: ""
        val bearer = "Bearer $rawToken"

        api.searchByRegion(bearer, region)
            .enqueue(object : Callback<ApiResponse<FoundCoursesResponse>> {
                override fun onResponse(
                    call: Call<ApiResponse<FoundCoursesResponse>>,
                    response: RetrofitResponse<ApiResponse<FoundCoursesResponse>>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && body.isSuccess && body.result != null) {
                            view.onSearchResultsLoaded(body.result.foundCourses)
                        } else {
                            view.onSearchError(body?.message ?: "검색 결과가 비어있습니다.")
                        }
                    } else {
                        view.onSearchError("HTTP 에러: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<FoundCoursesResponse>>,
                    t: Throwable
                ) {
                    view.onSearchError("네트워크 오류: ${t.message}")
                }
            })
    }

    /**
     * 내 여행 코스 목록을 가져옵니다.
     */
    fun fetchMyCourses() {
        val rawToken = prefs.getString("accessToken", "") ?: ""
        val bearer = "Bearer $rawToken"

        api.getMyCourses(bearer)
            .enqueue(object : Callback<ApiResponse<MyCoursesResponse>> {
                override fun onResponse(
                    call: Call<ApiResponse<MyCoursesResponse>>,
                    response: RetrofitResponse<ApiResponse<MyCoursesResponse>>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && body.isSuccess && body.result != null) {
                            view.onMyCoursesLoaded(body.result.myCourses)
                        } else {
                            view.onMyCoursesError(body?.message ?: "내 코스 조회에 실패했습니다.")
                        }
                    } else {
                        view.onMyCoursesError("HTTP 에러: ${response.code()}")
                    }
                }
                override fun onFailure(
                    call: Call<ApiResponse<MyCoursesResponse>>,
                    t: Throwable
                ) {
                    view.onMyCoursesError("네트워크 오류: ${t.message}")
                }
            })
    }



}
