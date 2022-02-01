package com.example.c_custom_myview_Sign_V2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.LinkedList;

/*
 * 目的：銀行簽名程式自訂View
 * 銀行簽名程式,線一定是很多條因為筆畫,所以我們在用一個LinkedList,包住每一條線
 * 每次畫一條下去就是存一條，然後再一條新的所以
 * setFirstPoint ＝>每次down下去,就儲存一個新的line線儲存x,y,在掛到整個很多lines裡面
 * setMovePoint => 移動時取得你在down時最新的那條線接著,把x,y存進去
 * onDraw => 1.新的取得多條線方法,尋訪lines裡面有多條線,在取得各自的線
 * onDraw => 2.在取得每條線的上一個點跟新得點onDrawLine去畫線
 * 畫線程式完成後,新增清除功能,新增回上一動功能unDo
 * 因為clean方法,已經清空資料,所以invalidate();,重新畫圖onDraw時資料是0不會進去畫圖
 * onDo,必須加判斷是有資料時再去刪除最後一條畫圖,不然每資料也unDo,invalidate()時,重新畫會近迴圈閃退
 * */
public class MyUnderline extends View {
    private LinkedList<LinkedList<HashMap<String, Float>>> lines;
    private Paint paint;

    public MyUnderline(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.GRAY);//1.設定背景顏色
        lines = new LinkedList<>();    //2.準備存放多條線
        initPaint();
    }

    //設定畫筆
    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //6.新的取得多條線方法,尋訪lines裡面有多條線,在取得各自的線
        for (LinkedList<HashMap<String, Float>> line : lines) {
            //7.取得一條線,畫上去
            for (int i = 1; i < line.size(); i++) {
                HashMap<String, Float> p0 = line.get(i - 1); //前一個點的x,y
                HashMap<String, Float> p1 = line.get(i);     //當前點的x,y
                canvas.drawLine(
                        p0.get("x"),
                        p0.get("y"),
                        p1.get("x"),
                        p1.get("y"),
                        paint
                );
            }
        }

        //舊的取得一條線方法
//        for (int i = 1; i < line.size(); i++) {
//            HashMap<String, Float> p0 = line.get(i - 1); //前一個點的x,y
//            HashMap<String, Float> p1 = line.get(i);     //當前點的x,y
//            canvas.drawLine(
//                    p0.get("x"),
//                    p0.get("y"),
//                    p1.get("x"),
//                    p1.get("y"),
//                    paint
//            );
//        }
    }

    //3.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setFirstPoint(event);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            setMovePoint(event);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

        }
        return true;
    }

    //對外開放的功能
    //8.新增清除全部線功能
    public void clean() {
        lines.clear();//將所有的線清楚
        invalidate(); //再重新畫一次圖,因為onDraw時,lines裡面沒有資料所以當然不會跑for each去畫線
    }

    //9.新增清除最後那一條線功能,效果就會重複上一動
    public void undo() {
        if (lines.size() > 0) {  //如果有線才讓使用者undo,防閃退
            lines.removeLast();//清除最後一條線
            invalidate();      //重新畫圖
        }
    }

    //4.每次down下去,就儲存一個新的line線儲存x,y,在掛到整個很多lines裡面
    private void setFirstPoint(MotionEvent event) {
        LinkedList<HashMap<String, Float>> line = new LinkedList<>();//每次down下去都是新的一條線,在這裡用區域變數每次都存一條新得
        float ex = event.getX();
        float ey = event.getY();
        HashMap<String, Float> point = new HashMap<>();
        point.put("x", ex);
        point.put("y", ey);
        line.add(point);//一條線儲存一個點
        lines.add(line);//很多線儲存一條線
    }

    //5.//移動時取得你在down時最新的那條線接著,把x,y存進去
    private void setMovePoint(MotionEvent event) {
        float ex = event.getX();
        float ey = event.getY();
        HashMap<String, Float> point = new HashMap<>();
        point.put("x", ex);
        point.put("y", ey);
        lines.getLast().add(point);//移動時取得你最新的那條線接著,把x,y存進去
        invalidate();//更新視圖
    }
}
