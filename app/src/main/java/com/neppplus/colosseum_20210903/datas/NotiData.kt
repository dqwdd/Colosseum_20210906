package com.neppplus.colosseum_20210903.datas

import org.json.JSONObject
import java.io.Serializable

class NotiData(
    var id: Int,
    var title: String) : Serializable {


    constructor() : this(0, "제목 없음")//기본값들

//
    companion object {

        //        JSON을 넣으면 -> SideData로 변환해주는 기능
        fun getNotiDataFromJson(json : JSONObject) : NotiData{

            val notiData = NotiData()

            notiData.id = json.getInt("id")
            notiData.title = json.getString("title")

            return notiData

        }

    }

}