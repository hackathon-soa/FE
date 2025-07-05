package com.example.soa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.soa.R
import com.example.soa.data.TravelCourse
import com.example.soa.databinding.ItemSearchResultBinding

class SearchCourseAdapter(
    private val items: List<TravelCourse>
) : RecyclerView.Adapter<SearchCourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TravelCourse) {
            binding.tvCourseTitle.text = item.title
            binding.ivCourseProfile.setImageResource(item.profileImgRes)
            binding.tvCourseNickname.text = item.nickname
            binding.tvCourseDisability.text = item.disability
            binding.tvCourseLocation.text = item.location
            binding.tvCourseSchedule.text = item.schedule
            binding.tvCourseGender.text = item.gender
            binding.ivCourseLike.setImageResource(
                if (item.like) R.drawable.ic_heart_active else R.drawable.ic_heart_inactive
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
