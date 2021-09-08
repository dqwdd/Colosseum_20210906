package com.neppplus.colosseum_20210903.datas

import android.util.Log
import org.json.JSONObject
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class NotiData(
    var id: Int,
    var title: String) : Serializable {

//    생성자와 관계없이 동작하는 멤버변수
//    val : Calendar의 내부 값만 변경. 변수 자체의 대입x
    val createdAt = Calendar.getInstance() // 현재 시간이 기본값 + 시간대도 폰에 설정된 시간대(서울)로 기본 설정


    constructor() : this(0, "제목 없음")//기본값들

//
    companion object {

        //        JSON을 넣으면 -> SideData로 변환해주는 기능
        fun getNotiDataFromJson(json : JSONObject) : NotiData{

            val notiData = NotiData()

            notiData.id = json.getInt("id")
            notiData.title = json.getString("title")

//            1. 서버가 알려주는 시간을 String으로 단순히 받기부터
            val createdAtString = json.getString("created_at")

//            2. 받아낸 String을 => Calendar의 time값으로 대입 (SimpleDateFormat - parse 필요)

            val serverFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            notiData.createdAt.time = serverFormat.parse(createdAtString)//parse쓸 때 목록중 String


//            3. 참고 코드 - 서버는 우리에게 UTC (GMT+0) => 영국 시간대를 기준으로 시간을 알려줌
//            앱은 -> 본인의 시간대(TimeZone)를 알아내서, 시간 값을 조정해보자


            val localTimeZone = notiData.createdAt.timeZone

            Log.d("내 폰의 시간대", localTimeZone.displayName)

//            몇 시간이나 차이날까?
            val timeDiff = localTimeZone.rawOffset /1000/60/60 //시차를, 밀리초단위까지 계산해준 결과
            Log.d("내 폰의 시차", timeDiff.toString())


//            내 알림의 생성시간에, 시차를 더해주자
            notiData.createdAt.add(Calendar.HOUR, timeDiff)


            return notiData

        }

    }

}