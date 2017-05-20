package com.example.andy.beikesellers.application;

import android.app.Application;

import com.example.andy.beikesellers.utils.StaticClass;

import cn.bmob.v3.Bmob;

/**
 *
 * Created by andyma on 5/1/17.
 * 文件属性： 从项目启动到结束
 * 如何设置此注释格式  --- File->Setting-> Code -> Include
 */

public class baseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);




    }
}
