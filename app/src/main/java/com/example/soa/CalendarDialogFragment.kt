package com.example.soa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.soa.databinding.FragmentCalendarDialogBinding

/**
 * @param type 의류 종류
 */
class CalendarDialogFragment() : DialogFragment() {
    // 이벤트 처리를 전달하기 위한 인터페이스에 대한 객체
    internal lateinit var dlistener: DialogListener
    lateinit var binding: FragmentCalendarDialogBinding

    /**
     * 호출부에서의 이벤트 처리를 위해서 필요한 리스너 인터페이스
     */
    // Dialog 객체를 생성하는 Activity에서 이벤트 콜백을 받기 위해서는 해당 인터페이스를 구현해야함.
    interface DialogListener {
        fun onSubmit(start: String, end: String)
    }

    /**
     * 리스너 객체 생성
     */
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        Log.d("FLOW", "onAttach")
//
//        try {
//            // 호출부에 이벤트를 보낼 수 있도록 리스너 객체를 생성
//            dlistener = context as DialogListener
//        } catch (e: ClassCastException) {
//            // Activity가 인터페이스를 구체화하지 않으면 에러 throw
//            throw ClassCastException((context.toString() + "must implement DialogListener"))
//        }
//    }

    /**
     * Dialog 구현
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarDialogBinding.inflate(layoutInflater, container, false)

        binding.viewScheduleCalendar.setOnRangeSelectedListener { widget, dates ->
            val startDate = dates.firstOrNull()?.toString()
            val endDate = dates.lastOrNull()?.toString()

            val result = Bundle().apply {
                putString("startDate", startDate.toString())
                putString("endDate", endDate.toString())
            }

            // ✅ FragmentManager를 통해 결과 전달
            parentFragmentManager.setFragmentResult("calendarResult", result)
            dismiss()

            Log.d("Schedule", "start: ${startDate}, end: ${endDate}")
        }

        return binding.root
    }
}