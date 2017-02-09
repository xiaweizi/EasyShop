package com.xiaweizi.easyshop.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xiaweizi.easyshop.R;
import com.xiaweizi.easyshop.commons.ActivityUtils;
import com.xiaweizi.easyshop.components.ProgressDialogFragment;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    
    private ActivityUtils mActivityUtils;
    private ProgressDialogFragment dialogFragment;
    private String userName;
    private String passWord;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        initToolBar();//初始化ToolBar
    }

    private void initToolBar() {
        //给左上角加一个返回图标，需要重写菜单的点击事件
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etPwd.addTextChangedListener(textWatcher);
        etUsername.addTextChangedListener(textWatcher);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        //表示最终内容
        @Override
        public void afterTextChanged(Editable s) {
            userName = etUsername.getText().toString();
            passWord = etPwd.getText().toString();
            //判断用户名密码是否为空
            boolean canLogin = !(TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord));
            btnLogin.setEnabled(canLogin);
        }
    };

    @OnClick({R.id.btn_login, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mActivityUtils.showToast("执行登陆的网络请求");

                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                //设置值级别
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient client = new OkHttpClient
                        .Builder()
                        .addInterceptor(interceptor)
                        .build();

                RequestBody requestBody = new FormBody.Builder()
                        .add("username", userName)
                        .add("password", passWord)
                        .build();

                Request request = new Request.Builder()
                        .url("http://wx.feicuiedu.com:9094/yitao/UserWeb?method=login")
                        .post(requestBody)
                        .build();

                client.newCall(request)
                        .enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                            }
                        });

                break;
            case R.id.tv_register:
                mActivityUtils.startActivity(RegisterActivity.class);
                break;
        }
    }

    /*************************** 设置右滑退出 ***************************/
    float XUp = 0;
    float XDown = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                XDown = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                XUp = event.getX();
                float XOffset = XUp - XDown;
                if (XOffset >= 50){
                    finish();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
