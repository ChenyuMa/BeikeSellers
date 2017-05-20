package com.example.andy.beikesellers.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.andy.beikesellers.R;
import com.example.andy.beikesellers.entity.MyUser;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private CircleImageView profile_image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        findView(view);
        getDataFromDB();
        return view;
    }

    private void getDataFromDB() {
        MyUser user = new MyUser();
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.getObject("6PqcaaaG", new QueryListener<MyUser>() {
            @Override
            public void done(MyUser object, BmobException e) {
                if(e==null){
                    //获得playerName的信息
                    object.getDesc();

                    Log.i("HomeFragment1:", String.valueOf(object.getAge()));
                    Log.i("HomeFragment2:", object.getDesc());

                    //获得数据的objectId信息
                    object.getObjectId();
                    //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                    object.getCreatedAt();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

    }

    //初始化View
    private void findView(View view) {
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //退出登录
        }
    }



    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
        }
    }

}
