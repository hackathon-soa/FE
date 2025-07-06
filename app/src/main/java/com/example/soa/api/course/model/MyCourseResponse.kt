package com.example.soa.api.course.model

data class MyCourseResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: MyCourseResult
)

data class MyCourseResult(
    val myCourses: List<MyCourse>
)

data class MyCourse(
    val startDate: String,
    val endDate: String,
    val region: String,
    val gender: String,
    val userName: String,
    val disabilityType: String
)
