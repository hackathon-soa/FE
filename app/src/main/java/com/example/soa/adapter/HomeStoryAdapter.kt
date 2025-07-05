package com.example.soa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.soa.data.TravelStory
import com.example.soa.databinding.ItemHomeStoryBinding

class HomeStoryAdapter(
    private val items: List<TravelStory>
) : RecyclerView.Adapter<HomeStoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemHomeStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TravelStory) {
            binding.ivHomeStory.setImageResource(item.storyImg)
            binding.ivHomeStoryUser.setImageResource(item.profileImg)
            binding.tvHomeStoryUser.text = item.nickname
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
