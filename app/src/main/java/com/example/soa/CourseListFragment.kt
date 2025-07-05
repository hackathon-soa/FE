package com.example.soa

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soa.adapter.ApplicantAdapter
import com.example.soa.adapter.ScheduleAdapter
import com.example.soa.data.Applicant
import com.example.soa.data.ScheduleItem
import com.example.soa.databinding.DialogApplicantsBinding
import com.example.soa.databinding.FragmentCourseListBinding

class CourseListFragment : Fragment() {

    private var _binding: FragmentCourseListBinding? = null
    private val binding get() = _binding!!

    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scheduleItems = generateScheduleList()

        scheduleAdapter = ScheduleAdapter(scheduleItems) { entry ->
            showApplicantsDialog()
        }
        binding.rvSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scheduleAdapter
        }
    }

    private fun generateScheduleList(): List<ScheduleItem> {
        return listOf(
            ScheduleItem.DayHeader("Day 1", "1.2/월"),
            ScheduleItem.ScheduleEntry("카페거리", "13:00 ~ 15:00", "동행신청"),
            ScheduleItem.WalkInfo("1km", "도보 10분"),
            ScheduleItem.ScheduleEntry("호수공원", "15:10 ~ 18:00", "신청완료")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showApplicantsDialog() {
        val binding = DialogApplicantsBinding.inflate(layoutInflater)

        val dummyApplicants = listOf(
            Applicant("네모지", "청각장애", 64, "010-xxxx-xxxx", "여", 22, "12시간", "2000p"),
            Applicant("세모지", "청각장애", 36, "010-xxxx-xxxx", "남", 25, "4시간", "800p")
        )

        val adapter = ApplicantAdapter(dummyApplicants)
        binding.rvApplicant.adapter = adapter
        binding.rvApplicant.layoutManager = LinearLayoutManager(requireContext())

        AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
            .show()
    }
}
