package com.rixin.cold;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        /** 设置Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_other);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_info_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        setSupportActionBar(toolbar);   //设置后无法响应NavigationIcon的点击事件

        initView();
    }

    private void initView(){
        /** 设置Title */
        TextView mTitle = (TextView) this.findViewById(R.id.tv_app_other_title);
        String title = this.getIntent().getStringExtra("title");
        mTitle.setText(title);
    }

}
