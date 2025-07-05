package com.example.soa.data

data class TravelCourse(
    val title: String,
    val location: String,
    val schedule: String,
    val gender: String,
    val nickname: String,
    val disability: String,
    val profileImgRes: Int,
    val like: Boolean = false
)
