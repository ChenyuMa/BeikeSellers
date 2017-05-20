package com.example.andy.beikesellers.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.andy.beikesellers.R;

import java.util.ArrayList;

public class IDTypeActivity extends AppCompatActivity implements View.OnClickListener {

    private int last_item = -1;
    private TextView oldView;

    private Button buttonidtype;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idtype);


        InitView();
    }

    private void InitView() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("国内身份证");
        list.add("港澳通行证");
        list.add("护照");
        list.add("军官证");

        final ListView listView = (ListView) findViewById(R.id.listviewIdtype);


        buttonidtype = (Button) findViewById(R.id.btn_idtypesure);
        buttonidtype.setOnClickListener(this);
        adapter = new MyAdapter(list);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView item = (TextView) view;

                item.setBackgroundResource(R.drawable.bg_article_listview_item_pressed);//把当前选中的条目加上选中效果
                if (last_item != -1 && last_item != position) {//如果已经单击过条目并且上次保存的item位置和当前位置不同
                    // oldView.setBackgroundColor(Color.WHITE);
                    oldView.setBackgroundResource(R.drawable.article_listview_item_bg);//把上次选中的样式去掉
                }
                oldView = item;//把当前的条目保存下来
                last_item = position;//把当前的位置保存下来
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_idtypesure:

                Intent intent = new Intent();
                intent = intent.setClass(IDTypeActivity.this, AuthenticationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("selectIdtype", oldView.getText().toString());
                intent.putExtras(bundle);
                IDTypeActivity.this.setResult(RESULT_OK, intent);   //RESULT_OK是返回状态码
                IDTypeActivity.this.finish();
                break;
        }
    }

    private final class MyAdapter extends BaseAdapter {
        TextView textView = null;
        private ArrayList<String> list = null;
        private int defItem;
        public MyAdapter(ArrayList<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                textView = new TextView(IDTypeActivity.this);
            } else {
                textView = (TextView) convertView;
            }
            if (last_item == position) {//½â¾ö»¬¶¯listviewµÄÊ±ºò,Ñ¡ÖÐµÄÌõÄ¿Ñ¡ÖÐÐ§¹ûÏûÊ§ÎÊÌâ
                // textView.setBackgroundColor(Color.BLUE);
                textView.setBackgroundResource(R.drawable.bg_article_listview_item_pressed);
            } else {
                // textView.setBackgroundColor(Color.WHITE);
                textView.setBackgroundResource(R.drawable.article_listview_item_bg);
            }
            textView.setText(list.get(position));
            textView.setTextSize(50);
            return textView;
        }


    }


}

