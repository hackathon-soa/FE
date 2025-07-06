package com.example.soa.api.course.network

import com.example.soa.api.course.model.MyCourseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface CourseService {
    @GET("api/home/my-courses/two")
    suspend fun getMyCourses(
        @Header("Authorization") token: String
    ): Response<MyCourseResponse>
}
