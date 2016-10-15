package com.liweijie.view.switchview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.liweijie.view.R;
import com.liweijie.view.util.LiWeiJieUtil;

import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by liweijie on 2016/10/16.
 */

public class AnimSwitchView extends View {
    private static final String TAG = TransitionSwitchView.class.getSimpleName();

    private static final int DEFAULT_ENABLE_COLOR = Color.parseColor("#ff0000");
    private static final int DEFAULT_DIS_COLOR = Color.parseColor("#333333");

    /**
     *选中圆颜色
     */
    private
    @ColorInt
    int enableCircleColor;
    /**
     * 选中圆颜色
     */
    private
    @ColorInt
    int disCircleColor;
    /**
     * 选中线条颜色
     */
    private
    @ColorInt
    int enableLineColor;
    /**
     * 未选中线条颜色
     */
    private
    @ColorInt
    int disLineColor;
    /**
     * 半径
     */
    private float radius;
    /**
     * 是否可选
     */
    private boolean isChecked;
    /**
     * 线宽度
     */
    private float lineWidth;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 回调
     */
    private OnCheckedListener changeListener;

    /**
     * 坐标x
     */
    private float circleX;
    /**
     * 坐标y
     */
    private float circleY;

    private float lineLeft;// 线坐标
    private float lineTop;// 线坐标
    private float lineRight;// 线坐标
    private float lineBottom;// 线坐标
    /**
     * View宽度
     */
    private float width;

    /**
     * 动画结束的位置
     */
    private float animEnd;
    /**
     * 每次更新的长度
     */
    private float perAnim;
    /**
     * 分开5次更新
     */
    private static final int SEPARATION_LENGHT = 5;//分开五次更新
    /**
     * 是否有动画
     */
    private boolean hasAnim;
    /**
     * 动画方向，true为向右，false为向左
     */
    private boolean animDirection;

    public AnimSwitchView(Context context) {
        this(context, null);
    }

    public AnimSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        circleX = isChecked ? (getWidth() - radius) : radius;
        circleY = getHeight() / 2;
        lineLeft = 0;
        lineBottom = lineTop = (float) (getMeasuredHeight() * 1.0 / 2);
        lineRight = getMeasuredWidth() - radius;
        width = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        super.onDraw(canvas);
        drawLine(canvas, isChecked);
        drawCircle(canvas, isChecked);
    }


    /**
     * 画线
     *
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
     * 画圆
     *
     * @param canvas
     * @param isChecked
     */
    private void drawCircle(Canvas canvas, boolean isChecked) {
        @ColorInt int currentColor = isChecked ? enableCircleColor : disCircleColor;
        mPaint.setColor(currentColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(circleX, circleY, radius, mPaint);
        if (hasAnim) {
            drawWithAnim();
        }
    }

    /**
     * 画动画
     */
    private void drawWithAnim() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                //右动画
                if (animDirection) {
                    if (circleX + perAnim >= animEnd) { //是否到终点
                        circleX = animEnd;
                        hasAnim = false;
                    } else {
                        circleX += perAnim;
                    }
                } else { //左动画
                    if (circleX - perAnim <= animEnd) {//是否到终点
                        circleX = animEnd;
                        hasAnim = false;
                    } else {
                        circleX -= perAnim;
                    }
                }
                update();
            }
        }, 20);// 20ms画一次

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                updateCircleX(event.getX(), action);
                break;
            case MotionEvent.ACTION_MOVE:
                updateCircleX(event.getX(), action);
                break;
            case ACTION_UP:
                // 结束事件
                updateCircleX(event.getX(), action);
                break;
        }
        return true;
    }
    private void updateCircleX(float x, int action) {
        isChecked = hasHalfWidth(x);// 判断是否过半
        if (action == MotionEvent.ACTION_DOWN) {
            hasAnim = calculateDownAnim(x);
        } else if (action == MotionEvent.ACTION_UP) {
            hasAnim = calculateUpAnim(x);
            if (changeListener != null) {
                changeListener.onCheckChange(this,isChecked);
            }
        } else {
            if (x >= width - radius) {
                circleX = width - radius;
            } else if (x < radius) {
                circleX = radius;
            } else {
                circleX = x;
            }
        }
        update();
    }

    /**
     * 是否有结束动画
     *
     * @param x
     * @return
     */
    private boolean calculateUpAnim(float x) {
        // 如果在边界
        animEnd = hasHalfWidth(x) ? width - radius : radius;
        perAnim = Math.abs(animEnd - circleX) / SEPARATION_LENGHT;
        animDirection = isChecked;
        return true;
    }

    /**
     * 是否有按下动画
     *
     * @param x
     * @return
     */
    private boolean calculateDownAnim(float x) {
        if (x >= circleX - radius && x <= circleX + radius) {
            if (x <= radius) {//在边界
                circleX = radius;
            } else if (x + radius >= width) { //在边界
                circleX = (width - radius);
            } else {
                circleX = x;
            }
            return false;
        }
        perAnim = Math.abs(animEnd - circleX)/SEPARATION_LENGHT;
        animDirection = isChecked;
        // 计算动画起始位置和每次长度
        if (x > circleX + radius) { // 右动画
            if (x >= width - radius) {
                animEnd = width - radius;
            } else {
                animEnd = x;
            }
        } else { //左动画
            if (x < radius) {
                animEnd = radius;
            } else {
                animEnd = x;
            }
        }
        return true;
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

    /**
     * 是否过半
     *
     * @param x
     * @return
     */
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
        void onCheckChange(AnimSwitchView view, boolean isChecked);
    }

}
