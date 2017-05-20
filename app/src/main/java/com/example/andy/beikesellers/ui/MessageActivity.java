package com.example.andy.beikesellers.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andy.beikesellers.MainActivity;
import com.example.andy.beikesellers.R;


import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etc_phone;
    private Button btn_sendmessage;
    private EditText etc_messagecode;
    private Button btn_authentiation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();

    }

    private void initView() {
        etc_phone = (EditText) findViewById(R.id.et_phone);
        btn_sendmessage = (Button) findViewById(R.id.btn_sendMessage);
        etc_messagecode = (EditText) findViewById(R.id.et_messagecode);
        btn_authentiation = (Button) findViewById(R.id.btn_authentication);

        btn_sendmessage.setOnClickListener(this);
        btn_authentiation.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        String phone = etc_phone.getText().toString();
        String messagecode = etc_messagecode.getText().toString();
        Log.e("Message: ", "1");
        switch (v.getId()) {
            case R.id.btn_sendMessage:
                Log.e("Message: ", "2");
                if (etc_phone.length() != 11) {
                    Toast.makeText(this, "请输入11位有效手机号码", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Message: ", "3");
                    BmobSMS.requestSMSCode(phone, "欢迎注册焙可", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer smsId, BmobException ex) {
                            Log.e("Message: ", "4");
                            if (ex == null) {//验证码发送成功
                                Log.i("smile", "短信id：" + smsId);//用于查询本次短信发送详情
                                btn_sendmessage.setClickable(false);
                                btn_sendmessage.setBackgroundColor(Color.GRAY);
                                Toast.makeText(MessageActivity.this, "验证码发送成功，请尽快使用", Toast.LENGTH_SHORT).show();
                                new CountDownTimer(60000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        btn_sendmessage.setBackgroundResource(R.drawable.button_shape02);
                                        btn_sendmessage.setText(millisUntilFinished / 1000 + "秒");
                                    }

                                    @Override
                                    public void onFinish() {
                                        Log.e("Message: ", "6");
                                        btn_sendmessage.setClickable(true);
                                        btn_sendmessage.setBackgroundResource(R.drawable.button_shape);
                                        btn_sendmessage.setText("重新发送");
                                    }
                                }.start();
                                Log.e("Message: ", "4");
                            } else {
                                Toast.makeText(MessageActivity.this, "验证码发送失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                break;
            case R.id.btn_authentication:
                Log.e("Message:", "5");

                if (etc_phone.length() == 0 || etc_messagecode.length() == 0 || etc_phone.length() != 11) {
                    Log.e("MESSAGE:", "6");
                    Toast.makeText(this, "手机号或验证码输入不合法", Toast.LENGTH_SHORT).show();
                } else {


                    BmobSMS.verifySmsCode(phone, messagecode, new UpdateListener() {

                        @Override
                        public void done(BmobException ex) {
                            if (ex == null) {//短信验证码已验证成功
                                Toast.makeText(MessageActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                Log.i("smile", "验证通过");
                                startActivity(new Intent(MessageActivity.this, AuthenticationActivity.class));

                            } else {
                                Toast.makeText(MessageActivity.this, "失败", Toast.LENGTH_SHORT).show();
                                Log.i("smile", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                            }
                        }
                    });


                }
                break;
        }
    }

}