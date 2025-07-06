package com.example.soa

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soa.databinding.FragmentScheduleBinding
import java.time.LocalDate

class ScheduleFragment : Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private var isDescOpened = true
    private var sex = ""
//    private var theme = arrayListOf<String>()
    private var theme = IntArray(7, {0})
    private var interest = ""
    private var significant = ""
    private lateinit var MD: Array<Int>
    private var scheduleList = arrayListOf<Schedule>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(layoutInflater, container, false)

        initClickListener()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSubmit()
    }

    override fun onResume() {
        super.onResume()

        val scheduleRVAdapter = ScheduleRVAdapter(scheduleList) // 데이터 리스트 연결
        scheduleRVAdapter.notifyDataSetChanged()
        binding.includeMakeCourses.rvCoursesSchedule.adapter = scheduleRVAdapter // RecyclerView에 Adapter 연결
        binding.includeMakeCourses.rvCoursesSchedule.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initClickListener() {
        binding.includeMakeCourses.apply {
            btnCoursesSexUni.setOnClickListener{
                bgBtnCoursesSexUni.setImageResource(R.drawable.bg_square_main_color_30)
                txtBtnCoursesSexUni.setTextColor(Color.WHITE)
                sex = "uni"
            }
            btnCoursesSexMan.setOnClickListener{
                bgBtnCoursesSexMan.setImageResource(R.drawable.bg_square_main_color_30)
                txtBtnCoursesSexMan.setTextColor(Color.WHITE)
                sex = "man"
            }
            btnCoursesSexWoman.setOnClickListener {
                bgBtnCoursesSexWoman.setImageResource(R.drawable.bg_square_main_color_30)
                txtBtnCoursesSexWoman.setTextColor(Color.WHITE)
                sex = "woman"
            }

            btnCoursesThemeActivity.setOnClickListener {
                bgBtnCoursesThemeActivity.setImageResource(R.drawable.bg_square_main_color_30)
                txtBtnCoursesThemeActivity.setTextColor(Color.WHITE)
//                theme.add("activity")
                theme[0] = 1
            }
            btnCoursesThemeFood.setOnClickListener {
                bgBtnCoursesThemeFood.setImageResource(R.drawable.bg_square_main_color_30)
                txtBtnCoursesThemeFood.setTextColor(Color.WHITE)
//                theme.add("food")
                theme[1] = 1
            }
            btnCoursesThemeFamous.setOnClickListener {
                bgBtnCoursesThemeFamous.setImageResource(R.drawable.bg_square_main_color_30)
                txtBtnCoursesThemeFamous.setTextColor(Color.WHITE)
//                theme.add("famous")
                theme[2] = 1
            }
            btnCoursesThemeNature.setOnClickListener {
                bgBtnCoursesThemeNature.setImageResource(R.drawable.bg_square_main_color_30)
                txtBtnCoursesThemeNature.setTextColor(Color.WHITE)
//                theme.add("nature")
                theme[3] = 1
            }
            btnCoursesThemeHealing.setOnClickListener {
                bgBtnCoursesThemeHealing.setImageResource(R.drawable.bg_square_main_color_30)
                txtBtnCoursesThemeHealing.setTextColor(Color.WHITE)
//                theme.add("healing")
                theme[4] = 1
            }
            btnCoursesThemeHotplace.setOnClickListener {
                bgBtnCoursesThemeHotplace.setImageResource(R.drawable.bg_square_main_color_30)
                txtBtnCoursesThemeHotplace.setTextColor(Color.WHITE)
//                theme.add("hot")
                theme[5] = 1
            }
            btnCoursesThemeShopping.setOnClickListener {
                bgBtnCoursesThemeShopping.setImageResource(R.drawable.bg_square_main_color_30)
                txtBtnCoursesThemeShopping.setTextColor(Color.WHITE)
//                theme.add("shopping")
                theme[6] = 1
            }

            btnScheduleFold.setOnClickListener {
                if (isDescOpened == false) {
                    rotateRightAnimation()
                    showAndHide(theme)
                    isDescOpened = true
                } else {
                    rotateLeftAnimation()
                    showAndHide(theme)
                    isDescOpened = false
                }
            }
            icCoursesCalendar.setOnClickListener {
                Log.d("FLOW", "CalendarClick")
                showCalendarDialog()
            }
        }
    }

    private fun showAndHide(arr: IntArray) {
        binding.includeMakeCourses.apply {
            if (isDescOpened) {
                for (i in 0..arr.size-1) {
                    if (arr[i] == 0) {
                        when (i) {
                            0 -> { btnCoursesThemeActivity.visibility = View.GONE }
                            1 -> { btnCoursesThemeFood.visibility = View.GONE }
                            2 -> { btnCoursesThemeFamous.visibility = View.GONE }
                            3 -> { btnCoursesThemeNature.visibility = View.GONE }
                            4 -> { btnCoursesThemeHealing.visibility = View.GONE }
                            5 -> { btnCoursesThemeHotplace.visibility = View.GONE }
                            6 -> { btnCoursesThemeShopping.visibility = View.GONE }
                        }
                    }
                }
            } else {
                btnCoursesThemeActivity.visibility = View.VISIBLE
                btnCoursesThemeFood.visibility = View.VISIBLE
                btnCoursesThemeFamous.visibility = View.VISIBLE
                btnCoursesThemeNature.visibility = View.VISIBLE
                btnCoursesThemeHealing.visibility = View.VISIBLE
                btnCoursesThemeHotplace.visibility = View.VISIBLE
                btnCoursesThemeShopping.visibility = View.VISIBLE
            }
        }

//            when (theme) {
//                "activity" -> { binding.includeMakeCourses.btnCoursesThemeActivity}
//                "food" -> {}
//                "famous" -> {}
//                "nature" -> {}
//                "healing" -> {}
//                "hot" -> {}
//                "shopping" -> {}
//            }
    }

    private fun showCalendarDialog() {
        Log.d("FLOW:ScheduleFragment", "CalendarDialog")
//        CalendarDialogFragment().show(childFragmentManager, "Calendar_Dialog")
        CalendarDialogFragment().show(parentFragmentManager, "calendar")
    }

    private fun rotateLeftAnimation(){
        val angle = -180f
        binding.includeMakeCourses.btnScheduleFold.animate()
            .rotation(angle)
            .setDuration(500)
            .start()
    }

    private fun rotateRightAnimation(){
        val angle = 0f
        binding.includeMakeCourses.btnScheduleFold.animate()
            .rotation(angle)
            .setDuration(500)
            .start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSubmit() {
        parentFragmentManager.setFragmentResultListener("calendarResult", viewLifecycleOwner) { _, bundle ->
            val startDate = bundle.getString("startDate")
            val endDate = bundle.getString("endDate")

            val startMD = DateStringToListHelper(startDate.toString())
            val endMD = DateStringToListHelper(endDate.toString())

            MD = arrayOf(startMD[0],startMD[1])

            val startLocalDate = LocalDate.of(startMD[0], startMD[1], startMD[2])
            val endLocalDate = LocalDate.of(endMD[0], endMD[1], endMD[2])
            val period = endLocalDate.toEpochDay() - startLocalDate.toEpochDay()
            Log.d("period", period.toString())

            scheduleList.clear()

            for (i in 0..period) {
                scheduleList.add(Schedule(null, null, null, MD.toList(), "weekday"))
            }

            binding.includeMakeCourses.txtCoursesTravelPeriod.text = String.format("%s/%s ~ %s/%s", startMD[1], startMD[2], endMD[1], endMD[2])

            onResume()
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