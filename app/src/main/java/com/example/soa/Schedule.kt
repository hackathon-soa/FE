package com.example.soa

data class Schedule(
    var place: String?,
    var mobility: String?,
    var time: String?,
    var date: List<Int>?,
    var weekday: String?
)