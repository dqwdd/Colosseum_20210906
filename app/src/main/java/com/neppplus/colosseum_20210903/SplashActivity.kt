package com.neppplus.colosseum_20210903

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.neppplus.colosseum_20210903.datas.UserData
import com.neppplus.colosseum_20210903.utils.ContextUtil
import com.neppplus.colosseum_20210903.utils.ServerUtil
import org.json.JSONObject

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
    }

    override fun setValues() {

        val myHandler = Handler(Looper.getMainLooper())

        myHandler.postDelayed({


            //        1. 자동로그인을 해도 되는 상황인지 여부 판단 -> 상황에 따라 다른 화면으로 넘어가게
//        다른 화면 : Intent의 목적지만 달라진다.

            val myIntent : Intent

//        자동 로그인의 여부 : 사용자가 자동로그인 하겠다 + 저장된 토큰이 유효(들어있다)하다
            if (ContextUtil.getAutoLogin(mContext) && ContextUtil.getToken(mContext) != "") {
                //체크가 되어 있느냐, 토큰이 저장되어 있느냐, SigninActivity에서 저장할 때 없으면 ""을 기본값으로 했었음
                //둘 다 만족 : 자동로그인 O -> 메인으로 이동
                myIntent = Intent(mContext, MainActivity::class.java)


//                내가 누구인지 정보를 받아오자 => API 활용
//                어느 화면에서든 접근할 수 있게 세팅해주자
                ServerUtil.getRequestUserData(mContext, object : ServerUtil.JsonResponseHandler{
                    override fun onResponse(jsonObj: JSONObject) {

                        val dataObj = jsonObj.getJSONObject("data")
                        val userObj = dataObj.getJSONObject("user")

                        val loginUserData = UserData.getUserDataFromJson(userObj)

                        Log.d("자동로그인", "로그인한 사람 닉네임 - ${loginUserData.nickname}")

                    }

                })



            } else {
                myIntent = Intent(mContext, SignInActivity::class.java)

//                내가 누구인지 받아오지 않겠다.(코드작성x)

            }

            startActivity(myIntent)
            finish()

        }, 2500)



    }
}