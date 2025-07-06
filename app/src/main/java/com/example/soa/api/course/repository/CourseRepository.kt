package com.example.soa.api.course.repository

import android.util.Log
import com.example.soa.api.course.model.MyCourse
import com.example.soa.api.course.network.CourseService

class CourseRepository(private val service: CourseService) {
    suspend fun fetchMyCourses(token: String): Result<List<MyCourse>> {
        return try {
            val response = service.getMyCourses(token)
            Log.d("CourseRepository", "response code: ${response.code()}")
            Log.d("CourseRepository", "response body: ${response.body()}")

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                Result.success(response.body()!!.result.myCourses)
            } else {
                Log.e("CourseRepository", "API 실패: ${response.errorBody()?.string()}")
                Result.failure(Exception(response.body()?.message ?: "API 실패"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
