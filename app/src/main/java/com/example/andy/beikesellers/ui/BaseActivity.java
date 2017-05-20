package com.example.andy.beikesellers.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by andyma on 5/1/17.
 * 描述： Activity的基类(无需绑定布局)

 */

public class BaseActivity extends AppCompatActivity {

/*
* 主要做的事情：
*   1. 统一的属性
*   2. 统一的接口
*   3. 统一的方法
*
* */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //显示返回键

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    //菜单栏操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        响应Item操作
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
