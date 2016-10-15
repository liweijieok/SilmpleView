[TOC]
# SilmpleView
个人的自定义View例子

## Progressbar

实现方式是通过完全的自定义View实现， 没有使用handler或者线程什么的，可以定义的属性有：

```
 <declare-styleable name="DynamicProgressBarStyle">
        <!--当前进度,进度条没有透明度时候的颜色,进度条更新速率(ms),文字大小，进度条最大值，文字宽度，文字-->
        <attr name="dynamicProgress" format="integer" />
        <attr name="dynamicColor" format="color|reference" />
        <attr name="dynamicRate" format="integer" />
        <attr name="dynamicTextSize" format="dimension|reference" />
        <attr name="dynamicMax" format="integer" />
        <attr name="dynamicSpec" format="dimension|reference" />
        <attr name="dynamicText" format="string|reference" />
    </declare-styleable>
```

效果图：

![](http://ocxgpwj6l.bkt.clouddn.com/GIF.gif)

## TransitionSwitchView
实现的方式是完全通过集成View，重写onTouchEvent事件实现，定义的属性有

```
 <declare-styleable name="TransitionSwitchViewStyle">
        <!--圆的两种状态颜色，线条两种状态颜色呢，圆的半径，线条宽度，是否已打开-->
        <attr name="transitionCircleEnableColor" format="color|reference"/>
        <attr name="transitionCircleDisColor" format="color|reference"/>
        <attr name="transitionLineEnableColor" format="color|reference"/>
        <attr name="transitionLineDisColor" format="color|reference"/>
        <attr name="transitionCircleRadius" format="dimension|reference"/>
        <attr name="transitionLineWidth" format="dimension|reference"/>
        <attr name="transitionIsChecked" format="boolean"/>
    </declare-styleable>

```

效果图：

![](http://ocxgpwj6l.bkt.clouddn.com/switch.gif)



