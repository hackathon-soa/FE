package com.example.soa

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.soa.databinding.ItemScheduleWithPlaceBinding

class ScheduleRVAdapter(private val scheduleList: ArrayList<Schedule>) : RecyclerView.Adapter<ScheduleRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ScheduleRVAdapter.ViewHolder {
        val binding: ItemScheduleWithPlaceBinding = ItemScheduleWithPlaceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleRVAdapter.ViewHolder, position: Int) {
        holder.bind(scheduleList[position])

        /** ViewHolder item view 클릭 이벤트 처리 */
//        holder.apply {
//            // item Update
//            binding.itemStackUpdateButton.setOnClickListener {
//                Log.d("FLOW:StackAdapter", "itemUpdate")
//            }
//
//            // item Delete
//            binding.itemStackDeleteButton.setOnClickListener{
//                Log.d("FLOW:StackAdapter", "itemDelete")
//                inAdapterListener.onRemoveData(position)
//                removeItem(position)
//            }
//        }
    }

    override fun getItemCount(): Int {
        Log.d("FLOW:StackRVAdapter", scheduleList.size.toString())
        return scheduleList.size
    }

    inner class ViewHolder(val binding: ItemScheduleWithPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(schedule: Schedule) {

            val year = schedule.date[0]
            val month = schedule.date[1]
            val weekday = schedule.weekday
        }
    }
}