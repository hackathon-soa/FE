package com.example.soa.data

sealed class ScheduleItem {
    data class DayHeader(
        val day: String,
        val date: String
    ) : ScheduleItem()
    data class ScheduleEntry(
        val place: String,
        val time: String,
        val status: String // "동행신청", "신청완료"
    ) : ScheduleItem()
    data class WalkInfo(
        val distance: String?,
        val walkTime: String?
    ) : ScheduleItem()
}
