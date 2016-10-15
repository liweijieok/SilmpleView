package com.liweijie.view.silmpleswitchbtn;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.liweijie.view.util.LiWeiJieUtil;

/**
 * Created by liweijie on 2016/10/15.
 * <p>
 * 根据手势移动的SwitchView
 * </p>
 */

public class TransitionSwitchView extends View {
    private static final String TAG = TransitionSwitchView.class.getSimpleName();

    private static final int DEFAULT_ENABLE_COLOR = Color.parseColor("#ff0000");
    private static final int DEFAULT_DIS_COLOR = Color.parseColor("#333333");

    /**
     * 圆可用颜色
     */
    private
    @ColorInt
    int enableCircleColor;

    /**
     * 圆没有打开颜色
     */
    private
    @ColorInt
    int disCircleColor;

    /**
     * 线可用颜色
     */
    private
    @ColorInt
    int enableLineColor;

    /**
     * 线没有打开颜色
     */
    private
    @ColorInt
    int disLineColor;
    /**
     * 半径
     */
    private float radius;

    /**
     * 是否是选中状态
     */
    private boolean isChecked;

    /**
     * 线的高度
     */
    private float lineWidth;

    /**
     * 画笔
     */
    private Paint mPaint;

    private OnCheckedListener changeListener;

    /**
     * 坐标x
     */
    private int circleX;
    /**
     * 坐标y
     */
    private int circleY;

    private int lineLeft;
    private int lineTop;
    private int lineRight;
    private int lineBottom;
    /**
     * 宽度
     */
    private float width;

    public TransitionSwitchView(Context context) {
        this(context, null);
    }

    public TransitionSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransitionSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TransitionSwitchViewStyle, defStyleAttr, 0);
        enableCircleColor = array.getColor(R.styleable.TransitionSwitchViewStyle_transitionCircleEnableColor, DEFAULT_ENABLE_COLOR);
        disCircleColor = array.getColor(R.styleable.TransitionSwitchViewStyle_transitionCircleDisColor, DEFAULT_DIS_COLOR);
        enableLineColor = array.getColor(R.styleable.TransitionSwitchViewStyle_transitionLineEnableColor, DEFAULT_ENABLE_COLOR);
        disLineColor = array.getColor(R.styleable.TransitionSwitchViewStyle_transitionLineDisColor, DEFAULT_DIS_COLOR);
        isChecked = array.getBoolean(R.styleable.TransitionSwitchViewStyle_transitionIsChecked, false);
        radius = array.getDimension(R.styleable.TransitionSwitchViewStyle_transitionCircleRadius, 10);
        lineWidth = array.getDimension(R.styleable.TransitionSwitchViewStyle_transitionLineWidth, 10);
        array.recycle();
        mPaint = new Paint();
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        circleX = isChecked ? (int) (getWidth() - radius) : (int) radius;
        circleY = getHeight() / 2;
        lineLeft = 0;
        lineBottom = lineTop = getMeasuredHeight() / 2;
        lineRight = (int) (getMeasuredWidth() - radius);
        width = getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas, isChecked);
        drawCircle(canvas, isChecked);
    }

    /**
     *  画线
     * @param canvas
     * @param isChecked
     */
    private void drawLine(Canvas canvas, boolean isChecked) {
        @ColorInt int currentColor = isChecked ? enableLineColor : disLineColor;
        mPaint.setColor(currentColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(lineWidth);
        canvas.drawLine(lineLeft, lineTop, lineRight, lineBottom, mPaint);
    }

    /**
     *  画圆
     * @param canvas
     * @param isChecked
     */
    private void drawCircle(Canvas canvas, boolean isChecked) {
        @ColorInt int currentColor = isChecked ? enableCircleColor : disCircleColor;
        mPaint.setColor(currentColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(circleX, circleY, radius, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                updateCircleX(event.getX(), false);
                break;
            case MotionEvent.ACTION_MOVE:
                updateCircleX(event.getX(), false);
                break;
            case MotionEvent.ACTION_UP:
                // 结束事件
                updateCircleX(event.getX(), true);
                break;
        }
        return true;
    }

    /** 更新x坐标
     * @param x
     * @param isActionUp
     */
    private void updateCircleX(float x, boolean isActionUp) {
        if (isActionUp) {
            isChecked = hasHalfWidth(x);
            circleX = (int) (hasHalfWidth(x)?(width - radius):radius);
            if (changeListener != null) {
                changeListener.onCheckChange(this,isChecked);
            }
        } else if (x <= radius) {    // 保证不会画出边界
            circleX = (int) radius;
            isChecked = false;
        } else if (x >= getMeasuredWidth() - radius) {
            circleX = (int) (getMeasuredWidth() - radius);
            isChecked = true;
        } else {
            // 判断x是否过半
            isChecked = hasHalfWidth(x);
            circleX = (int) x;
        }
        update();
    }

    /**
     * 重绘
     */
    public void update() {
        if (LiWeiJieUtil.isUIThread()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    private boolean hasHalfWidth(float x) {
        return x > (width / 2);
    }


    /**
     * 设置状态
     *
     * @param isChecked
     */
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        update();
    }

    /**
     * 回调监听
     *
     * @param listener
     */
    public void setChangeListener(OnCheckedListener listener) {
        this.changeListener = listener;
    }



    // 回调监听
    public interface OnCheckedListener {
        void onCheckChange(TransitionSwitchView view, boolean isChecked);
    }
}
