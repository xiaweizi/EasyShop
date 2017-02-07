package com.xiaweizi.easyshop.main.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaweizi.easyshop.R;
import com.xiaweizi.easyshop.commons.ActivityUtils;
import com.xiaweizi.easyshop.user.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ljkj on 2017/2/7.
 */

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

    @OnClick({R.id.tv_person_info, R.id.tv_login, R.id.tv_person_goods, R.id.tv_goods_upload})
    public void onClick(View view) {
        mActicityUtils.startActivity(LoginActivity.class);
        // TODO: 2017/2/7 0007 需要判断是否登录，从而决定跳转位置
    }

}
