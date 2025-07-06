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
import com.example.soa.data.TravelCourse
import com.example.soa.data.TravelStory
import com.example.soa.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
