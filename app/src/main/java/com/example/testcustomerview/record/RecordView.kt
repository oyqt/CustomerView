package com.example.testcustomerview.record

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.CountDownTimer
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.testcustomerview.R
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*


/**
 * @author tll
 * @time 2019/10/14 13:48
 * @desc 录制按钮
 */
class RecordView : View {
    private lateinit var mPaint: Paint
    private lateinit var mTxtPaint: Paint

    // 外圈宽
    private var mOutWidth: Int = 0
    // 外圈高
    private var mOutHeight: Int = 0

    // 外圆颜色
    private var mOuterRoundColor: Int = 0
    // 内圆颜色
    private var mInnerRoundColor: Int = 0
    // 边圈颜色
    private var mStrokeColor: Int = 0
    // 边圈宽度
    private var mStrokeWidth: Float = 0F
    // 绘制的文字
    private lateinit var mDrawTxt: String
    // 绘制的文字颜色
    private var mDrawTxtColor: Int = 0
    // 绘制的文字大小
    private var mDrawTxtSize: Float = 0F

    // 当前进度
    private var mCurProgress: Int = 0

    private val mRectF: RectF = RectF()

    interface OnCusTouchListener {
        fun onTouchLongPress()

        fun onCancelTouchLongPress()

        fun onTouchClick()
    }

    fun setOnCusTouchListener(listener: OnCusTouchListener) {
        mOnTouchListener = listener
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.RecordView)
        typedArray?.apply {
            mOuterRoundColor = getColor(R.styleable.RecordView_outerRoundColor, ContextCompat.getColor(context, R.color.grey))
            mInnerRoundColor = getColor(R.styleable.RecordView_innerRoundColor, ContextCompat.getColor(context, R.color.white))
            mStrokeColor = getColor(R.styleable.RecordView_strokeColor, ContextCompat.getColor(context, R.color.stroke))
            mStrokeWidth = getDimension(R.styleable.RecordView_strokeWidth, 26F)
            mDrawTxt = getString(R.styleable.RecordView_drawTxt)
                    ?: "0%"
            mDrawTxtColor = getColor(R.styleable.RecordView_drawTxtColor, 0X000000)
            mDrawTxtSize = getDimension(R.styleable.RecordView_drawTxtSize, 28F)
            mDrawDuration = getInteger(R.styleable.RecordView_maxDuration, 15000).toLong()
            recycle()
        }

        init()
    }

    private fun init() {
        mPaint = Paint()
        mTxtPaint = TextPaint()
        mTxtPaint.isAntiAlias = true
        mPaint.isAntiAlias = true

        mPressCountDownTimer = object : CountDownTimer(mDrawDuration, mDrawDuration / 360) {
            override fun onFinish() {
                if (mIsLongPress == 1) {
                    mIsLongPress = 2
                    mOnTouchListener?.onCancelTouchLongPress()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                mCurProgress = (360f - (millisUntilFinished / mDrawDuration.toFloat()) * 360f).toInt()
                mDrawTxt = "${(mCurProgress / 360F * 100F).toInt()}%"
                invalidate()
            }
        }
    }

    private var mStartX: Float = 0F
    private var mStartY: Float = 0F
    private var mIsLongPress: Int = 2 // 0 点击；1 长按； 2 空闲
    private var mDrawDuration: Long = 0L  // 绘制时间
    private var mOnTouchListener: OnCusTouchListener? = null
    private lateinit var mPressCountDownTimer: CountDownTimer

    private val mLongPressRunnable: Runnable = Runnable {
        mOnTouchListener?.onTouchLongPress()
        mIsLongPress = 1
        mPressCountDownTimer.start()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> { // 按下
                mStartX = event.x
                mStartY = event.y

                mIsLongPress = 0

                //按下超过500秒，进入长按状态
                postDelayed(mLongPressRunnable, 500)
            }
            MotionEvent.ACTION_UP -> { // 抬起
                // 移除长按
                removeCallbacks(mLongPressRunnable)

                if (mIsLongPress == 1) {
                    mPressCountDownTimer.cancel()
                    mOnTouchListener?.onCancelTouchLongPress()
                } else if (mIsLongPress == 0) {
                    mOnTouchListener?.onTouchClick()
                }

                mIsLongPress = 2
            }
            MotionEvent.ACTION_MOVE -> { // 移动

            }
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mOutWidth = measureWH(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getMode(widthMeasureSpec))
        mOutHeight = measureWH(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getMode(heightMeasureSpec))
        setMeasuredDimension(mOutWidth, mOutHeight)
    }

    private fun measureWH(size: Int, mode: Int): Int {
        return when (mode) {
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> {
                100
            }
            else -> {
                size
            }
        }
    }

    /**
     * curProgress 0 - 100
     */
    fun setCurProgress(curProgress: Int) {
        mCurProgress = ((curProgress / 100F) * 360).toInt()
        mDrawTxt = "$curProgress%"
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        mPaint.style = Paint.Style.FILL

        val w = width / 2
        val h = height / 2

        // 外圈
        mPaint.color = mOuterRoundColor
        canvas?.drawCircle(w.toFloat(), h.toFloat(), (mOutWidth / 2).toFloat(), mPaint)

        // 内圈
        mPaint.color = mInnerRoundColor
        canvas?.drawCircle(w.toFloat(), h.toFloat(), mOutWidth / 2 - mStrokeWidth, mPaint)

        // 边圈
        mPaint.color = mStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeWidth
        mPaint.strokeCap = Paint.Cap.ROUND
        mRectF.set(mStrokeWidth / 2, mStrokeWidth / 2, mOutWidth - mStrokeWidth / 2, mOutWidth - mStrokeWidth / 2)
        canvas?.drawArc(mRectF, -90F, mCurProgress.toFloat(), false, mPaint)

        // 中间文字
        mTxtPaint.color = mDrawTxtColor
        mTxtPaint.textSize = mDrawTxtSize
        mTxtPaint.textAlign = Paint.Align.CENTER

        val fm = mTxtPaint.fontMetrics
        canvas?.drawText(mDrawTxt, w.toFloat(), h - (fm.descent - (-fm.ascent + fm.descent) / 2), mTxtPaint)
    }

}