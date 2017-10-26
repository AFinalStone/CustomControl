### 安卓自定义View - Canvas之绘制图形

在上一篇自定义View分类与流程中我们了解自定义View相关的基本知识，不过，这些东西依旧还是理论，并不能拿来(zhuang)用(B), 这一次我们就了解一些能(zhaung)用(B)的东西。

在本篇文章中，我们先了解Canvas的基本用法，最后用一个小示例来结束本次教程。

#### 一.Canvas简介

Canvas我们可以称之为画布，能够在上面绘制各种东西，是安卓平台2D图形绘制的基础，非常强大。

一般来说，比较基础的东西有两大特点:

- 1.可操作性强：由于这些是构成上层的基础，所以可操作性必然十分强大。

- 2.比较难用：各种方法太过基础，想要完美的将这些操作组合起来有一定难度。
不过不必担心，本系列文章不仅会介绍到Canvas的操作方法，还会简单介绍一些设计思路和技巧。

#### 二.Canvas的常用操作速查表
	
操作类型 | 相关API	| 备注
---|---|---
绘制颜色 | drawColor, drawRGB, drawARGB | 使用单一颜色填充整个画布
绘制基本形状  | drawPoint, drawPoints, drawLine, drawLines, drawRect, drawRoundRect, drawOval, drawCircle, drawArc	| 依次为 点、线、矩形、圆角矩形、椭圆、圆、圆弧
绘制图片	| drawBitmap, drawPicture | 	绘制位图和图片
绘制文本	| drawText, drawPosText, drawTextOnPath	| 依次为 绘制文字、绘制文字时指定每个文字位置、根据路径绘制文字
绘制路径	| drawPath	| 绘制路径，绘制贝塞尔曲线时也需要用到该函数
顶点操作	| drawVertices, drawBitmapMesh	| 通过对顶点操作可以使图像形变，drawVertices直接对画布作用、 drawBitmapMesh只对绘制的Bitmap作用
画布剪裁	| clipPath, clipRect	| 设置画布的显示区域
画布快照	| save, restore, saveLayerXxx, restoreToCount, getSaveCount	| 依次为 保存当前状态、 回滚到上一次保存的状态、 保存图层状态、 回滚到指定状态、 获取保存次数
画布变换	| translate, scale, rotate, skew	| 依次为 位移、缩放、 旋转、错切
Matrix(矩阵)| 	getMatrix, setMatrix, concat	| 实际上画布的位移，缩放等操作的都是图像矩阵Matrix， 只不过Matrix比较难以理解和使用，故封装了一些常用的方法。

