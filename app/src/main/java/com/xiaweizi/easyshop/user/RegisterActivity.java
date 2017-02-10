package com.xiaweizi.easyshop.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.xiaweizi.easyshop.R;
import com.xiaweizi.easyshop.commons.ActivityUtils;
import com.xiaweizi.easyshop.commons.RegexUtils;
import com.xiaweizi.easyshop.components.ProgressDialogFragment;
import com.xiaweizi.easyshop.network.EasyShopClient;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwdAgain)
    EditText etPwdAgain;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.activity_register)
    RelativeLayout activityRegister;

    private String userName;
    private String passWord;
    private String pwdAgain;
    private ActivityUtils mActivityUtils;
    private ProgressDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);

        initToolBar();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etUsername.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(textWatcher);
        etPwdAgain.addTextChangedListener(textWatcher);
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

        @Override
        public void afterTextChanged(Editable s) {
            userName = etUsername.getText().toString();
            passWord = etPwd.getText().toString();
            pwdAgain = etPwdAgain.getText().toString();

            btnRegister.setEnabled(!(TextUtils.isEmpty(userName) ||
                    TextUtils.isEmpty(passWord) ||
                    TextUtils.isEmpty(pwdAgain)));


        }
    };

    @OnClick(R.id.btn_register)
    public void onClick() {
        if (RegexUtils.verifyUsername(userName) != RegexUtils.VERIFY_SUCCESS) {
            mActivityUtils.showToast(R.string.username_rules);
            return;
        } else if (RegexUtils.verifyPassword(passWord) != RegexUtils.VERIFY_SUCCESS) {
            mActivityUtils.showToast(R.string.password_rules);
            return;
        } else if (!TextUtils.equals(passWord, pwdAgain)) {
            mActivityUtils.showToast(R.string.username_equal_pwd);
            return;
        }

        /*************************** 完成网络注册请求 ***************************/
        //1. 创建客户端
        //2. 构建请求
        //      2.1 添加url(服务器地址，接口)
        //      2.2 添加请求方式(GET,POST)
        //      2.3 添加请求头(根据服务器的要求添加，通常不需要)
        //      2.4 添加请求体(可以为空)
        //3. 客户端发送请求给服务器 ->响应
        //4. 解析响应
        //      4.1 判断是否连接成功(判断响应码)
        //      4.2 如果响应码是200 - 299 -> 取出响应体(解析, 展示)

//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        //设置值级别
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                //添加日志拦截器
//                .addInterceptor(interceptor)
//                .build();
//
//        RequestBody requestBody = new FormBody.Builder()
//                .add("username", userName)
//                .add("password", passWord)
//                .build();
//        final Request request = new Request.Builder()
//                .url("http://wx.feicuiedu.com:9094/yitao/UserWeb?method=register")
//                .post(requestBody)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LogUtils.e(e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                //拿到响应体，判断是否成功
//                if (response.isSuccessful()){
//                    //拿到响应体
//                    ResponseBody responseBody = response.body();
//                    LogUtils.i(responseBody.string());
//                }
//            }
//        });

        Call call = EasyShopClient.getInstance().register(userName, passWord);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    /***************************
     * 设置右滑退出
     ***************************/
    float XUp = 0;
    float XDown = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                XDown = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                XUp = event.getX();
                float XOffset = XUp - XDown;
                if (XOffset >= 50) {
                    finish();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
