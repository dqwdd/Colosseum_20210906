package com.neppplus.colosseum_20210903.datas

import android.util.Log
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ReplyData (
    var id:Int,
    var content: String,
    var lickCount: Int,
    var hateCount: Int,
    var myLike : Boolean,
    var myHate : Boolean,
    var replyCount : Int) {

//    ReplyData의 하위 개념들

//    이 댓글이 지지하는 진영
    lateinit var selectedSide : SideData

//    이 댓글을 적은 사람
    lateinit var writer : UserData

//    이 댓글이 적힌 시점 (날짜 + 시간) -> Calendar 클래스 활용
//    SimpleDateFormat을 이용하면 -> 다양한 양식으로 가공 가능
//    ex) 지금 "2021-07-02 06:43:26"이건데 스트링으로 받자나 이걸 활용함
    val createdAt = Calendar.getInstance() //현재


    constructor() : this(0,"",0,0,false,false,0)

//    각 댓글마다의 기능 : 작성된 일시를 보고 -> ~분전 ~일전 ~시간 전 등으로가공
//    5일 이상 : yyyy년 MM월 dd일로 가공
    fun getFormattedTimeAgo() : String {

//        지금으로부터 얼마나 이전에 작성된 글인가? 두 일시의 텀(지금시간-작성된시간) 계산
        val now = Calendar.getInstance()
        val interval = now.timeInMillis - this.createdAt.timeInMillis

        Log.d("두 시간의 간격", interval.toString())

        if (interval < 1000) {
//            간격 : 밀리초까지 계산(1초/1000)
//            1초도안된다 -> "방금 전"으로 결과 나오게 하자 -> 위에  : String  추가
            return "방금 전"
        }
        else if (interval < 1 * 60 * 1000) {
//            1분이내면 -> ?초전으로 결과 나오게 하자
            return "${interval/1000}초 전"
        }
        else if (interval < 1 * 60 * 60 * 1000) {
//            1시간 이내면 -> ?분전으로 결과 나오게 하자
            return "${interval/1000/60}분 전"
        }
        else if (interval < 24 * 60 * 60 * 1000) {
//            1일이내면 -> ?시간전으로 결과 나오게 하자
            return "${interval / 1000 / 60 / 60}시간 전"
        }
        else if (interval < 5 * 24 * 60 * 60 * 1000) {
//            5일 이내면 -> 5일이내로 결과 나오게 하자
            return "${interval / 1000 / 60 / 60 / 24}일 전"
        }
        else {
//            5일 이상이면 -> yyyy년 M월 d일로 가공
            val replyDisplayFormat = SimpleDateFormat("yyyy년 MM월 d일")
            return replyDisplayFormat.format(this.createdAt.time)
        }

    }



    companion object {

//        서버가 주는 날짜 양식을 분석하기 위한 SimpleDateFormat
        val serverFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


//        JSON을 넣으면 -> ReplyData로 변환해주는 기능

        fun getReplyDataFromJson(json : JSONObject) : ReplyData {

            val replyData = ReplyData()

            replyData.id = json.getInt("id")
            replyData.content = json.getString("content")
            replyData.lickCount = json.getInt("like_count")
            replyData.hateCount = json.getInt("dislike_count")
            replyData.myLike = json.getBoolean("my_like")
            replyData.myHate = json.getBoolean("my_dislike")
            replyData.replyCount = json.getInt("reply_count")

//            선택 진영 파싱 -> SideData에 만들어둔 파싱 기능 활용
            val selectedSideObj = json.getJSONObject("selected_side")
            replyData.selectedSide = SideData.getSideDataFromJson(selectedSideObj)



//            작성자 정보 파싱 -> UserData의 기능 활용
            val userObj = json.getJSONObject("user")//이름이 user인 JSONObject를 달라
            replyData.writer = UserData.getUserDataFromJson(userObj)//이제 이걸 어댑터가 뿌려줄 때 사용


//            작성 일시 -> String으로 받아서 -> Calendar로 변환해서 저장
            val createdAtString = json.getString("created_at")
            //이걸 어케 변형하냐~-->위에yyyymm어쩌구

//            댓글 데이터 작성 일시에 serverFormat 변수를 이용해서 시간 저장
            replyData.createdAt.time = serverFormat.parse(createdAtString)





            return replyData

        }


    }


}