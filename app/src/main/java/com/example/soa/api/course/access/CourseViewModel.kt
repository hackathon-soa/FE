package com.example.soa.api.course.access

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soa.api.course.model.MyCourse
import com.example.soa.api.course.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CourseViewModel(
    private val repository: CourseRepository
) : ViewModel() {

    private val _myCourses = MutableStateFlow<List<MyCourse>>(emptyList())
    val myCourses: StateFlow<List<MyCourse>> = _myCourses

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadMyCourses(token: String) {
        Log.d("CourseViewModel", "loadMyCourses() 호출됨")
        viewModelScope.launch {
            val result = repository.fetchMyCourses(token)
            result
                .onSuccess { courses ->
                    Log.d("CourseViewModel", "성공적으로 받아옴: ${courses.size}개")
                    _myCourses.value = courses
                }
                .onFailure { e ->
                    Log.e("CourseViewModel", "에러 발생: ${e.message}")
                    _error.value = e.message
                }
        }
    }

}

