package com.neppplus.colosseum_20210903.datas

import org.json.JSONObject
import java.io.Serializable

class TopicData(
    var id: Int,
    var title: String,
    var imageURL: String) : Serializable {

//    선택 가능한 진영의 목록 담아줄 ArrayList
    val sideList = ArrayList<SideData>()//()붙이는거 == 객체화

//    내가 투표한 진영의 Id가 뭔가?
    var mySideId = 0
//    내가 투표한 진영 자체를 저장
    var myselectedSide : SideData? = null//투표한 진영이 없다면 널이 될 수 있다


    companion object {

//        json {} 를 넣으면 -> 파싱해서 -> TopicData 객체로 리턴해주는 함수

        fun getTopicDataFromJson( json : JSONObject ) : TopicData {

//            결과로 사용될 TopicData 객체 하나 생성
            val topicData = TopicData()

//            json 내부에서 값을 파싱 -> TopicData의 변수들에 대입
            topicData.id = json.getInt("id")
            topicData.title = json.getString("title")
            topicData.imageURL = json.getString("img_url")


//            토론의 하위 정보로 -> sides라는 JSONArray를 내려줌
//            JSONArray : for문 돌려서 파싱 -> sideList에 추가해주기
            val sideArr = json.getJSONArray("sides")

            for (i in 0 until sideArr.length()) {

                val sideObj = sideArr.getJSONObject(i)

//                JSONObject -> SideData()형태로 변환
                val sideData = SideData.getSideDataFromJson(sideObj)

//                topicData의 sideList에 추가하기
                topicData.sideList.add(sideData)

            }

//            내가 선택한 진영의 id?
            topicData.mySideId = json.getInt("my_side_id")

//            그 진영이 어떤건지? => null로 내려오면 파싱 x
            if (json.isNull("my_side")) {
//                null이 아닐 때만 파싱
                topicData.myselectedSide = SideData.getSideDataFromJson( json.getJSONObject("my_side"))
            }

//            최종 결과 선정
            return topicData
        }

    }




//    보조 생성자 추가
    constructor() : this(0, "제목 없음", "")



//    연습. id값만받는 보조생성자 생성
    constructor(id: Int) : this(id, "제목없음","")//받아준것만초깃값으로집어넣ㅇ는생성자
}