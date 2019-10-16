package com.example.testcustomerview.rotate

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.testcustomerview.R

/**
 * @author tll
 * @time 2019/10/16 15:59
 * @desc
 */
class RotateActivity : AppCompatActivity() {
    private val mBtnStartAnimation: Button by lazy { findViewById<Button>(R.id.btn_start_animation) }
    private val mRecordView: RotateView by lazy { findViewById<RotateView>(R.id.recordView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rotate)
        mBtnStartAnimation.setOnClickListener {
            mRecordView.start()
        }
    }
}