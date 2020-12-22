package com.bpzzr.skyschange

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bpzzr.skyschange.databinding.ActivityTestFragmentBinding

class TestFragmentActivity : AppCompatActivity() {


    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, TestFragmentActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_fragment)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fl_container, TestFragment())
            .commit()
    }
}