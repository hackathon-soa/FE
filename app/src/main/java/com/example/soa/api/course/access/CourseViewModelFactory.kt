package com.example.soa.api.course.access

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.soa.api.course.repository.CourseRepository

class CourseViewModelFactory(
    private val repository: CourseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CourseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}