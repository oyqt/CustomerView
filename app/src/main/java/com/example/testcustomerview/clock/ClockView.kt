package com.example.testcustomerview.clock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.example.testcustomerview.DensityUtils


/**
 * @author tll
 * @time 2019/5/14 10:14
 * @desc ${时钟}
 */
class ClockView : View {
    // 默认大小dp
    private val DEFAULT_SIZE: Int = 320

    // 时钟画笔
    private var mClockPaint: Paint? = null
    // 文字画笔
    private var mTextPaint: Paint? = null

    private val mRect = Rect()

    // 表盘边缘线的宽
    private var mAroundStockWidth: Int = 12
    // 表盘边缘颜色
    private var mAroundColor: Int = Color.parseColor("#083476")
    //表盘中心点颜色
    private var mClockCenterColor = Color.parseColor("#008577")

    // 字体大小
    private var mTextSize: Float = 28f

    private var hour: Float = 0f
    private var minute: Float = 0f
    private var second: Float = 0f

    constructor(context: Context?) : this(context, null) {
        initClockPaint()
        initTextPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {
        initClockPaint()
        initTextPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initClockPaint()
        initTextPaint()
    }

    private fun initClockPaint() {
        mClockPaint = Paint()
        mClockPaint?.style = Paint.Style.STROKE
        mClockPaint?.isAntiAlias = true
        mClockPaint?.strokeWidth = mAroundStockWidth.toFloat()
    }

    private fun initTextPaint() {
        mTextPaint = Paint()
        mTextPaint?.style = Paint.Style.FILL
        mTextPaint?.isAntiAlias = true
        mTextPaint?.isDither = true
        mTextPaint?.strokeWidth = 12f
        mTextPaint?.textAlign = Paint.Align.CENTER
        mTextPaint?.textSize = mTextSize
    }


    private fun getSize(measureSpec: Int, defaultSize: Int): Int {
        return when (val mode = MeasureSpec.getMode(measureSpec)) {
            MeasureSpec.AT_MOST -> {
                Math.min(MeasureSpec.getSize(measureSpec), defaultSize)
            }
            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(measureSpec)
            }
            else -> {
                defaultSize
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val defaultSize = DensityUtils.dp2px(context, DEFAULT_SIZE.toFloat())
        var width = getSize(widthMeasureSpec, defaultSize.toInt())
        var height = getSize(heightMeasureSpec, defaultSize.toInt())
        width = Math.min(width, height)
        height = Math.min(width, height)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        // 中心点
        val pointWH = width / 2.0f
        // 内圆半径
        val radiusIn = pointWH - mAroundStockWidth

        canvas?.translate(pointWH, pointWH)

        // 绘制表盘
        if (mAroundStockWidth > 0) {
            mClockPaint?.strokeWidth = mAroundStockWidth.toFloat()
            mClockPaint?.style = Paint.Style.STROKE
            mClockPaint?.color = mAroundColor
            canvas?.drawCircle(0f, 0f, pointWH - mAroundStockWidth / 2.0f, mClockPaint)
        }
        // 绘制背景
        mClockPaint?.style = Paint.Style.FILL
        mClockPaint?.color = Color.WHITE
        canvas?.drawCircle(0f, 0f, radiusIn, mClockPaint)

        // 绘制短线
        canvas?.save()
        canvas?.rotate(-90f)
        val longLineLength = radiusIn / 16.0f
        val longStartY = radiusIn - longLineLength
        val longStopY = longStartY - longLineLength
        val longStockWidth = 2.0f
        val temp = longLineLength / 4.0f
        val shortStartY = longStartY - temp
        val shortStopY = longStopY + temp
        val shortStockWidth = longStockWidth / 2.0f
        mClockPaint?.color = Color.BLACK
        val degrees = 6f
        var i = 0
        while (i <= 360) {
            if (i % 30 == 0) {
                mClockPaint?.strokeWidth = longStockWidth
                canvas?.drawLine(0f, longStartY, 0f, longStopY, mClockPaint)
            } else {
                mClockPaint?.strokeWidth = shortStockWidth
                canvas?.drawLine(0f, shortStartY, 0f, shortStopY, mClockPaint)
            }
            canvas?.rotate(degrees)
            i += degrees.toInt()
        }
        canvas?.restore()

        // 绘制时钟数据
        if (mTextSize > 0) {
            var x: Float
            var y: Float
            var i = 1
            while (i <= 12) {
                mTextPaint?.getTextBounds(i.toString(), 0, i.toString().length, mRect)
                val textHeight = mRect.height()
                val distance = radiusIn - 2 * longLineLength - textHeight
                val tempVa = i * 30.0f * Math.PI / 180.0f
                x = distance * Math.sin(tempVa).toFloat()
                y = -distance * Math.cos(tempVa).toFloat()
                canvas?.drawText(i.toString(), x, y + textHeight / 3, mTextPaint)
                i += 1
            }
        }

        canvas?.rotate(-90f)

        mClockPaint?.strokeWidth = 2f
        //绘制时针
        canvas?.save()
        canvas?.rotate(hour / 12.0f * 360.0f)
        canvas?.drawLine(-30f, 0f, radiusIn / 2.0f, 0f, mClockPaint)
        canvas?.restore()
        //绘制分针
        canvas?.save()
        canvas?.rotate(minute / 60.0f * 360.0f)
        canvas?.drawLine(-30f, 0f, radiusIn * 0.7f, 0f, mClockPaint)
        canvas?.restore()
        //绘制秒针
        mClockPaint?.color = Color.parseColor("#fff2204d")
        canvas?.save()
        canvas?.rotate(second / 60.0f * 360.0f)
        canvas?.drawLine(-30f, 0f, radiusIn * 0.85f, 0f, mClockPaint)
        canvas?.restore()
        //绘制中心小圆点
        mClockPaint?.style = Paint.Style.FILL
        mClockPaint?.color = mClockCenterColor
        canvas?.drawCircle(0f, 0f, radiusIn / 20.0f, mClockPaint)
    }


}