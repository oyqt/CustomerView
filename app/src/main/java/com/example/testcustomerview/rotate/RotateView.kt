package com.example.testcustomerview.rotate

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.testcustomerview.R

/**
 * @author tll
 * @time 2019/10/16 15:24
 * @desc 翻转动画
 */
class RotateView : View {
    private lateinit var mPaint: Paint
    private var mRotate: Float = 0F
    private var mRatio: Int = 50
    private var mColor: Int = 0

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 6F

        mColor = ContextCompat.getColor(context, R.color.colorPrimary)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.color = mColor

        val w = width / 2
        val h = height / 2

        // 旋转画布
        canvas?.rotate(mRotate, w.toFloat(), h.toFloat())

        // 正方形
        canvas?.drawRect((w - mRatio / 2).toFloat(), (h - mRatio / 2).toFloat(), (w + mRatio / 2).toFloat(), (h + mRatio / 2).toFloat(), mPaint)

        // 圆形
        canvas?.drawCircle(w.toFloat(), (h - 2 * mRatio).toFloat(), mRatio.toFloat(), mPaint)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun start() {

        // 旋转动画
        val rotateAnimate = ValueAnimator.ofFloat(0F, 360F).apply {
            repeatCount = Animation.INFINITE
            addUpdateListener {
                mRotate = it.animatedValue as Float
                invalidate()
            }
        }

        // 放大动画
        val scaleAnimate = ValueAnimator.ofInt(mRatio, mRatio + 15).apply {
            interpolator = LinearInterpolator()
            repeatCount = -1
            repeatMode = ValueAnimator.RESTART
            addUpdateListener {
                mRatio = it.animatedValue as Int
            }
        }

        // 颜色变化
        val colorAnamate = ValueAnimator.ofArgb(ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorAccent)).apply {
            interpolator = LinearInterpolator()
            repeatCount = -1
            repeatMode = ValueAnimator.RESTART
            addUpdateListener {
                mColor = it.animatedValue as Int
            }
        }

        AnimatorSet().apply {
            duration = 3000
            play(scaleAnimate).with(colorAnamate).with(rotateAnimate)
            start()
        }
    }

    /**
    我在例子中用的都是ValueAnimator，其实还有其它相关类，比如ObjectAnimator改变透明度：
    ObjectAnimator animator = ObjectAnimator.ofFloat(textview, "alpha", 1f, 0f, 1f);
    animator.setDuration(5000);
    animator.start();

    另外还有ViewPropertyAnimator，用起来更方便，不过只有有限的方法，比如让view在x轴y轴都平衡500：
    view.animate().x(500).y(500).setDuration(5000).setInterpolator(new BounceInterpolator());

    最后最后，再说一个PropertyValuesHolder，它保存了动画过程中所需要操作的属性和对应的值，
    通常和Keyframe一起使用。像实现一个View抖动动画时，你用上面的需要写很多重复的动画进行串联起来，
    但用Keyframe就可以很好的一次性把动画描述清楚。Keyframe其实就是动画的关键帧。举个抖动的实现例子：

    Keyframe frame0 = Keyframe.ofFloat(0f, 0);
    Keyframe frame1 = Keyframe.ofFloat(0.1f, -20f);
    Keyframe frame2 = Keyframe.ofFloat(1, 0);
    PropertyValuesHolder frameHolder = PropertyValuesHolder.ofKeyframe("rotation",frame0,frame1,frame2);
    Animator animator = ObjectAnimator.ofPropertyValuesHolder(mImage,frameHolder);
    animator.setDuration(1000);
    animator.start();
     */
}