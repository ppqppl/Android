package com.example.hp.game_not_step_white;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class View_Game extends View implements View.OnClickListener{
    private int mScreenheight;
    private int mScreenwidth;
    private int mRectheight;
    private int mRectwidth;
    private int Grade=0;
    private int Speed=5;
    private String text;
    private Paint mPaint;
    private Paint mPaint2;
    private Random mRandom;
    private ArrayList<RectsPostion> mArraylist_Rect;
    private RectsPostion rectPostion;
    private boolean IfYouLose=false;
    private boolean IfStart=false;

    public View_Game(Context context) {
        super(context);
    }
    public View_Game(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化
     */
    public void init() {
        mPaint=new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);

        mPaint2=new Paint();
        mPaint2.setColor(Color.BLUE);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeWidth(10);
        mPaint2.setTextSize(50);

        mRandom=new Random();
        mArraylist_Rect=new ArrayList<>();
        int rem=mRandom.nextInt(4);
        rectPostion=new RectsPostion();
        rectPostion.setmLeftPos(rem*(mScreenwidth/4));
        rectPostion.setmRightPos((rem+1)*(mScreenwidth/4));
        rectPostion.setmTopPos(-2*(mScreenwidth/4));
        rectPostion.setmBottomPos(0*(mScreenwidth/4));
        mArraylist_Rect.add(rectPostion);

        Grade=0;
        Speed=5;
    }

    /**
     * 获取屏幕手机数据
     * @param height
     * @param width
     */
    public void setScreenNum(int height,int width) {
        this.mScreenheight =height;
        this.mScreenwidth =width;
        mRectheight=mScreenwidth/4;
        mRectwidth=mScreenwidth/4;
    }
    public void DrawAllRects(Canvas canvas) {
        for(int i = 0; i< mArraylist_Rect.size(); i++)
        {
            /**
             * 坑，必须每次都初始化一次。。。
             */
            rectPostion=new RectsPostion();
            rectPostion= mArraylist_Rect.get(i);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rectPostion.getmLeftPos(),rectPostion.getmTopPos(),
                    rectPostion.getmRightPos(),rectPostion.getmBottomPos(),mPaint);
        }
    }

    /**
     * 循环方块，一个消失，得有出现的补上
     */
    public void RectMove() {
        if(mArraylist_Rect.get(mArraylist_Rect.size()-1).getmTopPos()>=mScreenheight)
        {
            mArraylist_Rect.remove(mArraylist_Rect.size()-1);IfYouLose=true;
        }
        if(mArraylist_Rect.get(0).getmTopPos()>=0)
        {
            int rand=mRandom.nextInt(4);
            rectPostion=new RectsPostion();
            rectPostion.setmLeftPos(rand * mRectwidth);
            rectPostion.setmTopPos(0 - 2*mRectheight);
            rectPostion.setmRightPos(mRectheight * (rand + 1));
            rectPostion.setmBottomPos(0);
            mArraylist_Rect.add(0, rectPostion);
        }
        for(int i = 0; i< mArraylist_Rect.size(); i++)
        {
            mArraylist_Rect.get(i).setmBottomPos(mArraylist_Rect.get(i).getmBottomPos()+Speed);
            mArraylist_Rect.get(i).setmTopPos(mArraylist_Rect.get(i).getmTopPos()+Speed);
        }
    }

    public void DrawLineY(Canvas canvas){
        for(int i=1;i<4;i++)
        {
            canvas.drawLine(mScreenwidth/4*i,0,
                    mScreenwidth/4*i,0+mScreenheight,mPaint);
        }
        canvas.drawLine(0,mRectheight*2,
                mScreenwidth+0,mRectheight*2,mPaint);
    }
    public void DrawGrade(Canvas canvas) {
         text=String.valueOf(Grade);
        canvas.drawText(text,66,mScreenheight/7*6,mPaint2);
    }
    public void DrawLose(Canvas canvas){
        mPaint2.setTextSize(200);
        mPaint2.setStrokeWidth(10);
        canvas.drawText("您输了",mScreenwidth/2-250,500,mPaint2);
    }
    public void DrawIfStart(Canvas canvas){
        mPaint2.setTextSize(100);
        mPaint2.setStrokeWidth(10);
        canvas.drawText("请点击开始",mScreenwidth/2-250,500,mPaint2);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        DrawGrade(canvas);
        DrawLineY(canvas);
        DrawAllRects(canvas);
        if(IfYouLose)
        {
            DrawLose(canvas);
        }
        else
        {
            if(IfStart)
            {
                RectMove();
            }
            else
            {
                DrawIfStart(canvas);
            }
            invalidate();
        }
        super.onDraw(canvas);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float X=event.getX();
        float Y=event.getY();
        for(int i=0;i<mArraylist_Rect.size();i++)
        {
            if(mArraylist_Rect.get(i).getmLeftPos()<=X
                    &&mArraylist_Rect.get(i).getmRightPos()>=X
                    &&mArraylist_Rect.get(i).getmTopPos()<=Y
                    &&mArraylist_Rect.get(i).getmBottomPos()>=Y
                    &&mArraylist_Rect.size()!=1)
            {
                mArraylist_Rect.remove(i);
                Grade+=1;
                if(Grade>=5&&Grade<10)Speed=10;
                else if(Grade>=10&&Grade<15)Speed=13;
                else if(Grade>=15&&Grade<20)Speed=16;
                else if(Grade>=20&&Grade<25)Speed=19;
                else if(Grade>=25&&Grade<30)Speed=22;
                else if(Grade>=30&&Grade<=45)Speed=25;
                else if(Grade>=45&&Grade<=60)Speed=28;
                else if(Grade>=60&&Grade<=80)Speed=31;
                else if(Grade>=80&&Grade<=100)Speed=15;
                else if(Grade>=100&&Grade<=120)Speed=37;
                else if(Grade>=120&&Grade<=140)Speed=41;
                else if(Grade>=140&&Grade<=160)Speed=45;
                else if(Grade>=160&&Grade<=180)Speed=59;
                else if(Grade>=180)Speed=55;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 控制状态，是否开始与重来
     * @param view
     */
    @Override
    public void onClick(View view) {
       if(!IfStart)
       {
           IfStart=true;
       }
        if(IfYouLose)
       {
           IfYouLose=false;
           IfStart=false;
           init();
           invalidate();
       }
    }
}
