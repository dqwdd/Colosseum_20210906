package com.neppplus.colosseum_20210903

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.neppplus.colosseum_20210903.datas.ReplyData
import com.neppplus.colosseum_20210903.datas.SideData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_reply_detail.*
import org.json.JSONObject

class ViewReplyDetailActivity : BaseActivity() {

    lateinit var mReplyData : ReplyData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reply_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        okBtn.setOnClickListener {
            val inputContent = contentEdt.text.toString()

            if (inputContent.length<5){
                Toast.makeText(mContext, "5글자 이상 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ServerUtil.postRequestChileReply(mContext, inputContent, mReplyData.id, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(jsonObj: JSONObject) {

                    runOnUiThread {
                        contentEdt.setText("")
                    }


                }

            })

        }

    }

    override fun setValues() {

        mReplyData = intent.getSerializableExtra("replyData") as ReplyData

        sideAndNicknameTxt.text = "(${mReplyData.selectedSide.title}) ${mReplyData.writer.nickname}"
        replyContentTxt.text = mReplyData.content

    }
}