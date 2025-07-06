package com.example.soa.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

//각 API 서비스를 제공하는 객체

interface ApiService {
    //notation으로 정의된 거 작성(회원가입은 POST)

    @POST("api/auth/signup")
    fun signUP(@Body req: SignUpRequest): Call<SignUpResponse>

    @Multipart
    @POST("api/auth/upload")
    fun uploadImage(
        @Query("memberId") memberId: Long,
        @Part file: MultipartBody.Part
    ) : Call<ImageUploadResponse>

    @POST("api/auth/login")
    fun login(@Body req: LoginRequest): Call<LoginResponse>

    @GET("api/auth/check")
    fun idcheck(@Query("appId") id: String): Call<IdCheckResponse>

    ////스토리 가져오기
    @GET("api/stories")
    fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Call<Response<PageResult<StoryItem>>>

    @POST("api/participation/segments/{segmentId}/register")
    fun registerSegment(
        @Header("Authorization") token: String,
        @Path("segmentId") segmentId: Long
    ): Call<Response<EmptyResult>>


    /**
     * courseId 로 해당 코스의 세그먼트 리스트를 포함한 상세 정보를 조회
     */
    @GET("api/segment/{courseId}")
    fun getCourseDetail(
        @Header("Authorization") token: String,
        @Path("courseId") courseId: Long
    ): Call<Response<CourseDetailResult>>


    /**
     * region 키워드로 /api/search/region 호출
     * -> ApiResponse<FoundCoursesResponse> 리턴
     */
    @GET("api/search/region")
    fun searchByRegion(
        @Header("Authorization") token: String,
        @Query("region") region: String
    ): Call<Response<FoundCoursesResponse>>


    /**
     * 현재 로그인한 사용자가 만든 코스 목록 조회
     */
    @GET("api/home/my-courses")
    fun getMyCourses(
        @Header("Authorization") token: String
    ): Call<Response<MyCoursesResponse>>


    /** 관광 코스 등록하기 */
    @POST("courses")
    fun createCourse(
        @Header("Authorization") token: String,
        @Body body: CourseCreateRequest
    ): Call<CreateCourseResponse>

}