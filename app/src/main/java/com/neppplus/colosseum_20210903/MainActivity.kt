package com.neppplus.colosseum_20210903

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import com.neppplus.colosseum_20210903.adapters.TopicAdapter
import com.neppplus.colosseum_20210903.datas.TopicData
import com.neppplus.colosseum_20210903.datas.UserData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import com.neppplus.colosseum_20210903.utils.ServerUtil.JsonResponseHandler
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : BaseActivity() {

    val mTopicList = ArrayList<TopicData>()
    lateinit var mTopicAdapter: TopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        topicListView.setOnItemClickListener { adapterView, view, position, l ->

            val clickedTopic = mTopicList[position]

            val myIntent = Intent(mContext, ViewTopicDetailActivity::class.java)
            myIntent.putExtra("topic", clickedTopic)
            startActivity(myIntent)

        }

    }

    override fun setValues() {

        getMainDataFromServer()//백그라운드처리, 적기는 먼저 적었지만 나중에 실행될 수 있음
        //어댑터가 먼저 추가되고 목록이 나중에 추가될 수 있다


        mTopicAdapter = TopicAdapter(mContext, R.layout.topic_list_item, mTopicList)
        topicListView.adapter = mTopicAdapter

    }

//    서버에서 메인 화면에 뿌려줄 정보를 받아오기
    fun getMainDataFromServer() {
        ServerUtil.getRequestMainData(mContext, object : JsonResponseHandler {
            override fun onResponse(jsonObj: JSONObject) {

//                응답 - jsonObj 분석 (파싱) => 토론 주제들을 서버가 내려줌
                val dataObj = jsonObj.getJSONObject("data")
                val topicsArr = dataObj.getJSONArray("topics")//데이터중괄호가 들고있네 토픽을
//                서버가 내려준 토론 주제들(JSONObject 목록)->TopicData로 변환해서 ArrayList에 추가(반복작업임, 추가 -> 반복문홯용)

//                topicArr에 10개 : 0~10직전까지 반복
                for (i in 0 until topicsArr.length()) {

                    val topicObj = topicsArr.getJSONObject(i)//순서에 맞는 {}를 통쨰로 받아내기

//                    Topic 데이터를 만들어서 -> 멤버변수들에 -> topicObj에서 파싱한 데이터를 대입
//                    val tempTopicData = TopicData()
//                    tempTopicData.id = topicObj.getInt("id")
//                    tempTopicData.title = topicObj.getString("title")
//                    tempTopicData.imageURL = topicObj.getString("img_url")


                    val tempTopicData = TopicData.getTopicDataFromJson(topicObj)


//                    mTopicList에 하나씩 추가 => 어댑터의 목록 구성 변수에 변화
                    mTopicList.add(tempTopicData)

                }

//                로그인한 사용자의 닉네임을 가져와서 토스트로 띄우기
                val userObj = dataObj.getJSONObject("user")
//                val userNickname = userObj.getString("nick_name")
                val loginUser = UserData.getUserDataFromJson(userObj)


//                목록의 변화 -> 리스트뷰가 인지 -> 새로고침 공지 -> 리스트뷰 변경 -> 백그라운드에서 UI 변경
                runOnUiThread {
                    mTopicAdapter.notifyDataSetChanged()
                    Toast.makeText(mContext, "${loginUser.nickname}님 환영합니다", Toast.LENGTH_SHORT).show()
                    //loginUser.email하면 email을 보여줄 수도 있음,

                }


            }
        })
    }

}
//test@test.com
//Test!123
//http://54.180.52.26/api/docs/