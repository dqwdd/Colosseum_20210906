package com.neppplus.colosseum_20210903

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.my_custom_action_bar.*

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext : Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

//        모든 화면의 onCreate에서 커스텀 액션바 적용도 실행.
//        액션바가 존재하는 화면에서만 실행하고 싶다~(if (supportActionBar != null)setCustomActionBar()하거나 밑에꺼 하거나(더 코틀린다움)
//        액션바가 존재할 때만->별개의 함수를 실행

        supportActionBar?.let {
//            액션바가 널이 아닐 때만 실행시켜줄 코드(코틀린다운 코드)
            setCustomActionBar()
        }




    }

    abstract fun setupEvents(
    )
    abstract fun setValues()


    // 액션바를 커스텀 액션바로 바꿔주는 함수

    fun setCustomActionBar() {
//        기본 액션바를 불러오자. 그걸 커스텀 모드로 변경
        val defaultActionBar = supportActionBar!!

//        커스텀모드로 변경 -> 우리가 만든 xml로 적용
        defaultActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defaultActionBar.setCustomView(R.layout.my_custom_action_bar)

//        양 옆의 여백 제거 -> 모든 영역이 커스텀뷰가 되게
        val myToolbar = defaultActionBar.customView.parent as Toolbar
        myToolbar.setContentInsetsAbsolute(0,0)



//        세팅이 끝나면 UI들의 이벤트도 달아주자
        backBtn.setOnClickListener {
            finish()
        }

        notificationBtn.setOnClickListener {
            Toast.makeText(mContext, "알림 목록 보러 갑니다", Toast.LENGTH_SHORT).show()
            val myIntent = Intent(mContext, NotificationListActivity::class.java)
            startActivity(myIntent)
        }


//        모든 화면은 기본적으로 noti버튼은 숨겨두고 메인만 보여주기 처리(레이아웃이서 gone처리)

    }

}