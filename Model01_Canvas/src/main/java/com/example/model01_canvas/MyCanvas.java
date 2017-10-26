package com.example.model01_canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/10/25.
 */

public class MyCanvas extends View {



    public MyCanvas(Context context) {
        this(context,null);
    }

    public MyCanvas(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, expandSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //这里重新设置测量的高度，否则外部嵌套ScrollView无内容
        setMeasuredDimension(width,4000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawColor(getResources().getColor(R.color.colorPrimary));
        //绘制点和点阵
        drawPoint(canvas);
        //绘制线条
        drawLine(canvas);
        //绘制矩形
        drawRect(canvas);
        //绘制椭圆
        drawRoundCircle(canvas);
        //绘制圆
        drawCircle(canvas);
        //绘制圆弧
        drawCircleARC01(canvas);
        drawCircleARC02(canvas);
        //测试Paint的几种风格效果
        testPaintStyle(canvas);
    }

    //绘制点和点阵
    private void drawPoint(Canvas canvas){
        Paint mPaint = new Paint();

        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        int x = getLeft() + getMeasuredWidth()/2;
        mPaint.setColor(Color.GREEN);
        canvas.drawPoint(x, 50, mPaint);     //在坐标(x,50)位置绘制一个点
        mPaint.setColor(Color.WHITE);
        canvas.drawPoints(new float[]{      //绘制一组点，坐标位置由float数组指定
                x,100,
                x,150,
                x,200
        },mPaint);
    }

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

        // 第五种
        roundRectF = new RectF(100,700,600,900);
        // 绘制背景矩形
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(roundRectF,mPaint);
        // 绘制圆角矩形
        mPaint.setColor(Color.GREEN);
        canvas.drawRoundRect(roundRectF,500,200,mPaint);
    }

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


    //绘制一个外部空心圆和内部实心圆
    private void drawCircle(Canvas canvas){
        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        int x = getLeft() + getMeasuredWidth()/2;
        int y = 1400;
        int radius = 90;
        //绘制一个圆心坐标在(x,y)，半径为90的空心圆
        canvas.drawCircle(x,y,radius,mPaint);
        //绘制一个圆心坐标在(x,y)，半径为90的实心圆
        mPaint.setStyle(Paint.Style.FILL);
        radius = 50;
        canvas.drawCircle(x,y,radius,mPaint);
    }
    //绘制圆弧
    private void drawCircleARC01(Canvas canvas){

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

    //绘制圆弧
    private void drawCircleARC02(Canvas canvas){

        Paint mPaint = new Paint();
        RectF rectF = new RectF(100,2400,600,2700);
        // 绘制背景矩形
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(rectF,mPaint);

        // 绘制圆弧
        mPaint.setColor(Color.RED);
        canvas.drawArc(rectF,0,90,false,mPaint);

        // 绘制背景矩形
        RectF rectF2 = new RectF(200,2800,500,3100);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(rectF2,mPaint);

        // 绘制圆弧
        mPaint.setColor(Color.RED);
        canvas.drawArc(rectF2,0,90,true,mPaint);
    }

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

}
