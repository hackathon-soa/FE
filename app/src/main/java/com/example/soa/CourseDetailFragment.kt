package com.example.soa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soa.adapter.ScheduleAdapter
import com.example.soa.data.ScheduleItem
import com.example.soa.databinding.FragmentCourseDetailBinding

class CourseDetailFragment : Fragment() {

    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var scheduleAdapter: ScheduleAdapter
    private var isApplied = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCourseAttend.setOnClickListener {
            toggleCourseAttendButton()
        }

        binding.icCourseDetailBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val scheduleItems = generateDummySchedule()
        scheduleAdapter = ScheduleAdapter(scheduleItems)
        binding.rvSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scheduleAdapter
        }
    }

    private fun toggleCourseAttendButton() {
        isApplied = !isApplied

        if (isApplied) {
            binding.ivCourseAttend.setImageResource(R.drawable.bg_gray_400_fill_14)
            binding.tvCourseAttend.text = "신청 완료"
            binding.tvCourseAttend.setTextColor(
                ContextCompat.getColor(requireContext(), android.R.color.white)
            )
        } else {
            binding.ivCourseAttend.setImageResource(R.drawable.bg_square_white_grey_14)
            binding.tvCourseAttend.text = "전체 동행 신청"
            binding.tvCourseAttend.setTextColor(
                ContextCompat.getColor(requireContext(), android.R.color.black)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generateDummySchedule(): List<ScheduleItem> {
        return listOf(
            ScheduleItem.DayHeader("Day 1", "12.31/토"),
            ScheduleItem.ScheduleEntry("한옥마을", "14:00 ~ 18:00", "동행신청"),
            ScheduleItem.WalkInfo("500M", "도보 5분"),
            ScheduleItem.ScheduleEntry("기온정", "18:05 ~ 20:30", "신청완료"),
            ScheduleItem.DayHeader("Day 2", "1.1/일"),
            ScheduleItem.ScheduleEntry("조선한복", "10:00 ~ 12:00", "동행신청")
        )
    }
}
