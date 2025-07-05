package com.example.soa

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soa.adapter.HomeCourseAdapter
import com.example.soa.adapter.HomeStoryAdapter
import com.example.soa.data.TravelCourse
import com.example.soa.data.TravelStory
import com.example.soa.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: SharedPreferences
    private lateinit var accessToken: String

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

        prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        accessToken = prefs.getString("accessToken", "") ?: ""
        Log.d("test", accessToken)

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

        val courseList = listOf(
            TravelCourse(
                title = "3박 4일 대구 일정",
                location = "대구",
                schedule = "3 / 1 ~ 3 / 4",
                gender = "여",
                nickname = "하랑별",
                disability = "청각장애",
                profileImgRes = R.drawable.ic_story_user
            ),
            TravelCourse(
                title = "2박 3일 전주 여행",
                location = "전주",
                schedule = "2 / 20 ~ 2 / 22",
                gender = "남",
                nickname = "별이",
                disability = "지체장애",
                profileImgRes = R.drawable.ic_story_user
            )
        )

        val adapter = HomeCourseAdapter(courseList) {
            findNavController().navigate(R.id.action_homeFragment_to_courseDetailFragment)
        }
        binding.rvHomeCourse.adapter = adapter

        binding.rvHomeCourse.adapter = adapter
        binding.rvHomeCourse.layoutManager = LinearLayoutManager(requireContext())

        binding.etHomeSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}