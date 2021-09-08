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
import com.neppplus.colosseum_20210903.ViewTopicDetailActivity
import com.neppplus.colosseum_20210903.datas.NotiData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import org.json.JSONObject
import java.text.SimpleDateFormat

class NotiListAdapter(
    val mContext : Context,
    resId : Int,
    val mList : List<NotiData>) : ArrayAdapter<NotiData>(mContext, resId, mList) {

    val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView
        if (tempRow == null) {
            tempRow = mInflater.inflate(R.layout.noti_list_item, null)
        }

        val row = tempRow!!

        val data = mList[position]

        val notiTitleTxt = row.findViewById<TextView>(R.id.notiTitleTxt)
        val createdAtTxt = row.findViewById<TextView>(R.id.createdAtTxt)

        notiTitleTxt.text = data.title


//        Calendar -> String으로 가공(SimpleDateFormat - format 활용)
        val sdf = SimpleDateFormat("yyyy년 M월 d일 a h:mm")
        createdAtTxt.text = sdf.format(data.createdAt.time)


        return row
    }


}