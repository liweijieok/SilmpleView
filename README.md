# SilmpleView
个人的自定义View例子

## Progressbar

实现方式是通过完全的自定义View实现， 没有使用handler或者线程什么的，可以定义的属性有：

```
<declare-styleable name="DynamicProgressBarStyle">
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

