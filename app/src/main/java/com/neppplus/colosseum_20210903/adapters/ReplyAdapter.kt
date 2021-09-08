package com.neppplus.colosseum_20210903.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.neppplus.colosseum_20210903.R
import com.neppplus.colosseum_20210903.ViewTopicDetailActivity
import com.neppplus.colosseum_20210903.datas.ReplyData
import com.neppplus.colosseum_20210903.datas.TopicData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import org.json.JSONObject
import java.text.SimpleDateFormat

class ReplyAdapter(
    val mContext: Context,
    resId: Int,
    val mList: List<ReplyData>) : ArrayAdapter<ReplyData>(mContext, resId, mList) {

        val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row==null) {
            row = mInflater.inflate(R.layout.reply_list_item, null)
        }
        row!!

        val data = mList[position]

        val selectedSideTxt = row.findViewById<TextView>(R.id.selectedSideTxt)
        val writerNicknameTxt = row.findViewById<TextView>(R.id.writerTxt)
        val createdAtTxt = row.findViewById<TextView>(R.id.createdTxt)
        val contentTxt = row.findViewById<TextView>(R.id.contentTxt)
        val replyCountTxt = row.findViewById<TextView>(R.id.replyCountTxt)
        val likeCountTxt = row.findViewById<TextView>(R.id.likeCountTxt)
        val hateCountTxt = row.findViewById<TextView>(R.id.hateCountTxt)

        contentTxt.text = data.content
        replyCountTxt.text = "답글 ${data.replyCount}개"
        likeCountTxt.text = "좋아요 ${data.lickCount}개"
        hateCountTxt.text = "싫어요 ${data.hateCount}개"

        selectedSideTxt.text = "(${data.selectedSide.title})"

        writerNicknameTxt.text = data.writer.nickname

        createdAtTxt.text = data.getFormattedTimeAgo()

//        이 댓글은 내가 좋아요 / 싫어요 찍은 여부를 판단할 수 있다
//        data.myLike 등 변수를 활용

//        응용문제
//        내가 좋아요 한 댓글 -> 버튼 테두리 빨간 사각형
//        좋아요 안 한 댓글 -> 버튼 테두리 검정 사각형

//        싫어요 : 내가 했다면 파란 사각형. 아니면 검정


//        응용 문제 2
//        위의 같은 상황에서 글씨 색상도, 빨간 or #DFDFDF 회색
//        싫어요 글씨 파란색 or #DFDFDF 회색

        if (data.myLike) {
            likeCountTxt.setBackgroundResource(R.drawable.red_border_rect)
            likeCountTxt.setTextColor(mContext.resources.getColor(R.color.like_red))
        }
        else {
            likeCountTxt.setBackgroundResource(R.drawable.black_border_rect)
            likeCountTxt.setTextColor(mContext.resources.getColor(R.color.black))
        }

        if (data.myHate) {
            hateCountTxt.setBackgroundResource(R.drawable.blue_border_rect)
            hateCountTxt.setTextColor(mContext.resources.getColor(R.color.hate_blue))
        }
        else {
            hateCountTxt.setBackgroundResource(R.drawable.black_border_rect)
            hateCountTxt.setTextColor(mContext.resources.getColor(R.color.black))
        }





        likeCountTxt.tag = true
        hateCountTxt.tag = false

//        해당 댓글에 좋아요/싫어요 찍었다 : 서버에 전송

//        API : POST - topic_reply_like
//        토큰값 / 댓글 id / true or false (좋아요/싫어요) 파라미터

//        도전과제 : 두 개의 텍스트뷰가 눌리면 할 일이 거의 동일
//        차이점 : true 보내냐, false보내냐가 다름

        val ocl = object : View.OnClickListener {
            override fun onClick(view: View?) {

                val isLike = view!!.tag.toString().toBoolean()

                ServerUtil.postRequestLikeOrHate(mContext, data.id, isLike, object : ServerUtil.JsonResponseHandler{
                    override fun onResponse(jsonObj: JSONObject) {

//                        어댑터 안에서 -> ViewTopicDetailActivity의 (mContext변수에 담겨있다!)기능 실행

                        (mContext as ViewTopicDetailActivity).getTopicDetailDataFromServer()

                    }

                })

            }
        }

//        tag 속성 이용, 하나의 코드에서 두 개 대응


//        추가설명 : 좋아요/싫어요 갯수 바로 변경되도록 (어댑터 -> 액티비티의 기능 실행)


        likeCountTxt.setOnClickListener (ocl)
        hateCountTxt.setOnClickListener (ocl)


//        임시처리 : 답글 갯수도 눌러면 이벤트
        replyCountTxt.setOnClickListener {
//            답글 목록 보는 화면으로 이동
        }

        return row
    }

}