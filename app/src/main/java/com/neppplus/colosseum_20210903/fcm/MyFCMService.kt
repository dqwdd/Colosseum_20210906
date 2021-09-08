package com.neppplus.colosseum_20210903.fcm

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCMService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

//        푸시 알림을 받았을 때 실행시킬 코드?
        Log.d("푸시알림", "수신 이벤트")

//        토스트로 받은 내용(제목) 출력 => 앱을 켜둔 상태에서 알림을 받았을 때만 실행 됨

        val title = p0.notification!!.title

//        핸들러 활용 -> UI쓰레드 (메인쓰레드) 접근

        val myHandler = Handler(Looper.getMainLooper())//Handler는 android.os꺼 고름
        myHandler.post {

            //runOnUiThread와 같은 역할
            Toast.makeText(applicationContext, title, Toast.LENGTH_SHORT).show()


        }



    }

}