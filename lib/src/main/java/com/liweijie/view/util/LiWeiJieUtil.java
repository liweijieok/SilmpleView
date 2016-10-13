package com.liweijie.view.util;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.DimenRes;

/**
 * Created by liweijie on 2016/10/13.
 * 工具类
 */

public class LiWeiJieUtil {

    /**
     *  是否是UI线程
     * @return
     */
    public static boolean isUIThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     *  获取像素值
     * @param resId
     * @param context
     * @return
     */
    public static float getDimen(@DimenRes int resId, Context context) {
        return resId == -1 ? 0 : context.getResources().getDimension(resId);
    }
}
