package com.example.soa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.soa.databinding.FragmentSignUpInfoBinding
import java.util.Vector

class SignUpInfoFragment : Fragment(R.layout.fragment_sign_up_info) {
    private var _binding: FragmentSignUpInfoBinding? = null
    private val binding get() = _binding!!

    // 이 프로퍼티가 layout_signup.xml 전체 바인딩
    private val signupBinding get() = binding.itemSignupInfo

    //들어가는 정보들
    private var name: String = ""
    private var tel: String = ""
    private var birth: String = ""
    private var gender: String = ""
    private var jangae : Vector<String> = Vector()
    private var id: String = ""
    private var nickname : String = ""
    private var password : String = ""
    private var passwordCheck : String = ""



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSignUpInfoBinding.bind(view)
        updateStepUI()

        binding.btnNextSignupInfo.setOnClickListener {

            if (signupBinding.includeBasicInfo.root.isVisible) {
                // 1단계 값 읽기
                val name   = signupBinding.includeBasicInfo.etBasicInfoName.text.toString()
                val tel    = signupBinding.includeBasicInfo.etBasicInfoTel.text.toString()
                val birth  = signupBinding.includeBasicInfo.etBasicInfoBirth.text.toString()
                val gender = if (signupBinding.includeBasicInfo.btnBasicInfoMale.isSelected) "남성" else "여성"

                // TODO: ViewModel 등에 저장…

                // 화면 전환
                signupBinding.includeBasicInfo.root.visibility = View.GONE
                signupBinding.includeIdPassword.root.visibility = View.VISIBLE
                signupBinding.includeIdentify.root.visibility = View.GONE

            } else if(signupBinding.includeIdPassword.root.isVisible) {
                // 2단계 값 읽기
                val nick   = signupBinding.includeIdPassword.etSignupNickname.text.toString()
                val userId = signupBinding.includeIdPassword.etSignupId.text.toString()
                val pwd    = signupBinding.includeIdPassword.etSignupPassword.text.toString()
                val chk    = signupBinding.includeIdPassword.etSignupPasswordCheck.text.toString()

                // TODO: ViewModel 등에 저장…

                // 화면 전환 (원한다면 뒤로 돌아가기도)
                signupBinding.includeBasicInfo.root.visibility = View.GONE
                signupBinding.includeIdPassword.root.visibility = View.GONE
                signupBinding.includeIdentify.root.visibility = View.VISIBLE
            }
            else if(signupBinding.includeIdentify.root.isVisible){

                findNavController().navigate(R.id.action_signupInfoFragment_to_signupFinishFragment)



            }

            updateStepUI()
        }
    }


    private fun handleBasicInfo(){

        //성별 버튼
        signupBinding.includeBasicInfo.btnBasicInfoMale.setOnClickListener {

        }
        signupBinding.includeBasicInfo.btnBasicInfoFemale.setOnClickListener {

        }

    }


    private fun updateStepUI() {
        if (signupBinding.includeBasicInfo.root.isVisible) {
            signupBinding.tvSignupNumber.text = "01"
            signupBinding.tvSignupTitle.text  = "기본정보입력"
        } else if(signupBinding.includeIdPassword.root.isVisible) {
            signupBinding.tvSignupNumber.text = "02"
            signupBinding.tvSignupTitle.text  = "닉네임·계정정보"
        } else if(signupBinding.includeIdentify.root.isVisible){
            signupBinding.tvSignupNumber.text = "03"
            signupBinding.tvSignupTitle.text  = "신원인증"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

