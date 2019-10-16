package com.example.testcustomerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSeekBar

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.SeekBar
import com.example.testcustomerview.clock.ClockActivity
import com.example.testcustomerview.record.RecordActivity

import com.example.testcustomerview.record.RecordView
import com.example.testcustomerview.rotate.RotateActivity

class MainActivity : AppCompatActivity() {
    private val mBtnClock: Button by lazy { findViewById<Button>(R.id.btn_clock) }
    private val mBtnRecord: Button by lazy { findViewById<Button>(R.id.btn_record) }
    private val mBtnRotate: Button by lazy { findViewById<Button>(R.id.btn_rotate) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBtnClock.setOnClickListener {
            startActivity(Intent(this, ClockActivity::class.java))
        }

        mBtnRecord.setOnClickListener {
            startActivity(Intent(this, RecordActivity::class.java))
        }

        mBtnRotate.setOnClickListener {
            startActivity(Intent(this, RotateActivity::class.java))
        }
    }
}
