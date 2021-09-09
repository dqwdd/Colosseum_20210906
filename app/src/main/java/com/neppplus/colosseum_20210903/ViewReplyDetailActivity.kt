package com.neppplus.colosseum_20210903

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.neppplus.colosseum_20210903.adapters.ChildReplyAdapter
import com.neppplus.colosseum_20210903.adapters.ReplyAdapter
import com.neppplus.colosseum_20210903.datas.ReplyData
import com.neppplus.colosseum_20210903.datas.SideData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_reply_detail.*
import org.json.JSONObject

class ViewReplyDetailActivity : BaseActivity() {

    lateinit var mReplyData : ReplyData

    val mChildReplyList = ArrayList<ReplyData>()

    lateinit var mChildReplyAdapter: ChildReplyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reply_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        답글 삭제 리스트 -> 리스트뷰의 이벤트 처리(LongClick)
        childReplyListView.setOnItemLongClickListener { adapterView, view, position, l ->

//            내가 적은 답글이 아니라면, 함수 강제 종료
//            길게 누른 답글의 작성자가 나인가??를 확인
//            답글.작성자.id(Int) == 로그인 한 사람.id(Int)인가?


//            if () {
//                return@setOnItemLongClickListener true
//            }


//            경고창 -> 정말 해당 답글을 삭제하시겠습니까?

            val alert = AlertDialog.Builder(mContext)
            alert.setMessage("정말 해당 답글을 삭제하시겠습니까?")
            alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

//                해당 답글 삭제 -> API 요청 + 새로고침

            })

            alert.setNegativeButton("취소", null)
            alert.show()

            return@setOnItemLongClickListener true//true면 롱클릭만, false면 손 떼면 일반클릭도 같이 실행
        }






        okBtn.setOnClickListener {
            val inputContent = contentEdt.text.toString()

            if (inputContent.length<5){
                Toast.makeText(mContext, "5글자 이상 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ServerUtil.postRequestChileReply(mContext, inputContent, mReplyData.id, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(jsonObj: JSONObject) {

//                    답글 목록 다시 불러오기
                    getChildRepliesFromServer()



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

        mChildReplyAdapter = ChildReplyAdapter(mContext, R.layout.chile_reply_list_item, mChildReplyList)
        childReplyListView.adapter = mChildReplyAdapter
        //이거 만들고 밑에 mChildReplyAdapter.notifyDataSetChanged()이거 선언해야함
        //프롬서버라서 늦게 올 수 있으니까 같이 오게 런쓰레스

    }

    fun getChildRepliesFromServer() {
        ServerUtil.getRequestReplyDetail(mContext, mReplyData.id, object : ServerUtil.JsonResponseHandler{
            override fun onResponse(jsonObj: JSONObject) {

                val dataObj = jsonObj.getJSONObject("data")
                val replyObj = dataObj.getJSONObject("reply")

                val repliesArr = replyObj.getJSONArray("replies")


//                댓글이 쌓이는걸 방지
                mChildReplyList.clear()

                for ( i in 0 until repliesArr.length() ) {
                    mChildReplyList.add(ReplyData.getReplyDataFromJson(repliesArr.getJSONObject(i)))
                }


                runOnUiThread {
                    mChildReplyAdapter.notifyDataSetChanged()

//                    리스트뷰의 최하단으로 이동(댓글 쓰면)
                    childReplyListView.smoothScrollToPosition(mChildReplyList.lastIndex)

                }


            }

        })
    }

}