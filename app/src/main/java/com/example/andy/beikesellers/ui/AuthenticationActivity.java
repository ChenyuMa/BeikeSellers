package com.example.andy.beikesellers.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andy.beikesellers.MainActivity;
import com.example.andy.beikesellers.R;
import com.example.andy.beikesellers.entity.SellerInfo;
import com.example.andy.beikesellers.utils.L;
import com.example.andy.beikesellers.view.CustomDialog;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.kymjs.rxvolley.toolbox.RxVolleyContext.toast;


public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tv_Idtype;
    private String content = "";

    //圆形头像
    private CircleImageView identifyHands;

    private CustomDialog dialog;


    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;
    private Button btn_submit;


    private EditText Idname;
    private EditText IdNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Log.i("Authentication01: ", "1");
        ListenView();


    }

    private void ListenView() {
        tv_Idtype = (TextView) findViewById(R.id.et_Idtype);
        tv_Idtype.setOnClickListener(this);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        dialog = new CustomDialog(AuthenticationActivity.this, 0, 0,
                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        //提示框以外点击无效
        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        identifyHands = (CircleImageView) findViewById(R.id.identifyHands);
        identifyHands.setOnClickListener(this);


        dialog.setCancelable(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_Idtype:
                Log.i("ID", "1");
                Intent intent = new Intent();
                intent = intent.setClass(AuthenticationActivity.this, IDTypeActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.identifyHands:
                Log.i("Dialog: ", "1");
                dialog.show();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.btn_submit:
                submitImage();
                startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));

                break;
        }
    }


    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;

    private File tempFile = null;

    //跳转相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用的话就进行储存
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);//数据的返回
        dialog.dismiss();
    }

    //在startActivityForResult(myintent) 等这个myintent对应的activity返回后，就回到原来的acticity中调用onActivityResult()
    //作用： 用于接受返回值
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != AuthenticationActivity.RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());

                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，我们原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }

        }
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            identifyHands.setImageBitmap(bitmap);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存
//        UtilTools.putImageToShare(AuthenticationActivity.this, identifyHands);

    }


    private void submitImage() {
        final String filePath ;

        final BmobFile file = new BmobFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)); //创建BmobFile对象，转换为Bmob对象
        file.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    String filePath = file.getFileUrl();
                    SubmitSellerInfo(filePath);
                    toast("上传文件成功:" + file.getFileUrl());

                } else {
                    toast("上传文件失败：" + e.getMessage());
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }

        });


    }

    private void SubmitSellerInfo(String image1) {

        Idname = (EditText) findViewById(R.id.et_Idname);
        IdNum = (EditText) findViewById(R.id.et_IdNum);

        String idType = tv_Idtype.getText().toString();
        String idname = Idname.getText().toString();
        String idNum = IdNum.getText().toString();

        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setIdType(idType);
        sellerInfo.setName(idname);
        sellerInfo.setIdNum(idNum);
        sellerInfo.setImage1(image1);
        sellerInfo.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
//                    toast("创建数据成功：" + objectId);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }


}