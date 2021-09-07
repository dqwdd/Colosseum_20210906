package com.neppplus.colosseum_20210903.datas

import org.json.JSONObject

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


    constructor() : this(0,"",0,0,false,false,0)

    companion object {

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

            return replyData

        }


    }


}