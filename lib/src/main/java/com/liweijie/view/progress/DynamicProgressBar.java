package com.liweijie.view.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.liweijie.view.silmpleswitchbtn.R;
import com.liweijie.view.util.LiWeiJieUtil;

/**
 * Created by liweijie on 2016/10/13.
 * 动态的进度条
 */

public class DynamicProgressBar extends View {
    private static final int DEFAULT_COLOR = Color.parseColor("#e61e23");//默认颜色
    private static final int MAX_PROGRESS = 100;//默认，同时也是最大值
    private static final int DEFAULT_RATE = 30; // 默认更新速率

    private Paint mPaint;
    private int progress; // 总分数
    private int progressColor;// 颜色
    private int currentAlphaLevel;// 实时的透明度比例
    private int currentProgress;// 实时的长度
    private float textSize;// 字体大小
    private int rateProgress; //每隔30毫秒画一次，最多一百次
    private float specProgress; // 文字需要占整一个View的宽度
    //三种rgb的原值
    private int redProgress;
    private int greenProgress;
    private int blueProgress;
    private String textProgress; // 后边的文字
    private float proportion;// 总体比例，是一个float，小于等于1
    private int perProgress; // 每次更新的进度


    /**
     * 设置progress
     *
     * @param progress
     */
    public void setProgress(int progress) {
        setProgress(progress, true);
    }

    /**
     * @param progress
     * @param isNeedReload 整一个重置 ，适合哪种外部更新进度条的，比如下载
     */
    public void setProgress(int progress, boolean isNeedReload) {
        if (isNeedReload) {
            init(progress);
        } else {
            this.progress = progress;
        }
        update();
    }

    /**
     * 更新重绘进度条
     */
    private void update() {
        if (LiWeiJieUtil.isUIThread()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setColor(int color) {
        this.progressColor = color;
        update();
    }

    /**
     * Java代码直接new的使用
     *
     * @param context
     */
    public DynamicProgressBar(Context context) {
        this(context, MAX_PROGRESS);
    }

    /**
     * 动态添加View的时候可以使用该View
     *
     * @param context
     * @param progress
     */
    public DynamicProgressBar(Context context, int progress) {
        super(context);
        this.textSize = LiWeiJieUtil.getDimen(R.dimen.default_dynamic_text_size, getContext());
        setRGB(DEFAULT_COLOR);
        rateProgress = DEFAULT_RATE;
        specProgress = LiWeiJieUtil.getDimen(R.dimen.default_dynamic_spec, getContext());
        progressColor = DEFAULT_COLOR;
        perProgress = progress % 100;
        init(progress);
    }


    public DynamicProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    /**
     * 基本的初始化
     *
     * @param progress
     */
    private void init(int progress) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(textSize);
        setBackgroundColor(Color.TRANSPARENT);
        currentAlphaLevel = 1;
        currentProgress = 1;
        this.progress = progress;
        proportion = (float) (1.0 * progress / MAX_PROGRESS);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DynamicProgressBarStyle, defStyleAttr, 0);
        progress = array.getInteger(R.styleable.DynamicProgressBarStyle_dynamicProgress, MAX_PROGRESS);
        textSize = array.getDimension(R.styleable.DynamicProgressBarStyle_dynamicTextSize, LiWeiJieUtil.getDimen(R.dimen.default_dynamic_text_size, context));
        progressColor = array.getColor(R.styleable.DynamicProgressBarStyle_dynamicColor, DEFAULT_COLOR);
        setRGB(progressColor);
        rateProgress = array.getInteger(R.styleable.DynamicProgressBarStyle_dynamicRate, DEFAULT_RATE);
        int maxProgress = array.getInteger(R.styleable.DynamicProgressBarStyle_dynamicMax, DEFAULT_RATE);
        perProgress = maxProgress % 100;
        specProgress = array.getDimension(R.styleable.DynamicProgressBarStyle_dynamicSpec, LiWeiJieUtil.getDimen(R.dimen.default_dynamic_spec, context));
        textProgress = array.getString(R.styleable.DynamicProgressBarStyle_dynamicText);
        array.recycle();
        init(progress);
    }

    private void setRGB(int progressColor) {
        redProgress = Color.red(progressColor);
        greenProgress = Color.green(progressColor);
        blueProgress = Color.blue(progressColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画矩形
        canvas.drawColor(Color.TRANSPARENT);
        drawProgress(canvas);
        // 还需要画
        if (currentProgress < progress) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 每次变化的长度
                    currentProgress += perProgress;
                    // 每次变化的透明度
                    currentAlphaLevel += 2.55;
                    invalidate();
                }
            }, rateProgress);
        }
    }


    /**
     * 画进度条和文字
     *
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        // 计算需要花的位置
        mPaint.setAntiAlias(true);
        int color = Color.argb(0xFF - 0xFF * currentAlphaLevel, redProgress, greenProgress, blueProgress);
        mPaint.setColor(color);
        RectF rectF = new RectF();
        rectF.left = 0;
        rectF.top = 0;
        rectF.right = (getMeasuredWidth() - specProgress) * currentProgress * proportion / progress;
        rectF.bottom = getMeasuredHeight();
        canvas.drawRoundRect(rectF, 4, 4, mPaint);
        float textX = rectF.right + LiWeiJieUtil.getDimen(R.dimen.default_dynamic_spec, getContext());
        float textY = rectF.bottom;
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        mPaint.setColor(progressColor);
        //计算文字高度
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        //计算文字baseline
        float textBaseY = textY - (textY - fontHeight) / 2 - fontMetrics.bottom;
        canvas.drawText(String.valueOf(currentProgress) + textProgress, textX, textBaseY, mPaint);
    }

}
