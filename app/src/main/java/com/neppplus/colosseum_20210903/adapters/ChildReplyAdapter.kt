package com.neppplus.colosseum_20210903.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.neppplus.colosseum_20210903.R
import com.neppplus.colosseum_20210903.ViewReplyDetailActivity
import com.neppplus.colosseum_20210903.ViewTopicDetailActivity
import com.neppplus.colosseum_20210903.datas.NotiData
import com.neppplus.colosseum_20210903.datas.ReplyData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import org.json.JSONObject
import java.text.SimpleDateFormat

class ChildReplyAdapter(
    val mContext : Context,
    resId : Int,
    val mList : List<ReplyData>) : ArrayAdapter<ReplyData>(mContext, resId, mList) {

    val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView
        if (tempRow == null) {
            tempRow = mInflater.inflate(R.layout.chile_reply_list_item, null)
        }

        val row = tempRow!!

        val data = mList[position]


        val childSideAndNicknameTxt = row.findViewById<TextView>(R.id.childSideAndNicknameTxt)
        val childContentTxt = row.findViewById<TextView>(R.id.childContentTxt)
        val childLikeCountTxt = row.findViewById<TextView>(R.id.childLikeCountTxt)
        val childHateCountTxt = row.findViewById<TextView>(R.id.childHateCountTxt)



        childContentTxt.text = data.content

        childSideAndNicknameTxt.text = "(${data.selectedSide.title}) ${data.writer.nickname}"





        //댓글의 답글 입력화면에서 좋아요/싫어요 누르는 함수
        if (data.myLike) {
            childLikeCountTxt.setBackgroundResource(R.drawable.red_border_box)
            childLikeCountTxt.setTextColor(mContext.resources.getColor(R.color.red_border_box))
        }
        else {
            childLikeCountTxt.setBackgroundResource(R.drawable.black_border_rect)
            childLikeCountTxt.setTextColor(mContext.resources.getColor(R.color.black))
        }

        if (data.myHate) {
            childHateCountTxt.setBackgroundResource(R.drawable.blue_border_box)
            childHateCountTxt.setTextColor(mContext.resources.getColor(R.color.blue_border_box))
        }
        else {
            childHateCountTxt.setBackgroundResource(R.drawable.black_border_rect)
            childHateCountTxt.setTextColor(mContext.resources.getColor(R.color.black))
        }

        childLikeCountTxt.tag = true
        childHateCountTxt.tag = false

        val ocl = object : View.OnClickListener {
            override fun onClick(view: View?) {

                val isLike = view!!.tag.toString().toBoolean()

                ServerUtil.postRequestLikeOrHate(mContext, data.id, isLike, object : ServerUtil.JsonResponseHandler{
                    override fun onResponse(jsonObj: JSONObject) {
//                        어댑터 안에서 -> ViewTopicDetailActivity의 (mContext변수에 담겨있다!)기능 실행
                        (mContext as ViewReplyDetailActivity).getChildRepliesFromServer()
                    }
                })
            }
        }

//        tag 속성 이용, 하나의 코드에서 두 개 대응

//        추가설명 : 좋아요/싫어요 갯수 바로 변경되도록 (어댑터 -> 액티비티의 기능 실행)


        childLikeCountTxt.setOnClickListener (ocl)
        childHateCountTxt.setOnClickListener (ocl)




        return row
    }


}