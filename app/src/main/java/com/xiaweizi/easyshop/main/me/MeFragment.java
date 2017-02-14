package com.xiaweizi.easyshop.main.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaweizi.easyshop.R;
import com.xiaweizi.easyshop.commons.ActivityUtils;
import com.xiaweizi.easyshop.main.me.person.PersonActivity;
import com.xiaweizi.easyshop.model.CachePreferences;
import com.xiaweizi.easyshop.user.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeFragment extends Fragment {

    private ActivityUtils mActicityUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        ButterKnife.bind(this, view);
        mActicityUtils = new ActivityUtils(this);
        return view;
    }

    @OnClick({R.id.iv_user_head, R.id.tv_person_info, R.id.tv_login, R.id.tv_person_goods, R.id.tv_goods_upload})
    public void onClick(View view) {
        if (CachePreferences.getUser().getName() == null){
            mActicityUtils.startActivity(LoginActivity.class);
            return;
        }

        switch (view.getId()){
            case R.id.iv_user_head:

            case R.id.tv_login:

            case R.id.tv_person_info:
                // TODO: 2017/2/14 进入个人信息界面
                mActicityUtils.startActivity(PersonActivity.class);

            case R.id.tv_person_goods:
                // TODO: 2017/2/14 进入个人商品页面

            case R.id.tv_goods_upload:
                // TODO: 2017/2/14 跳转到商品上传界面
        }
    }

}
