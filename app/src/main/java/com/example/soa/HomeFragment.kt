package com.example.soa

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soa.adapter.HomeCourseAdapter
import com.example.soa.adapter.HomeStoryAdapter
import com.example.soa.api.NetworkManager
import com.example.soa.api.course.access.CourseViewModel
import com.example.soa.api.course.access.CourseViewModelFactory
import com.example.soa.api.course.model.MyCourse
import com.example.soa.api.course.repository.CourseRepository
import com.example.soa.api.CourseDetailResult
import com.example.soa.api.FoundCourse
import com.example.soa.api.MyCourse
import com.example.soa.api.StoryItem
import com.example.soa.api.StoryService
import com.example.soa.api.StoryView
import com.example.soa.data.TravelCourse
import com.example.soa.data.TravelStory
import com.example.soa.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), StoryView {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var storyService: StoryService

    private lateinit var prefs: SharedPreferences
    private lateinit var accessToken: String

    private val repository by lazy { CourseRepository(NetworkManager.courseService) }
    private val viewModel by lazy {
        ViewModelProvider(this, CourseViewModelFactory(repository))[CourseViewModel::class.java]
    }

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

        storyService = StoryService(this, requireContext())
        testAPI()
        setupHomeStoryRecyclerView()
        setupSearchClick()
        fetchMyCourses()
    }

    private fun setupHomeStoryRecyclerView() {
        val storyList = listOf(
            TravelStory(1, R.drawable.ic_story_user, R.drawable.img_demo, "소연"),
            TravelStory(2, R.drawable.ic_story_user, R.drawable.img_demo, "지우"),
            TravelStory(3, R.drawable.ic_story_user, R.drawable.img_demo, "하람"),
            TravelStory(4, R.drawable.ic_story_user, R.drawable.img_demo, "지현")
        )

        homeStoryAdapter = HomeStoryAdapter(storyList)
        binding.rvTravelStory.apply {
            adapter = homeStoryAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun fetchMyCourses() {
        // 서버 데이터 가져오기
        viewModel.loadMyCourses("Bearer $accessToken")

        // 데이터 수집해서 UI에 반영
        lifecycleScope.launch {
            viewModel.myCourses.collect { myCourses ->
                Log.d("HomeFragment", "서버에서 가져온 myCourses: $myCourses")

                if (myCourses.isNotEmpty()) {
                    val travelCourseList = mapToTravelCourseList(myCourses)
                    val adapter = HomeCourseAdapter(travelCourseList) {
                        findNavController().navigate(R.id.action_homeFragment_to_courseListFragment)
                    }

                    binding.rvHomeCourse.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        this.adapter = adapter
                    }
                } else {
                    Log.d("HomeFragment", "받은 코스 데이터가 없습니다.")
                }
            }
        }

        // 에러 처리
        lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Log.e("HomeFragment", "에러 발생: $it")
                }
            }
        }
    }


    private fun setupSearchClick() {
        binding.etHomeSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun mapToTravelCourseList(myCourses: List<MyCourse>): List<TravelCourse> {
        return myCourses.map {
            TravelCourse(
                title = "${it.startDate} ~ ${it.endDate} ${it.region} 일정",
                location = it.region,
                schedule = "${formatDate(it.startDate)} ~ ${formatDate(it.endDate)}",
                gender = if (it.gender == "MALE") "남" else "여",
                nickname = it.userName,
                disability = it.disabilityType
                    .replace("[", "")
                    .replace("]", "")
                    .replace("\"", "")
                    .replace(",", ", "),
                profileImgRes = R.drawable.ic_story_user,
                like = false
            )
        }.take(2) // 최대 2개만 표시
    }

    private fun formatDate(date: String): String {
        return try {
            val parts = date.split("-")
            "${parts[1].toInt()} / ${parts[2].toInt()}"
        } catch (e: Exception) {
            date
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
}

    override fun onStoriesLoaded(stories: List<StoryItem>) {
        Log.d("HomeFragment", "스토리 ${stories.size}개 불러옴")
        stories.forEach { item ->
            Log.d("HomeFragment", "${item.nickname} → ${item.imageUrl}")
        }
    }

    override fun onStoriesError(errorMsg: String) {
        Log.d("HomeFragment", "스토리 로드 실패: $errorMsg")
    }

    override fun onRegisterSuccess() {
        Log.d("HomeFragment", "동행 신청 성공")
    }

    override fun onRegisterFailure(errorMsg: String) {
        Log.d("HomeFragment", "동행 신청 실패: $errorMsg")
    }

    override fun onCourseDetailLoaded(detail: CourseDetailResult) {
        Log.d("HomeFragment", "코스 상세 로드: ${detail.segments.size} 세그먼트")
    }

    override fun onCourseDetailError(errorMsg: String) {
        Log.d("HomeFragment", "코스 상세 에러: $errorMsg")
    }

    override fun onSearchResultsLoaded(results: List<FoundCourse>) {
        Log.d("HomeFragment", "검색 결과 ${results.size}개")
    }

    override fun onSearchError(errorMsg: String) {
        Log.d("HomeFragment", "검색 에러: $errorMsg")
    }

    override fun onMyCoursesLoaded(courses: List<MyCourse>) {
        Log.d("HomeFragment", "내 코스 ${courses.size}개")
    }

    override fun onMyCoursesError(errorMsg: String) {
        Log.d("HomeFragment", "내 코스 조회 실패: $errorMsg")
    }

    override fun onCourseCreated() {

    }

    override fun onCourseCreateError(errorMsg: String) {

    }
}

