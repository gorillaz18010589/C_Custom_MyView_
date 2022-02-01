package com.example.c_custom_myview_Sign_V2;
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

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MyUnderline underline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        underline = findViewById(R.id.underline);
    }

    //清除所有線
    public void clean(View view) {
        underline.clean();
    }

    //清除最後一條線,就是返回上一動
    public void undo(View view) {
        underline.undo();
    }
}