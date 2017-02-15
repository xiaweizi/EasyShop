package com.xiaweizi.easyshop.user.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.xiaweizi.easyshop.R;
import com.xiaweizi.easyshop.commons.ActivityUtils;
import com.xiaweizi.easyshop.commons.MyConstants;
import com.xiaweizi.easyshop.commons.SPUtils;
import com.xiaweizi.easyshop.components.ProgressDialogFragment;
import com.xiaweizi.easyshop.user.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView{

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

    private SPUtils spUtils;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);
        spUtils = new SPUtils(MyConstants.SP_LOGIN);

        setLastUserName();//设置上次登陆的用户名

        initToolBar();//初始化ToolBar
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    private void setLastUserName() {
        String lastUserName = spUtils.getString(MyConstants.LAST_LOGIN_USERNAME);
        etUsername.setText(lastUserName);
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
                presenter.login(userName, passWord);
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

    @Override
    public void showPrb() {
        mActivityUtils.hideSoftKeyboard();
        if (dialogFragment == null) dialogFragment = new ProgressDialogFragment();
        if (dialogFragment.isVisible()) return;
        dialogFragment.show(getSupportFragmentManager(), "progress_dialog_fragment");
    }

    @Override
    public void hidePrb() {
        dialogFragment.dismiss();
    }

    @Override
    public void loginSuccess() {
        finish();
    }

    @Override
    public void loginFailed() {
//        mActivityUtils.startActivity(MainActivity.class);
//        finish();
    }

    @Override
    public void showMsg(String msg) {
        mActivityUtils.showToast(msg);
    }
}