PS： Canvas常用方法在上面表格中已经全部列出了，当然还存在一些其他的方法未列出，[具体可以参考官方文档 Canvas](https://developer.android.google.cn/reference/android/graphics/Canvas.html)

#### 三.Canvas详解

本篇内容主要讲解如何利用Canvas绘制基本图形。

绘制颜色：
绘制颜色是填充整个画布，常用于绘制底色。

```
        //绘制背景
        canvas.drawColor(getResources().getColor(R.color.colorPrimary));
```

![](picture/1.png)

创建画笔：
要想绘制内容，首先需要先创建一个画笔，如下：

```
// 1.创建一个画笔
private Paint mPaint = new Paint();

// 2.初始化画笔
private void initPaint() {
	mPaint.setColor(Color.BLACK);       //设置画笔颜色
	mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
	mPaint.setStrokeWidth(10f);         //设置画笔宽度为10px
}

// 3.在构造函数中初始化
public SloopView(Context context, AttributeSet attrs) {
   super(context, attrs);
   initPaint();
}
```

在创建完画笔之后，就可以在Canvas中绘制各种内容了。

- 绘制点：

可以绘制一个点，也可以绘制一组点，如下：

```
    //绘制点和点阵
    private void drawPoint(Canvas canvas){
        Paint mPaint = new Paint();

        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        int x = getLeft() + getMeasuredWidth()/2;
        mPaint.setColor(Color.GREEN);
        canvas.drawPoint(x, 50, mPaint);     //在坐标(x,450)位置绘制一个点
        mPaint.setColor(Color.WHITE);
        canvas.drawPoints(new float[]{      //绘制一组点，坐标位置由float数组指定
                x,100,
                x,150,
                x,200
        },mPaint);
    }
```

![](picture/2.png)

关于坐标原点默认在左上角，水平向右为x轴增大方向，竖直向下为y轴增大方向。

- 绘制直线：

绘制直线需要两个点，初始点和结束点，同样绘制直线也可以绘制一条或者绘制一组：

```
    //绘制线条
    private void drawLine(Canvas canvas){
        Paint mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        canvas.drawLine(getLeft(),250,getRight(),280,mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawLines(new float[]{               // 绘制一组线 每四数字(两个点的坐标)确定一条线
                100,300,400,300,
                100,350,600,350
        },mPaint);
    }
```

![](picture/3.png)

- 绘制矩形：

确定确定一个矩形最少需要四个数据，就是对角线的两个点的坐标值，这里一般采用左上角和右下角的两个点的坐标。

关于绘制矩形，Canvas提供了三种重载方法，第一种就是提供四个数值(矩形左上角和右下角两个点的坐标)来确定一个矩形进行绘制。 其余两种是先将矩形封装为Rect或RectF(实际上仍然是用两个坐标点来确定的矩形)，然后传递给Canvas绘制，如下：

```
    //绘制矩形
    private void drawRect(Canvas canvas){

        Paint mPaint = new Paint();
        mPaint.setColor(Color.YELLOW);
        // 第一种
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(100,400,600,500,mPaint);

        // 第二种
        Rect rect = new Rect(100,400,600,500);
        canvas.drawRect(rect,mPaint);

        // 第三种
        RectF rectF = new RectF(100f,400,600f,500);
        canvas.drawRect(rectF,mPaint);

        // 第四种
        // 绘制圆角矩形
        RectF roundRectF = new RectF(100f,550,600f,650);
        canvas.drawRoundRect(roundRectF,30,30,mPaint);
        //通过坐标来绘制圆角矩形，这个方法是在SDK21之后添加的,所以我们一般使用的都是上面的那种方式
        //canvas.drawRoundRect(100,100,800,400,30,30,mPaint);

    }

    
```

以上三种方法所绘制出来的结果是完全一样的。

![](picture/4.png)

看到这里,相信很多观众会产生一个疑问，为什么会有Rect和RectF两种？两者有什么区别吗？

答案当然是存在区别的，两者最大的区别就是精度不同，Rect是int(整形)的，而RectF是float(单精度浮点型)的。除了精度不同，两种提供的方法也稍微存在差别，在这里我们暂时无需关注，想了解更多参见官方文档 Rect 和 RectF

- 关于绘制圆角矩形：

下面简单解析一下圆角矩形的几个必要的参数的意思。

很明显可以看出，第二种方法前四个参数和第一种方法的RectF作用是一样的，都是为了确定一个矩形，最后一个参数Paint是画笔，无需多说，与矩形相比，圆角矩形多出来了两个参数rx 和 ry，这两个参数是干什么的呢？

稍微分析一下，既然是圆角矩形，他的角肯定是圆弧(圆形的一部分)，我们一般用什么确定一个圆形呢？

答案是圆心 和 半径，其中圆心用于确定位置，而半径用于确定大小。

由于矩形位置已经确定，所以其边角位置也是确定的，那么确定位置的参数就可以省略，只需要用半径就能描述一个圆弧了。

但是，半径只需要一个参数，但这里怎么会有两个呢？

好吧，让你发现了，这里圆角矩形的角实际上不是一个正圆的圆弧，而是椭圆的圆弧，这里的两个参数实际上是椭圆的两个半径，他们看起来个如下图

![](picture/5.)

红线标注的 rx 与 ry 就是两个半径，也就是相比绘制矩形多出来的那两个参数。

我们了解到原理后，就可以为所欲为了，通过计算可知我们上次绘制的矩形宽度为700，高度为300，当你让 rx大于350(宽度的一半)， ry大于150(高度的一半) 时奇迹就出现了， 你会发现圆角矩形变成了一个椭圆， 他们画出来是这样的 ( 为了方便确认我更改了画笔颜色， 同时绘制出了矩形和圆角矩形 )：

```
        // 第五种
        roundRectF = new RectF(100,700,600,900);
        // 绘制背景矩形
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(roundRectF,mPaint);
        // 绘制圆角矩形
        mPaint.setColor(Color.GREEN);
        canvas.drawRoundRect(roundRectF,500,200,mPaint);
```

其中黄色部分是我们所选定的矩形，而里面的圆角矩形则变成了一个椭圆，实际上在rx为宽度的一半，ry为高度的一半时，刚好是一个椭圆，通过上面我们分析的原理推算一下就能得到，而当rx大于宽度的一半，ry大于高度的一半时，实际上是无法计算出圆弧的，所以drawRoundRect对大于该数值的参数进行了限制(修正)，凡是大于一半的参数均按照一半来处理。

- 绘制椭圆：

相对于绘制圆角矩形，绘制椭圆就简单的多了，因为他只需要一个矩形矩形作为参数:

```
    //绘制椭圆
    private void drawRoundCircle(Canvas canvas){

        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);

        // 第一种
        RectF rectF = new RectF(100,1000,600,1200);
        canvas.drawOval(rectF,mPaint);

        // 第二种，API21的时候才添加上方法
        //canvas.drawOval(100,1000,600,1200,mPaint);
    }
```

![](picture/6.png)

绘制椭圆实际上就是绘制一个矩形的内切图形，原理如下，就不多说了：

![](picture/7.jpg)

>PS： 如果你传递进来的是一个长宽相等的矩形(即正方形)，那么绘制出来的实际上就是一个圆。

- 绘制圆：

绘制圆形也比较简单, 如下：

```
    //绘制一个外部空心圆和内部实心圆
    private void drawCircle(Canvas canvas){
        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        int x = getLeft() + getMeasuredWidth()/2;
        int y = 1400;
        int radius = 90;
        canvas.drawCircle(x,y,radius,mPaint);
        //绘制内部实心圆
        mPaint.setStyle(Paint.Style.FILL);
        radius = 50;
        canvas.drawCircle(x,y,radius,mPaint);
    }
```

![](picture/8.png)

绘制圆形有四个参数，前两个是圆心坐标，第三个是半径，最后一个是画笔。

- 绘制圆弧：

绘制圆弧就比较神奇一点了，为了理解这个比较神奇的东西，我们先看一下它需要的几个参数：

```
// 第一种
public void drawArc(@NonNull RectF oval, float startAngle, float sweepAngle, boolean useCenter, @NonNull Paint paint){}
    
// 第二种
public void drawArc(float left, float top, float right, float bottom, float startAngle,
            float sweepAngle, boolean useCenter, @NonNull Paint paint) {}
```

从上面可以看出，相比于绘制椭圆，绘制圆弧还多了三个参数：

```
startAngle  // 开始角度
sweepAngle  // 扫过角度
useCenter   // 是否使用中心
```

通过字面意思我们基本能猜测出来前两个参数(startAngle， sweepAngel)的作用，就是确定角度的起始位置和扫过角度， 不过第三个参数是干嘛的？试一下就知道了,上代码：

```
    //绘制圆弧
    private void drawARC(Canvas canvas){

        Paint mPaint = new Paint();
        RectF rectF = new RectF(100,1600,600,1900);
        // 绘制背景矩形
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(rectF,mPaint);

        // 绘制圆弧
        mPaint.setColor(Color.RED);
        canvas.drawArc(rectF,0,90,false,mPaint);
        RectF rectF2 = new RectF(100,2000,600,2300);

        // 绘制背景矩形
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(rectF2,mPaint);

        // 绘制圆弧
        mPaint.setColor(Color.RED);
        canvas.drawArc(rectF2,0,90,true,mPaint);
    }
```

上述代码实际上是绘制了一个起始角度为0度，扫过90度的圆弧，两者的区别就是是否使用了中心点，结果如下：

![](picture/9.png)

可以发现使用了中心点之后绘制出来类似于一个扇形，而不使用中心点则是圆弧起始点和结束点之间的连线加上圆弧围成的图形。这样中心点这个参数的作用就很明显了，不必多说想必大家试一下就明白了。 另外可以关于角度可以参考一下这篇文章： 角度与弧度

相比于使用椭圆，我们还是使用正圆比较多的，使用正圆展示一下效果：

![](picture/10.png)

- 简要介绍Paint

看了上面这么多，相信有一部分人会产生一个疑问，如果我想绘制一个圆，只要边不要里面的颜色怎么办？

很简单，绘制的基本形状由Canvas确定，但绘制出来的颜色,具体效果则由Paint确定。

如果你注意到了的话，在一开始我们设置画笔样式的时候是这样的：

```
mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
```

为了展示方便，容易看出效果，之前使用的模式一直为填充模式，实际上画笔有三种模式，如下：
```
STROKE                //描边
FILL                  //填充
FILL_AND_STROKE       //描边加填充
为了区分三者效果我们做如下实验：
```


为了区分三者效果我们做如下实验：

```
    private void testPaintStyle(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStrokeWidth(40);     //为了实验效果明显，特地设置描边宽度非常大


        int x = getLeft() + getMeasuredWidth()/2;
        int y = 3300;
        int radius = 80;
        // 描边
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle( x, y,radius, paint);

        // 填充
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle( x, y+200,radius, paint);

        // 描边加填充
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle( x, y+400, radius, paint);
    }
```

![](picture/11.png)

一图胜千言，通过以上实验我们可以比较明显的看出三种模式的区别，如果只需要边缘不需要填充内容的话只需要设置模式为描边(STROKE)即可。

其实关于Paint的内容也是有不少的，这些只是冰山一角，在后续内容中会详细的讲解Paint。

















