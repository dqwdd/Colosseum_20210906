package com.neppplus.colosseum_20210903

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.neppplus.colosseum_20210903.utils.ContextUtil
import com.neppplus.colosseum_20210903.utils.GlobalData
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {

        logoutBtn.setOnClickListener {

//            1. 로그인? == id랑 pw를 받아서 => 서버에 이 조합의 회원이 있는지 검사 요청
//            => 그런 회원이 있다면, 토큰 값을 받아서 기기에 저장 + 그 사람이 누군지 정보를 GlobalData 변수에 저장

//            2. 로그아웃? 정말 로그아웃 할지? 확인 눌리면 => 로그인 시 세팅한 데이터를 로그인 안했을 때처럼 초기화
//            => token 없던걸로
//            => GlobalData.loginUser 없던걸로

            val alert = AlertDialog.Builder(mContext)
            alert.setMessage("정말 로그아웃 하시겠습니까?")
            alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
//            => token 없던걸로 -> 어디서 가져오지? ContextUtill에서
//                ==> ContextUtill에 저장된 토큰 값을 ""으로 바꿔주자
                ContextUtil.setToken(mContext,"")
//            => GlobalData.loginUser 없던걸로
                GlobalData.loginUser = null

//            => 모두 끝나면 모든 화면 종료, 로그인 화면으로 이동
                val myIntent = Intent(mContext, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(myIntent)
//                finish()//이건 MyProfile화면 하나만 종료하는 거임 즉 다른거 써야 함->구글링
//                Intent 부가기능 활용 -> flag로 기존화면 (열었던 모든 화면) 전부 종료


            })
            alert.setNegativeButton("취소", null)//null자리는 할 일이 없다임
            alert.show()




        }

    }

    override fun setValues() {
    }
}