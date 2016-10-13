package com.liweijie.view.silmpleswitchbtn;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by liweijie on 2016/10/6.
 *
 */

public class MySwitchButton extends RelativeLayout implements View.OnClickListener {
    private ImageView imgv_handler = null; //用于显示Switch按钮
    private Paint pat_line = null; //用于画出Switch按钮底下的线
    private boolean statue = false; //用于标示当前SwitchButton所处的状态，false为未激活状态，true为激活状态

    private ObjectAnimator slideLeftAnimator = null; //按钮向左滑动的动画
    private ObjectAnimator slideRightAnimator = null; //按钮向右滑动的动画
    private TransitionDrawable transitionDrawable = null; //按钮切换图片资源的动画
    private LayoutParams params;
    private DecelerateInterpolator decelerateInterpolator;

    public MySwitchButton(Context context) {
        this(context, null);
    }

    public MySwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //初始化按钮
        imgv_handler = new ImageView(context);
        imgv_handler.setImageDrawable(getResources().getDrawable(R.drawable.switch_button_drawable));
        this.addView(imgv_handler);
        //用八位色值将背景设为透明
        this.setBackgroundColor(Color.parseColor("#00fafafa"));
        pat_line = new Paint();
        pat_line.setColor(Color.parseColor("#333333"));
        pat_line.setStyle(Paint.Style.STROKE);
        //因为setStrokeWidth只能设置像素数，所以用这个方法将3dip转化为对应的像素数
        pat_line.setStrokeWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        //获取图片切换动画待后面使用
        transitionDrawable = (TransitionDrawable) imgv_handler.getDrawable();
        this.setOnClickListener(this);
        decelerateInterpolator = new DecelerateInterpolator();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置按钮图片对应的参数
        Log.i("MySwitchBtn", "onMeasure" + getMeasuredHeight() + "==getMeasuredHeight() " +
                " #### " + getMeasuredWidth() + "=getMeasuredWidth");
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
            params = new LayoutParams(getMeasuredHeight(), getMeasuredHeight());
        params.addRule(ALIGN_PARENT_LEFT, TRUE);
        imgv_handler.setLayoutParams(params);
        //设置右滑动动画
        slideRightAnimator = ObjectAnimator.ofFloat(imgv_handler, "translationX", 0, getMeasuredWidth() - getMeasuredHeight());
        slideRightAnimator.setInterpolator(decelerateInterpolator); //此处为动画插值器，用于设置动画曲线，不懂的同学百度一下~~~
        slideRightAnimator.setDuration(300);
        //设置做滑动动画
        slideLeftAnimator = ObjectAnimator.ofFloat(imgv_handler, "translationX", getMeasuredWidth() - getMeasuredHeight(), 0);
        slideLeftAnimator.setInterpolator(decelerateInterpolator);
        slideLeftAnimator.setDuration(300);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#00fafafa"));
        int startX = getMeasuredHeight() / 2;
        int startY = getMeasuredHeight() / 2;
        int endX = getMeasuredWidth() - getMeasuredHeight() / 2;
        int endY = getMeasuredHeight() / 2;
        if (statue) {
            pat_line.setColor(Color.parseColor("#ff0000"));
        } else {
            pat_line.setColor(Color.parseColor("#333333"));
        }
        canvas.drawLine(startX, startY, endX, endY, pat_line);
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View view) {
        if (!statue) {
            slideRightAnimator.start();
            transitionDrawable.startTransition(300); //将资源从item1转化为item2
        } else {
            slideLeftAnimator.start();
            transitionDrawable.reverseTransition(300); //将资源从item2转化为item1
        }
        statue = !statue;
        invalidate();
    }

}
