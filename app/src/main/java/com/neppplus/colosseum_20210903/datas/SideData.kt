package com.neppplus.colosseum_20210903.datas

import org.json.JSONObject

class SideData(
    var id: Int,
    var topicId: Int,
    var title: String,
    var voteCount: Int) {

    constructor() : this(0, 0, "", 0)//기본값들

    companion object {

//        JSON을 넣으면 -> SideData로 변환해주는 기능
        fun getSideDataFromJson(json : JSONObject) : SideData{

            val sideData = SideData()

            sideData.id = json.getInt("id")
            sideData.topicId = json.getInt("topic_id")
            sideData.title = json.getString("title")
            sideData.voteCount = json.getInt("vote_count")

            return sideData

        }

    }

}