package com.example.soa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soa.adapter.HomeStoryAdapter
import com.example.soa.data.TravelStory
import com.example.soa.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeStoryAdapter: HomeStoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHomeStoryRecyclerView()
    }

    private fun setupHomeStoryRecyclerView() {
        val storyList = listOf(
            TravelStory(1, R.drawable.ic_story_user, R.drawable.img_demo, "소연"),
            TravelStory(2, R.drawable.ic_story_user, R.drawable.img_demo, "지우"),
            TravelStory(3, R.drawable.ic_story_user, R.drawable.img_demo, "하람"),
            TravelStory(3, R.drawable.ic_story_user, R.drawable.img_demo, "지현")
        )

        homeStoryAdapter = HomeStoryAdapter(storyList)
        binding.rvTravelStory.apply {
            adapter = homeStoryAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}