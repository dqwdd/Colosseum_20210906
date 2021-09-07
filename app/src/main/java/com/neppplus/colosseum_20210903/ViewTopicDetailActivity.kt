package com.neppplus.colosseum_20210903

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.neppplus.colosseum_20210903.adapters.ReplyAdapter
import com.neppplus.colosseum_20210903.datas.ReplyData
import com.neppplus.colosseum_20210903.datas.TopicData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    lateinit var mTopicData : TopicData

    val mReplyList = ArrayList<ReplyData>()
    lateinit var mReplyAdapter: ReplyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_topic_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        첫 번째 진영, 두 번째 진영 투표 버튼의 이벤트 생성
//        두 개의 버튼이 하는 일이 거의 동일함 -> 코드를 한 번만 짜서, 두 개의 버튼에 똑같이 달아보자

//        버튼이 눌리면 할 일(OnclickListener)을 적어두는 변수 (Interface 변수)
        val ocl = object : View.OnClickListener {
            override fun onClick(view: View?) { //==>p0를 view로 바꿨는데 view는 눌린게 어떤 버튼인지, 즉 눌린 버튼을 담아준다
//                버튼이 눌리면 할 일
//                해당 진영에 투표하기 (서버에 투표 실행)
                ServerUtil.postRequestTopicVote(mContext, 1, object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(jsonObj: JSONObject) {

//                        투표 결과를 확인

                    }

                })


            }

        }

        voteToFirstSideBtn.setOnClickListener(ocl)
        voteToSecondSideBtn.setOnClickListener(ocl)


    }

    override fun setValues() {

        mTopicData = intent.getSerializableExtra("topic") as TopicData

        Glide.with(mContext).load(mTopicData.imageURL).into(topicImg)
        titleTxt.text = mTopicData.title

//        나머지 데이터는 서버에서 가져오자
        getTopicDetailDataFromServer()


        mReplyAdapter = ReplyAdapter(mContext, R.layout.reply_list_item, mReplyList)
        replyListView.adapter = mReplyAdapter

    }

//    투표 현황 등, 최신 토론 상세 데이터를 다시 서버에서 불러오기

    fun getTopicDetailDataFromServer() {
        ServerUtil.getRequestTopicDetail(mContext, mTopicData.id, object : ServerUtil.JsonResponseHandler {
            override fun onResponse(jsonObj: JSONObject) {

                val dataObj = jsonObj.getJSONObject("data")
                val topicObj = dataObj.getJSONObject("topic")

//                mTopicData를 새로 파싱한 데이터로 교체

                mTopicData = TopicData.getTopicDataFromJson(topicObj)



//                topicObj 안을 들여다 보면 댓글 목록도 같이 들어있다 -> 추가 파싱, UI 반영
                val repliesArr =  topicObj.getJSONArray("replies")

                for (i in 0 until repliesArr.length()) {
//                    댓글 {} json -> ReplyData 파싱 (변환) -> mReplyList 목록에 추가

//                    val replyObj = repliesArr.getJSONObject(i)//side{+}라서 그 안에꺼 내놔 하는 함수
//                    val replyData = ReplyData.getReplyDataFromJson(replyObj)//열심히 어댑터에 짠 데이터 replyData에 넣음
//                    mReplyList.add(replyData)
                    mReplyList.add(ReplyData.getReplyDataFromJson(repliesArr.getJSONObject(i)))//위에 3줄을 이렇게 1줄로 짤 수 있음

                }



//                새로 받은 데이터로 UI 반영(득표 수 등등)
                refreshTopicDataToUI()

            }

        })
    }



    fun refreshTopicDataToUI() {

        runOnUiThread {

            fistSideTitleTxt.text = mTopicData.sideList[0].title
            fistSideVoteCountTxt.text = "${mTopicData.sideList[0].voteCount}표"

            secondSideTitleTxt.text = mTopicData.sideList[1].title
            fistSideVoteCountTxt.text = "${mTopicData.sideList[1].voteCount}표"

//            리스트뷰도 새로고침
            mReplyAdapter.notifyDataSetChanged()//서버에서 데이터를 받아와도 해외 같은 경우 멀어서 느릴 수 있으니
        // 이렇게 새로고침 넣어줌(UI새로고침 시 리스트뷰도 새로고침)

        }

    }

}