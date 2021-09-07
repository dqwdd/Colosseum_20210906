package com.neppplus.colosseum_20210903

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.neppplus.colosseum_20210903.adapters.ReplyAdapter
import com.neppplus.colosseum_20210903.datas.ReplyData
import com.neppplus.colosseum_20210903.datas.TopicData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    lateinit var mTopicData : TopicData
//    lateinit var mSideId : TopicData

    val mReplyList = ArrayList<ReplyData>()
    lateinit var mReplyAdapter: ReplyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_topic_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        addReplyBtn.setOnClickListener {

//            투표를 해야만 댓글 작성 화면 이동 가능하게 하자
//            선택한 진영이 없다면, myIntent 관련 코드 실행 불가 => Validation(입력값, 검증 작업)
            if (mTopicData.myselectedSide == null) {
                Toast.makeText(mContext, "투표를 하셔야 의견 등록이 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 결과 지정 : 함수를 강제 종료
            }

            val myIntent = Intent(mContext, EditReplyActivity::class.java)
            myIntent.putExtra("selectedSide", mTopicData.myselectedSide)
            startActivity(myIntent)
        }


//        첫 번째 진영, 두 번째 진영 투표 버튼의 이벤트 생성
//        두 개의 버튼이 하는 일이 거의 동일함 -> 코드를 한 번만 짜서, 두 개의 버튼에 똑같이 달아보자

//        버튼이 눌리면 할 일(OnclickListener)을 적어두는 변수 (Interface 변수)
        val ocl = object : View.OnClickListener {
            override fun onClick(view: View?) { //==>p0를 view로 바꿨는데 view는 눌린게 어떤 버튼인지, 즉 눌린 버튼을 담아준다

//                버튼이 눌리면 할 일
//                view는 눌린게 어떤 버튼인지?, 즉 눌린 버튼을 담아준다
                val clickedSideId = view!!.tag.toString().toInt()

                Log.d("투표 진영 Id", clickedSideId.toString())



//                해당 진영에 투표하기 (서버에 투표 실행)
                ServerUtil.postRequestTopicVote(mContext, clickedSideId, object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(jsonObj: JSONObject) {

//                        투표 결과를 확인 => 새로 투표 현황을 다시 받아오자
//                        이전에 함수로 분리해둔, 서버에서 상세 정보 받아오기 호출
                        getTopicDetailDataFromServer()//아까 여기에서 다 파싱하고 불러오고 하는거 다 짜놨었으니 재활용


                    }

                })


            }

        }

        voteToFirstSideBtn.setOnClickListener(ocl)
        voteToSecondSideBtn.setOnClickListener(ocl)


    }

    override fun setValues() {

        mTopicData = intent.getSerializableExtra("topic") as TopicData

//        투표 버튼에 각 진영이 어떤 진영인지 "버튼에 메모해두면" -> 투표할 때, 그 진영이 뭔지 알아낼 수 있다.
        voteToFirstSideBtn.tag = mTopicData.sideList[0].id
        voteToSecondSideBtn.tag = mTopicData.sideList[1].id


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


//            투표 여부에 따라 버튼들에 다른 문구 적용
            if(mTopicData.mySideId == -1) {
                voteToFirstSideBtn.text = "투표하기"
                voteToSecondSideBtn.text = "투표하기"
            }
            else {
//                내 투표 진영의 id가  첫째 진영의 id와 같은지?
                if(mTopicData.mySideId == mTopicData.sideList[0].id) {
                    voteToFirstSideBtn.text = "취소하기"
                    voteToSecondSideBtn.text = "선택하기"
                }
                else {
                    voteToFirstSideBtn.text = "선택하기"
                    voteToSecondSideBtn.text = "취소하기"
                }

            }


//            리스트뷰도 새로고침
            mReplyAdapter.notifyDataSetChanged()//서버에서 데이터를 받아와도 해외 같은 경우 멀어서 느릴 수 있으니
        // 이렇게 새로고침 넣어줌(UI새로고침 시 리스트뷰도 새로고침)

        }

    }

}