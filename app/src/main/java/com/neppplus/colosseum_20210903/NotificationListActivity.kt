package com.neppplus.colosseum_20210903

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import com.neppplus.colosseum_20210903.adapters.NotiListAdapter
import com.neppplus.colosseum_20210903.datas.NotiData
import kotlinx.android.synthetic.main.activity_notification_list.*

class NotificationListActivity : BaseActivity() {

    val mNotiList = ArrayList<NotiData>()
    lateinit var mNotiListAdapter : NotiListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {
    }

    override fun setValues() {
    }
}