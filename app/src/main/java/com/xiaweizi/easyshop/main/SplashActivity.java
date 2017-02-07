package com.xiaweizi.easyshop.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xiaweizi.easyshop.R;
import com.xiaweizi.easyshop.commons.ActivityUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mActivityUtils = new ActivityUtils(this);

        // TODO: 2017/2/7 环信登陆相关(账号冲突提出)
        // TODO: 2017/2/7 判断用户是否登陆，自动登陆


        /*************************** 1.5s后跳转到主页 ***************************/
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //跳转到主页
                mActivityUtils.startActivity(MainActivity.class);
                finish();
            }
        }, 1500);
    }
}
