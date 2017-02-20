package com.xiaweizi.easyshop.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.feicuiedu.apphx.model.HxUserManager;
import com.feicuiedu.apphx.model.event.HxErrorEvent;
import com.feicuiedu.apphx.model.event.HxEventType;
import com.feicuiedu.apphx.model.event.HxSimpleEvent;
import com.hyphenate.easeui.domain.EaseUser;
import com.xiaweizi.easyshop.R;
import com.xiaweizi.easyshop.commons.ActivityUtils;
import com.xiaweizi.easyshop.model.CachePreferences;
import com.xiaweizi.easyshop.model.CurrentUser;
import com.xiaweizi.easyshop.model.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mActivityUtils = new ActivityUtils(this);
        EventBus.getDefault()
                .register(this);

        /*
         * 账号冲突后,自动登出
         * 由apphx模块HxMainService发出
         * */
        if (getIntent().getBooleanExtra("AUTO_LOGIN", false)) {
            /*清除本地缓存的用户信息*/
            CachePreferences.clearAllData();
            /*踢出时,登出环信*/
            HxUserManager.getInstance().asyncLogout();
        }

        /*当前用户如果是已经登录过并未登出的,再次进入需要自动登录*/
        if (CachePreferences.getUser().getName() != null && !HxUserManager.getInstance().isLogin()) {
            User user = CachePreferences.getUser();
            EaseUser easeUser = CurrentUser.convert(user);
            HxUserManager.getInstance().asyncLogin(easeUser, user.getPassword());
            return;
        }


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault()
                .unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSimpleEvent event) {

        // 判断是否是登录成功事件
        if (event.type != HxEventType.LOGIN) return;
        mActivityUtils.startActivity(MainActivity.class);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent event) {

        // 判断是否是登录失败事件
        if (event.type != HxEventType.LOGIN) return;

        throw new RuntimeException("login error");
    }
}
