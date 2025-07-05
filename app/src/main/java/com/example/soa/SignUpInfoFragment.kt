package com.example.soa

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.soa.api.AuthService
import com.example.soa.api.AuthView
import com.example.soa.api.IdCheckResult
import com.example.soa.api.LoginResult
import com.example.soa.api.SignUpResult
import com.example.soa.databinding.FragmentSignUpInfoBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Vector
import kotlin.math.sign

class SignUpInfoFragment : Fragment(R.layout.fragment_sign_up_info), AuthView {
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
    private var idCheck: Boolean = false
    private var pwCheck : Boolean = false
    private var nickname : String = ""
    private var password : String = ""
    private var passwordCheck : String = ""
    private var userId : Long = 0

    private val REQUEST_CAMERA = 1001
    private lateinit var currentPhotoPath: String

    private lateinit var authService: AuthService


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSignUpInfoBinding.bind(view)
        updateStepUI()

        authService = AuthService(this)

        binding.btnBackSignupInfo.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNextSignupInfo.setOnClickListener {

            if (signupBinding.includeBasicInfo.root.isVisible) {

                // 화면 전환
                if(checkBasicInfo()) {
                    signupBinding.includeBasicInfo.root.visibility = View.GONE
                    signupBinding.includeIdPassword.root.visibility = View.VISIBLE
                    signupBinding.includeIdentify.root.visibility = View.GONE
                }
                else{
                    Toast.makeText(requireContext(), "잘못된 정보가 있습니다.", Toast.LENGTH_SHORT).show()
                }

            } else if(signupBinding.includeIdPassword.root.isVisible) {

                if(!checkIdPassword()){
                    Toast.makeText(requireContext(), "잘못된 정보가 있습니다.", Toast.LENGTH_SHORT).show()

                }
                else {
                    // 화면 전환 (원한다면 뒤로 돌아가기도)
                    signupBinding.includeBasicInfo.root.visibility = View.GONE
                    signupBinding.includeIdPassword.root.visibility = View.GONE
                    signupBinding.includeIdentify.root.visibility = View.VISIBLE
                }
            }
            else if(signupBinding.includeIdentify.root.isVisible){



                findNavController().navigate(R.id.action_signupInfoFragment_to_signupFinishFragment)



            }

            updateStepUI()
        }
    }

    //API에게 모든 정보 전송
    private fun sendInfo(){
        Log.d("test", name)
        Log.d("test", tel)
        Log.d("test", birth)
        Log.d("test", gender)
        Log.d("test", jangae.toString())
        Log.d("test", id)
        Log.d("test", nickname)
        Log.d("test", password)

    }
    
    //3단계 갤러리에서 사진 정보 체크
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            // file.exists() 확인 후, 업로드 진행
            signupBinding.includeIdentify.imvPictureIdentify.setImageURI(Uri.fromFile(file))
            signupBinding.includeIdentify.tvPicturetextIdentify.visibility = View.GONE

            binding.btnNextSignupInfo.isEnabled = true
            binding.btnNextSignupInfo.backgroundTintList = resources.getColorStateList(R.color.main_color)

            // 2) MIME 타입에 맞춰 RequestBody 생성
            val requestBody = file
                .asRequestBody("image/jpg".toMediaTypeOrNull())

            // 3) MultipartBody.Part 생성 (서버가 요구하는 파트 이름이 "file" 이라면)
            val multipartPart = MultipartBody.Part.createFormData(
                name = "file",        // Swagger 에서 정의한 파트 키
                filename = file.name, // 보내는 파일의 이름
                body = requestBody
            )

            //API에 이미지 보내기
            sendInfo()
            authService.sendImage(userId, multipartPart)
        }
    }

    private fun getPicture(){
        val photoFile: File = createImageFile().also { currentPhotoPath = it.absolutePath }
        val photoURI: Uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photoFile
        )

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, REQUEST_CAMERA)
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* 파일명 접두사 */
            ".jpg",              /* 확장자 */
            storageDir           /* 디렉토리 */
        )
    }


    //2단계 회원정보입력 체크
    private fun handleIdPassword(){
        //중복확인 버튼 누를 때
        signupBinding.includeIdPassword.btnJungbokcheck.setOnClickListener {


            AuthService(this).idcheck(id)

        }

        //editText 추적
        val pwdEt    = signupBinding.includeIdPassword.etSignupPassword
        val confirmEt= signupBinding.includeIdPassword.etSignupPasswordCheck
        val msgTv = signupBinding.includeIdPassword.tvAvailablePw

        confirmEt.doOnTextChanged { text, start, before, count ->
            password = pwdEt.text.toString()
            passwordCheck = confirmEt.text.toString()

            if(passwordCheck.isEmpty()){
                msgTv.visibility = View.GONE
                pwCheck = false
            }
            else if(password == passwordCheck){
                msgTv.visibility = View.VISIBLE
                msgTv.text = "비밀번호가 일치합니다."
                msgTv.setTextColor(Color.parseColor("#327AFF"))
                pwCheck = true
            }
            else{
                msgTv.visibility = View.VISIBLE
                msgTv.text = "비밀번호가 일치하지 않습니다."
                msgTv.setTextColor(Color.parseColor("#F82525"))
                pwCheck = false
            }

        }


    }

    private fun checkIdPassword(): Boolean{
        nickname = signupBinding.includeIdPassword.etSignupNickname.text.toString()
        id = signupBinding.includeIdPassword.etSignupId.text.toString()
        password = signupBinding.includeIdPassword.etSignupPassword.text.toString()

        if(nickname.isEmpty() || id.isEmpty() || password.isEmpty()){return false}
        if(!idCheck){return false}
        if(!pwCheck){return false}

        var jangaeString = listToJsonManual(jangae)


        Log.d("test", "id : $id")
        Log.d("test", "pw : $password")
        Log.d("test", "nickname : $nickname")
        Log.d("test", "jangae : $jangaeString")
        Log.d("test", "tel : $tel")
        Log.d("test", "birth : $birth")
        Log.d("test", "gender : $gender")
        Log.d("test", "name : $name")


        authService.signUp(id, password, name, nickname, tel, birth, gender, jangaeString)
        
        return true
    }

    fun listToJsonManual(list: List<String>): String {
        val data = if (list.isEmpty()) listOf("없음") else list
        return data.joinToString(
            prefix = "[\"",
            separator = "\",\"",
            postfix = "\"]"
        )
    }

    //1단계 기본 정보 체크
    private fun checkBasicInfo(): Boolean{
        name   = signupBinding.includeBasicInfo.etBasicInfoName.text.toString()
        tel    = signupBinding.includeBasicInfo.etBasicInfoTel.text.toString()
        birth  = signupBinding.includeBasicInfo.etBasicInfoBirth.text.toString()

        if(name.isEmpty() || tel.isEmpty() || birth.isEmpty()){return false}
        if(birth.length != 8){return false}
        if(tel.length != 13){return false}
        if(tel.get(3) != '-' || tel.get(8) != '-'){return false}
        if(gender.isEmpty()){return false}

        return true;

    }

    private fun handleBasicInfo(){
        //성별 버튼
        signupBinding.includeBasicInfo.btnBasicInfoMale.setOnClickListener {
            signupBinding.includeBasicInfo.imvBasicInfoMale.setImageResource(R.drawable.bg_square_main_fill_14)
            signupBinding.includeBasicInfo.tvBasicInfoMale.setTextColor(resources.getColor(R.color.white))
            signupBinding.includeBasicInfo.imvBasicInfoFemale.setImageResource(R.drawable.bg_square_white_grey_14)
            signupBinding.includeBasicInfo.tvBasicInfoFemale.setTextColor(resources.getColor(R.color.black))
            gender = "남성"
        }
        signupBinding.includeBasicInfo.btnBasicInfoFemale.setOnClickListener {
            signupBinding.includeBasicInfo.imvBasicInfoFemale.setImageResource(R.drawable.bg_square_main_fill_14)
            signupBinding.includeBasicInfo.tvBasicInfoFemale.setTextColor(resources.getColor(R.color.white))
            signupBinding.includeBasicInfo.imvBasicInfoMale.setImageResource(R.drawable.bg_square_white_grey_14)
            signupBinding.includeBasicInfo.tvBasicInfoMale.setTextColor(resources.getColor(R.color.black))
            gender = "여성"
        }

        //없음 버튼
        signupBinding.includeBasicInfo.btnBasicInfoJangaeNo.setOnClickListener {
            signupBinding.includeBasicInfo.imvBasicInfoJangaeNo.setImageResource(R.drawable.bg_square_main_fill_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaeNo.setTextColor(resources.getColor(R.color.white))
            //기타
            signupBinding.includeBasicInfo.imvBasicInfoJangaegita.setImageResource(R.drawable.bg_square_white_grey_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaegita.setTextColor(resources.getColor(R.color.black))
            //경증보행
            signupBinding.includeBasicInfo.imvBasicInfoJangaegyungjeongbohang.setImageResource(R.drawable.bg_square_white_grey_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaegyungjeongbohang.setTextColor(resources.getColor(R.color.black))
            //청각
            signupBinding.includeBasicInfo.imvBasicInfoJangaeChungGak.setImageResource(R.drawable.bg_square_white_grey_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaeChungGak.setTextColor(resources.getColor(R.color.black))
            //지체
            signupBinding.includeBasicInfo.imvBasicInfoJangaejicahe.setImageResource(R.drawable.bg_square_white_grey_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaejicahe.setTextColor(resources.getColor(R.color.black))
            //언어
            signupBinding.includeBasicInfo.imvBasicInfoJangaeUnoh.setImageResource(R.drawable.bg_square_white_grey_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaeUnoh.setTextColor(resources.getColor(R.color.black))
            jangae.clear()
        }

        //청각
        signupBinding.includeBasicInfo.imvBasicInfoJangaeChungGak.setOnClickListener {
            signupBinding.includeBasicInfo.imvBasicInfoJangaeNo.setImageResource(R.drawable.bg_square_white_grey_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaeNo.setTextColor(resources.getColor(R.color.black))

            signupBinding.includeBasicInfo.imvBasicInfoJangaeChungGak.setImageResource(R.drawable.bg_square_main_fill_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaeChungGak.setTextColor(resources.getColor(R.color.white))
            jangae.add("청각장애")
        }
        //언어
        signupBinding.includeBasicInfo.imvBasicInfoJangaeUnoh.setOnClickListener {
            signupBinding.includeBasicInfo.imvBasicInfoJangaeNo.setImageResource(R.drawable.bg_square_white_grey_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaeNo.setTextColor(resources.getColor(R.color.black))

            signupBinding.includeBasicInfo.imvBasicInfoJangaeUnoh.setImageResource(R.drawable.bg_square_main_fill_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaeUnoh.setTextColor(resources.getColor(R.color.white))
            jangae.add("언어장애")

        }
        //경증
        signupBinding.includeBasicInfo.imvBasicInfoJangaegyungjeongbohang.setOnClickListener {
            signupBinding.includeBasicInfo.imvBasicInfoJangaeNo.setImageResource(R.drawable.bg_square_white_grey_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaeNo.setTextColor(resources.getColor(R.color.black))

            signupBinding.includeBasicInfo.imvBasicInfoJangaegyungjeongbohang.setImageResource(R.drawable.bg_square_main_fill_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaegyungjeongbohang.setTextColor(resources.getColor(R.color.white))
            jangae.add("경증보행장애")
        }
        //지체
        signupBinding.includeBasicInfo.imvBasicInfoJangaejicahe.setOnClickListener {
            signupBinding.includeBasicInfo.imvBasicInfoJangaeNo.setImageResource(R.drawable.bg_square_white_grey_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaeNo.setTextColor(resources.getColor(R.color.black))

            signupBinding.includeBasicInfo.imvBasicInfoJangaejicahe.setImageResource(R.drawable.bg_square_main_fill_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaejicahe.setTextColor(resources.getColor(R.color.white))
            jangae.add("지체장애")
        }

        //기타
        signupBinding.includeBasicInfo.imvBasicInfoJangaegita.setOnClickListener {
            signupBinding.includeBasicInfo.imvBasicInfoJangaeNo.setImageResource(R.drawable.bg_square_white_grey_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaeNo.setTextColor(resources.getColor(R.color.black))

            signupBinding.includeBasicInfo.imvBasicInfoJangaegita.setImageResource(R.drawable.bg_square_main_fill_14)
            signupBinding.includeBasicInfo.tvBasicInfoJangaegita.setTextColor(resources.getColor(R.color.white))
            jangae.add("기타")
        }
    }




    private fun updateStepUI() {
        if (signupBinding.includeBasicInfo.root.isVisible) {
            signupBinding.tvSignupNumber.text = "01"
            signupBinding.tvSignupTitle.text  = "기본정보입력"
            handleBasicInfo()
        } else if(signupBinding.includeIdPassword.root.isVisible) {
            signupBinding.tvSignupNumber.text = "02"
            signupBinding.tvSignupTitle.text  = "닉네임·계정정보"
            handleIdPassword()
        } else if(signupBinding.includeIdentify.root.isVisible){
            signupBinding.tvSignupNumber.text = "03"
            signupBinding.tvSignupTitle.text  = "신원인증"
            binding.btnNextSignupInfo.text = "완료"

            binding.btnNextSignupInfo.isEnabled = false
            binding.btnNextSignupInfo.backgroundTintList = resources.getColorStateList(R.color.gray_300)

            signupBinding.includeIdentify.layoutGetgalleryIdentify.setOnClickListener {
                getPicture()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSignUpSuccess(result: SignUpResult) {
        userId = result.userId
        Log.d("test", "유저 ID" + userId.toString())
    }

    override fun onSignUpFailure(errorMsg: String) {
        Log.d("test", errorMsg)
    }

    override fun onLoginSuccess(result: LoginResult) {

    }

    override fun onLoginFailure(errorMsg: String) {

    }

    override fun onIdCheckSuccess(result: IdCheckResult) {
        idCheck = result.isAvailable
        signupBinding.includeIdPassword.tvAvailableId.visibility = View.VISIBLE
        if(idCheck){
            signupBinding.includeIdPassword.tvAvailableId.setTextColor(Color.parseColor("#327AFF"))
            signupBinding.includeIdPassword.tvAvailableId.text = "사용 가능한 아이디입니다."
        }
        else{
            signupBinding.includeIdPassword.tvAvailableId.setTextColor(Color.parseColor("#F82525"))
            signupBinding.includeIdPassword.tvAvailableId.text = "이미 존재하는 아이디입니다."
        }

        Log.d("test", idCheck.toString())
    }
}

