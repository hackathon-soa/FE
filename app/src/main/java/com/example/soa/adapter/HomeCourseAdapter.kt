package com.example.soa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.soa.data.TravelCourse
import com.example.soa.databinding.ItemHomeCourseBinding

class HomeCourseAdapter(
    courses: List<TravelCourse>
) : RecyclerView.Adapter<HomeCourseAdapter.ViewHolder>() {

    private val items = courses.take(2) // 최대 2개까지만 표시

    inner class ViewHolder(private val binding: ItemHomeCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: TravelCourse) {
            binding.tvCourseTitle.text = course.title
            binding.tvCourseLocation.text = course.location
            binding.tvCourseSchedule.text = course.schedule
            binding.tvCourseGender.text = course.gender
            binding.tvCourseNickname.text = course.nickname
            binding.tvCourseDisability.text = course.disability
            binding.ivCourseProfile.setImageResource(course.profileImgRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeCourseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
