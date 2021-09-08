package com.neppplus.colosseum_20210903

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import com.neppplus.colosseum_20210903.adapters.NotiListAdapter
import com.neppplus.colosseum_20210903.datas.NotiData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_notification_list.*
import org.json.JSONObject

class NotificationListActivity : BaseActivity() {

//    알림 목록을 담을 ArrayList
    val mNotiList = ArrayList<NotiData>()
    lateinit var mNotiListAdapter : NotiListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {
    }

    override fun setValues() {

        getNotiListFromServer()
        mNotiListAdapter = NotiListAdapter(mContext, R.layout.noti_list_item, mNotiList)
        notiListView.adapter = mNotiListAdapter

    }


    fun getNotiListFromServer() {//json에서 그 괄호 안에 데이터 꺼내는 과정
        ServerUtil.getRequestNotificationCountOrList(mContext, true, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(jsonObj: JSONObject) {

                val dataObj = jsonObj.getJSONObject("data")//
                val notificationsArr = dataObj.getJSONArray("notifications")

                for (i in 0 until notificationsArr.length()) {
                    val notiObj = notificationsArr.getJSONObject(i)

                    val notiData = NotiData.getNotiDataFromJson(notiObj)

                    mNotiList.add(notiData)

                }

                //이제 UI에 반영
                runOnUiThread {
                    //어댑터가 새로 고침
                    mNotiListAdapter.notifyDataSetChanged()//너 새로고침좀 해라
                }



            }

        })
    }

}