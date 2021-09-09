package com.neppplus.colosseum_20210903

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.neppplus.colosseum_20210903.datas.ReplyData
import com.neppplus.colosseum_20210903.datas.SideData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_reply_detail.*
import org.json.JSONObject

class ViewReplyDetailActivity : BaseActivity() {

    lateinit var mReplyData : ReplyData

    val mChildReplyList = ArrayList<ReplyData>()



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

//                        도전코드(구글링) : 키보드 숨김 처리
                        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

                    }
//

                }

            })

        }

    }

    override fun setValues() {

        mReplyData = intent.getSerializableExtra("replyData") as ReplyData

        sideAndNicknameTxt.text = "(${mReplyData.selectedSide.title}) ${mReplyData.writer.nickname}"
        replyContentTxt.text = mReplyData.content


        getChildRepliesFromServer()

    }

    fun getChildRepliesFromServer() {
        ServerUtil.getRequestReplyDetail(mContext, mReplyData.id, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(jsonObj: JSONObject) {

                val dataObj = jsonObj.getJSONObject("data")
                val replyObj = dataObj.getJSONObject("reply")

                val repliesArr = replyObj.getJSONArray("replies")

                for ( i in 0 until repliesArr.length() ) {
                    mChildReplyList.add(ReplyData.getReplyDataFromJson(repliesArr.getJSONObject(i)))
                }

            }

        })
    }

}