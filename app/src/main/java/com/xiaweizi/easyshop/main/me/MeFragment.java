package com.xiaweizi.easyshop.main.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.view.CircularImageView;
import com.xiaweizi.easyshop.R;
import com.xiaweizi.easyshop.commons.ActivityUtils;
import com.xiaweizi.easyshop.components.AvatarLoadOptions;
import com.xiaweizi.easyshop.main.me.goodload.GoodsUpLoadActivity;
import com.xiaweizi.easyshop.main.me.person.PersonActivity;
import com.xiaweizi.easyshop.main.me.persongoods.PersonGoodsActivity;
import com.xiaweizi.easyshop.model.CachePreferences;
import com.xiaweizi.easyshop.network.EasyShopApi;
import com.xiaweizi.easyshop.user.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiaweizi.easyshop.R.id.iv_user_head;
import static com.xiaweizi.easyshop.R.id.tv_login;

public class MeFragment extends Fragment {

    @BindView(R.id.iv_user_head)
    CircularImageView ivUserHead;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_person_info)
    TextView tvPersonInfo;
    @BindView(R.id.tv_person_goods)
    TextView tvPersonGoods;
    @BindView(R.id.tv_goods_upload)
    TextView tvGoodsUpload;
    private ActivityUtils mActicityUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        ButterKnife.bind(this, view);
        mActicityUtils = new ActivityUtils(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (CachePreferences.getUser().getName() == null) return;
        tvLogin.setText(CachePreferences.getUser().getNick_name());
        /*设置头像*/
        ImageLoader.getInstance()
                .displayImage(EasyShopApi.IMAGE_URL + CachePreferences.getUser().getHead_Image(),
                        ivUserHead, AvatarLoadOptions.build());
    }

    @OnClick({iv_user_head, R.id.tv_person_info, tv_login, R.id.tv_person_goods, R.id.tv_goods_upload})
    public void onClick(View view) {
        if (CachePreferences.getUser().getName() == null) {
            mActicityUtils.startActivity(LoginActivity.class);
            return;
        }

        switch (view.getId()) {
            case iv_user_head:

            case tv_login:

            case R.id.tv_person_info:
                mActicityUtils.startActivity(PersonActivity.class);
                break;
            case R.id.tv_person_goods:
                // 跳转到我的商品页面
                mActicityUtils.startActivity(PersonGoodsActivity.class);
                break;

            case R.id.tv_goods_upload:
                mActicityUtils.startActivity(GoodsUpLoadActivity.class);
        }
    }

}
