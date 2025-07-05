package com.example.soa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.soa.databinding.FragmentScheduleBinding

class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private var isDescOpened = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(layoutInflater, container, false)

        initClickListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSubmit()
    }

    private fun initClickListener() {
        binding.includeMakeCourses.apply {
            btnCoursesMore.setOnClickListener {
                if (isDescOpened == false) {
                    rotateLeftAnimation()
                    binding.includeMakeCourses.layoutCoursesTravelDesc.visibility = View.VISIBLE
                    isDescOpened = true
                } else {
                    rotateRightAnimation()
                    binding.includeMakeCourses.layoutCoursesTravelDesc.visibility = View.GONE
                    isDescOpened = false
                }
            }
            icCoursesCalendar.setOnClickListener {
                Log.d("FLOW", "CalendarClick")
                showCalendarDialog()
            }
        }
    }

    private fun showCalendarDialog() {
        Log.d("FLOW:ScheduleFragment", "CalendarDialog")
//        CalendarDialogFragment().show(childFragmentManager, "Calendar_Dialog")
        CalendarDialogFragment().show(parentFragmentManager, "calendar")
    }

    private fun rotateLeftAnimation(){
        val angle = -90f
        binding.includeMakeCourses.btnCoursesMore.animate()
            .rotation(angle)
            .setDuration(500)
            .start()
    }

    private fun rotateRightAnimation(){
        val angle = 0f
        binding.includeMakeCourses.btnCoursesMore.animate()
            .rotation(angle)
            .setDuration(500)
            .start()
    }

    fun onSubmit() {
        parentFragmentManager.setFragmentResultListener("calendarResult", viewLifecycleOwner) { _, bundle ->
            val startDate = bundle.getString("startDate")
            val endDate = bundle.getString("endDate")

            val startMD = DateStringToListHelper(startDate.toString())
            val endMD = DateStringToListHelper(endDate.toString())

            binding.includeMakeCourses.txtCoursesTravelPeriod.text = String.format("%s/%s ~ %s/%s", startMD[1], startMD[2], endMD[1], endMD[2])
        }
    }

    private fun DateStringToListHelper(date: String): ArrayList<Int> {
        var ymdList = arrayListOf<Int>()

        val onlyDate = date.toString().substring(12, date.toString().lastIndex)
        val ymd = onlyDate.split("-")

        for (i in 0..2) {
            ymdList.add(ymd[i].toInt())
        }

        return ymdList
    }
}