package com.liweijie.view.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.liweijie.view.R;
import com.liweijie.view.util.CommentUtil;


/**
 * 作者：黎伟杰-子然 on 2017/4/14.
 * 邮箱：liweijie@linghit.com
 * description：
 * update by:
 * update day:
 */
public class CustomRatingBar extends View {
    //单个高
    private float singleWidth;
    //单个宽
    private float singleHeight;
    //间距
    private int padding;
    //总数量
    private int size = 5;
    //默认激活数量
    private int activeSize = 3;
    //激活的bitmap
    private Bitmap activeBitmap;
    //没有激活的bitmap
    private Bitmap disactiveBitmap;
    //画笔
    private Paint mPaint;
    //步长
    private int stepSize;
    //开始画的x坐标
    private int drawStartX;
    //开始画的y坐标
    private int drawStartY;


    public CustomRatingBar(Context context) {
        this(context, null);

    }

    public CustomRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int activeId = 0;
        int disactiveId = 0;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomRatingStyle);
            singleWidth = array.getDimensionPixelOffset(R.styleable.CustomRatingStyle_custom_rate_width, 0);
            singleHeight = array.getDimensionPixelOffset(R.styleable.CustomRatingStyle_custom_rate_height, 0);
            activeId = array.getResourceId(R.styleable.CustomRatingStyle_custom_rate_active_drawable, R.drawable.custom_rateingbar_active);
            disactiveId = array.getResourceId(R.styleable.CustomRatingStyle_custom_rate_disactive_drawable, R.drawable.custom_rateingbar_disactive);
            size = array.getInteger(R.styleable.CustomRatingStyle_custom_rate_size, 5);
            activeSize = array.getInteger(R.styleable.CustomRatingStyle_custom_rate_active_size, 3);
            padding = array.getDimensionPixelOffset(R.styleable.CustomRatingStyle_custom_rate_padding, 10);
            array.recycle();
        }
        activeBitmap = BitmapFactory.decodeResource(getResources(), activeId);
        disactiveBitmap = BitmapFactory.decodeResource(getResources(), disactiveId);
        stepSize = padding + activeBitmap.getWidth();
        mPaint = new Paint();
        if (singleHeight <= 0) {
            singleHeight = activeBitmap.getHeight();
        }
        if (singleWidth <= 0) {
            singleWidth = activeBitmap.getWidth();
        }
        //对Bitmap进行压缩，因为设计只给了一张图
        if (activeBitmap.getWidth() != disactiveBitmap.getWidth() || activeBitmap.getHeight() != disactiveBitmap.getHeight()) {
            //把dis压缩或者是放大的active的大小
            disactiveBitmap = Bitmap.createScaledBitmap(disactiveBitmap, activeBitmap.getWidth(), activeBitmap.getHeight(), false);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        //计算宽
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = 5 * stepSize;
        }
        //计算高
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) singleHeight;
        }
        //总共应该的宽
        int drawWidth = (int) (size * singleWidth + (size <= 1 ? 0 : (size - 1) * padding));
        //开始的y位置
        int drawHeight = (int) ((height - singleHeight) / 2);
        drawStartX = (width - drawWidth) / 2;
        drawStartY = drawHeight > 0 ? drawHeight : 0;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //开始画active
        for (int i = 0; i < activeSize; i++) {
            canvas.drawBitmap(activeBitmap, drawStartX + i * stepSize, drawStartY, mPaint);
        }
        //开始画disactive
        for (int i = activeSize; i < size; i++) {
            canvas.drawBitmap(disactiveBitmap, drawStartX + i * stepSize, drawStartY, mPaint);
        }
    }

    /**
     * 设置激活的数量
     *
     * @param activeSize
     */
    public void setActiveSize(int activeSize) {
        this.activeSize = activeSize;
        if (CommentUtil.isUIThread()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }


}

