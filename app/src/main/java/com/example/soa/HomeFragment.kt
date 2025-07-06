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
import com.example.soa.api.CourseDetailResult
import com.example.soa.api.FoundCourse
import com.example.soa.api.MyCourse
import com.example.soa.api.StoryItem
import com.example.soa.api.StoryService
import com.example.soa.api.StoryView
import com.example.soa.data.TravelCourse
import com.example.soa.data.TravelStory
import com.example.soa.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), StoryView {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var storyService: StoryService

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

        storyService = StoryService(this, requireContext())
        testAPI()
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
            findNavController().navigate(R.id.action_homeFragment_to_courseListFragment)
        }
        binding.rvHomeCourse.adapter = adapter

        binding.rvHomeCourse.adapter = adapter
        binding.rvHomeCourse.layoutManager = LinearLayoutManager(requireContext())

        binding.etHomeSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun testAPI() {

        //1. 스토리 호출
        storyService.fetchStories(0, 1)

        // 1) 스토리 목록
        storyService.fetchStories(page = 0, size = 10)

        // 2) 동행 신청 (segmentId 예시로 1L)
        storyService.registerSegment(segmentId = 1L)

        // 3) 코스 상세 조회 (courseId 예시로 1L)
        storyService.fetchCourseDetail(courseId = 1L)

        // 4) 지역 검색 (region 예시로 "서울")
        storyService.searchRegion(region = "서울")

        // 5) 내 코스 조회
        storyService.fetchMyCourses()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStoriesLoaded(stories: List<StoryItem>) {
        Log.d("HomeFragment", "✅ 스토리 ${stories.size}개 불러옴")
        stories.forEach { item ->
            Log.d("HomeFragment", "${item.nickname} → ${item.imageUrl}")
        }
    }

    override fun onStoriesError(errorMsg: String) {
        Log.d("HomeFragment", "❌ 스토리 로드 실패: $errorMsg")
    }

    override fun onRegisterSuccess() {
        Log.d("HomeFragment", "✅ 동행 신청 성공")
    }

    override fun onRegisterFailure(errorMsg: String) {
        Log.d("HomeFragment", "❌ 동행 신청 실패: $errorMsg")
    }

    override fun onCourseDetailLoaded(detail: CourseDetailResult) {
        Log.d("HomeFragment", "✅ 코스 상세 로드: ${detail.segments.size} 세그먼트")
    }

    override fun onCourseDetailError(errorMsg: String) {
        Log.d("HomeFragment", "❌ 코스 상세 에러: $errorMsg")
    }

    override fun onSearchResultsLoaded(results: List<FoundCourse>) {
        Log.d("HomeFragment", "✅ 검색 결과 ${results.size}개")
    }

    override fun onSearchError(errorMsg: String) {
        Log.d("HomeFragment", "❌ 검색 에러: $errorMsg")
    }

    override fun onMyCoursesLoaded(courses: List<MyCourse>) {
        Log.d("HomeFragment", "✅ 내 코스 ${courses.size}개")
    }

    override fun onMyCoursesError(errorMsg: String) {
        Log.d("HomeFragment", "❌ 내 코스 조회 실패: $errorMsg")
    }

    override fun onCourseCreated() {

    }

    override fun onCourseCreateError(errorMsg: String) {

    }
}