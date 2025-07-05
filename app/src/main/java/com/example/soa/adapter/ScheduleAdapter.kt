package com.example.soa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.soa.data.ScheduleItem
import com.example.soa.databinding.ViewDayHeaderBinding
import com.example.soa.databinding.ViewScheduleEntryBinding
import com.example.soa.databinding.ViewWalkInfoBinding

class ScheduleAdapter(private val items: List<ScheduleItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is ScheduleItem.DayHeader -> 0
        is ScheduleItem.ScheduleEntry -> 1
        is ScheduleItem.WalkInfo -> 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                val binding = ViewDayHeaderBinding.inflate(inflater, parent, false)
                DayHeaderViewHolder(binding)
            }
            1 -> {
                val binding = ViewScheduleEntryBinding.inflate(inflater, parent, false)
                ScheduleViewHolder(binding)
            }
            else -> {
                val binding = ViewWalkInfoBinding.inflate(inflater, parent, false)
                WalkInfoViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ScheduleItem.DayHeader -> (holder as DayHeaderViewHolder).bind(item)
            is ScheduleItem.ScheduleEntry -> (holder as ScheduleViewHolder).bind(item)
            is ScheduleItem.WalkInfo -> (holder as WalkInfoViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class DayHeaderViewHolder(private val binding: ViewDayHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScheduleItem.DayHeader) {
            binding.tvDayLabel.text = item.day      // "Day 1"
            binding.tvDateLabel.text = item.date    // "12.31/토"
        }
    }

    class ScheduleViewHolder(private val binding: ViewScheduleEntryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScheduleItem.ScheduleEntry) {
            binding.tvPlace.text = item.place
            binding.tvTime.text = item.time
            binding.tvStatus.text = item.status

            binding.btnStatus.setOnClickListener {
                // Todo : 상태 변환
            }

        }
    }

    class WalkInfoViewHolder(private val binding: ViewWalkInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScheduleItem.WalkInfo) {
            binding.tvDistance.text = item.distance ?: ""
            binding.tvWalkTime.text = item.walkTime ?: ""
        }
    }
}