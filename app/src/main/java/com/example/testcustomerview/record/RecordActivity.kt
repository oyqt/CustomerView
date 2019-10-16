package com.example.testcustomerview.record

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSeekBar
import com.example.testcustomerview.R

/**
 * @author tll
 * @time 2019/10/16 14:59
 * @desc
 */
class RecordActivity :AppCompatActivity(){
    private val mSeekBar: AppCompatSeekBar by lazy { findViewById<AppCompatSeekBar>(R.id.seekBar) }
    private val mRecordView: RecordView by lazy { findViewById<RecordView>(R.id.recordView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                mRecordView.setCurProgress(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        mRecordView.setOnCusTouchListener(object : RecordView.OnCusTouchListener {
            override fun onTouchLongPress() {
                Log.i("asda", "长按")
            }

            override fun onCancelTouchLongPress() {
                Log.i("asda", "取消长按")
            }

            override fun onTouchClick() {
                Log.i("asda", "点击")
            }

        })
    }
}