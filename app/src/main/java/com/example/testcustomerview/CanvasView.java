package com.example.testcustomerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author tll
 * @time 2019/2/16 14:13
 * @desc ${des}
 */
public class CanvasView extends View {

    private Paint mPaint;
    private Path mPath;

    private String mStr;

    public CanvasView(Context context) {
        super(context);
        initPaint();
    }

    public CanvasView(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public void setStr(String str) {
        mStr = str;
        invalidate();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.RED);
        mPath.moveTo(0, 0);
        mPath.lineTo(120, 0);
        mPath.lineTo(120, 60);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(28);
        float w = mPaint.measureText(mStr);
        canvas.drawText(mStr, 60 - w / 2 + 22, 30, mPaint);
    }

}
