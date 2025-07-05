package com.example.soa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soa.adapter.SearchCourseAdapter
import com.example.soa.data.TravelCourse
import com.example.soa.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var courseAdapter: SearchCourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 좋아요 없는 상태 기본 데이터
        val dummyList = listOf(
            TravelCourse(
                title = "3박 4일 제주도",
                location = "제주",
                schedule = "8월 3일 ~ 8월 6일",
                gender = "여성",
                nickname = "하랑별",
                disability = "청각장애",
                profileImgRes = R.drawable.ic_story_user,
                like = false
            ),
            TravelCourse(
                title = "2박 3일 부산 여행",
                location = "부산",
                schedule = "9월 1일 ~ 9월 3일",
                gender = "남성",
                nickname = "파도소리",
                disability = "지체장애",
                profileImgRes = R.drawable.ic_story_user,
                like = true
            ),
            TravelCourse(
                title = "2박 3일 부산 여행",
                location = "부산",
                schedule = "9월 1일 ~ 9월 3일",
                gender = "남성",
                nickname = "파도소리",
                disability = "지체장애",
                profileImgRes = R.drawable.ic_story_user,
                like = true
            )
        )

        courseAdapter = SearchCourseAdapter(dummyList) { clickedItem ->
            findNavController().navigate(R.id.action_searchFragment_to_courseDetailFragment)
        }
        binding.rvSearchResult.apply {
            adapter = courseAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.ivHomeSearch.setOnClickListener {
            binding.tvSearchResult.visibility = View.VISIBLE
            binding.tvSearchResultDescription.visibility = View.VISIBLE
            binding.rvSearchResult.visibility = View.VISIBLE
        }

        binding.icSearchBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
