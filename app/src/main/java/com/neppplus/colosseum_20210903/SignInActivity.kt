package com.neppplus.colosseum_20210903

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.neppplus.colosseum_20210903.datas.UserData
import com.neppplus.colosseum_20210903.utils.ContextUtil
import com.neppplus.colosseum_20210903.utils.GlobalData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONObject

class SignInActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        자동로그인 체크박스의 값이 바뀔때마다 저장.
        autoLoginCheckBox.setOnCheckedChangeListener { compoundButton, isCheked ->

//            Log.d("체크여부", isCheked.toString())

//            자동로그인 여부인 isChecked 에 들어오는 값을 저장.
            ContextUtil.setAutoLogin(mContext, isCheked)

        }


        signUpBtn.setOnClickListener {
            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)
        }

        signInBtn.setOnClickListener {

//            입력한 아이디 비번 변수로 저장.
            val inputId = emailEdt.text.toString()
            val inputPw = passwordEdt.text.toString()

//            서버에 이 데이터가 회원이 맞는지? 확인 요청.  => 로그인 시도.
//            서버 로그인 시도 => 서버에 다녀오면 어떡할건지? 대응 가이드북 변수 첨부. (인터페이스 객체)
            ServerUtil.postRequestSignIn(inputId, inputPw, object : ServerUtil.JsonResponseHandler {

                override fun onResponse(jsonObj: JSONObject) {

//                    서버가 보내준 jsonObj를 가지고 처리하는 코드 작성 영역.

//                    Log.d("화면에서받은JSON",  jsonObj.toString())

//                    "code" 이름표가 붙은 Int값 추출.

                    val code = jsonObj.getInt("code")

//                    원하는 의도대로 잘 동작 (ex. 로그인 성공) => code : 200
//                    어떤 이유든 에러가 있다 : code : 200이 아닌 값.

                    if (code == 200) {
//                        정상 작동 경우 : 로그인 성공.

//                        그 뒤의 행동? 시나리오대로 작성.
//                        임시 시나리오 :  로그인한 사람의 닉네임을 토스트로
//                         "~~님, 환영합니다!"

////                        "data" 이름의 {  }를 변수로 담자.
//                        val dataObj = jsonObj.getJSONObject("data")
//
////                        data: { } 안에서,   user:{ } 를 변수에 담자.
//                        val userObj = dataObj.getJSONObject("user")
////                      jsonpretty에서 본 파일 상세 내용에서 보면 data괄호 안에 user가 들어있음
////                        그래서 한 번 더 변수 선언해서 데이터 받아옴
//                        val nickname = userObj.getString("nick_name")//어디서 꺼내오냐에 따라.getString일케 달라짐
//
//                        runOnUiThread {
//                        UI조작은 UI쓰레드에게 일을 따로 맡겨주자
//                            Toast.makeText(mContext, "${nickname}님 환영합니다!", Toast.LENGTH_SHORT).show()
//                        }


//                        서버가 내려주는 토큰값을 기기에 저장. (ContextUtil 활용)
//                        data {  } 내부에 토큰값이 내려옴.

                        val dataObj = jsonObj.getJSONObject("data")//json pretty에 있는 data괄호 거기 데이터 다 내놔
                        val token = dataObj.getString("token")//token이라 붙은거 다 내놔

                        ContextUtil.setToken(mContext, token)//token을 저장할거(함수) 더 만들고 오자



                        //여기가 추가된 작업 20210909-12:09
//                        data->user{}를 UserData로 변환
                        val userObj = dataObj.getJSONObject("user")
                        val loginUserData = UserData.getUserDataFromJson(userObj)

//                        GlobalData의 변수에 대입
                        GlobalData.loginUser = loginUserData



//                        메인화면으로 이동 + 로그인화면 종료
                        val myIntent = Intent(mContext, MainActivity::class.java)
                        startActivity(myIntent)
                        finish()


                    }
                    else {

//                        코드가 200이 아니다 -> 무조건 실패로 간주.
//                        1. 우선 토스트를 "로그인 실패" 로 띄워보자.
//                        백그라운드에서 서버통신 중 -> UI에 토스트 띄운다  -> 다른쓰레드가 UI 조작. (위험요소라 앱이 죽음.트렐로가봐)
//                        2. 서버가 알려주는 로그인 실패사유도 파싱. 토스트의 내용으로 띄워주자.

                        val message = jsonObj.getString("message")//메세지내놔

                        runOnUiThread {
//                            UI조작은, UI쓰레드에게 일을 따로 맡겨주자.

                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }


                    }



                }

            })



        }


    }

    override fun setValues() {

//        저장된 자동로그인 여부를 받아내서 -> 자동로그인 체크박스에 반영.
        autoLoginCheckBox.isChecked = ContextUtil.getAutoLogin(mContext)


    }

}