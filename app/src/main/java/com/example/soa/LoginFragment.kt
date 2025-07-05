package com.example.soa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.soa.api.AuthService
import com.example.soa.api.AuthView
import com.example.soa.api.IdCheckResult
import com.example.soa.api.LoginResult
import com.example.soa.api.SignUpResult
import com.example.soa.databinding.FragmentLoginBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment(), AuthView {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var authService : AuthService

    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //api
        authService = AuthService(this)

        binding.btnLoginLogin.setOnClickListener {
            validLogin()
        }

        binding.btnSignupLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupInfoFragment)
        }

        binding.layoutPassloginLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

    }

    private fun validLogin(){
        var id = binding.etIdLogin.text.toString()
        var pw = binding.etPwLogin.text.toString()

        //빈 경우 문제
        if(id.isEmpty() || pw.isEmpty()){
            binding.tvWarningLogin.visibility = View.VISIBLE
            binding.tvWarningLogin.text = "아이디와 비밀번호를 입력해주세요."
            return
        }
        
        //값을 보낸 경우 --> API 결과에 따라 통과 or 오류

        authService.login(id, pw)

        

    }


    override fun onSignUpSuccess(result: SignUpResult) {}
    override fun onSignUpFailure(errorMsg: String) {}
    override fun onLoginSuccess(result: LoginResult) {
        val accessToken = result.accessToken
        Log.d("test", accessToken)
    }

    override fun onLoginFailure(errorMsg: String) {
        Log.d("test", "꾸앙")
    }

    override fun onIdCheckSuccess(result: IdCheckResult) {

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}