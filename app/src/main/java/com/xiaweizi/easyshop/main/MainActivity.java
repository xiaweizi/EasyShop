package com.xiaweizi.easyshop.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.feicuiedu.apphx.presentation.contact.list.HxContactListFragment;
import com.feicuiedu.apphx.presentation.conversation.HxConversationListFragment;
import com.xiaweizi.easyshop.R;
import com.xiaweizi.easyshop.commons.ActivityUtils;
import com.xiaweizi.easyshop.main.me.MeFragment;
import com.xiaweizi.easyshop.main.shop.ShopFragment;
import com.xiaweizi.easyshop.model.CachePreferences;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_title)
    TextView mainTitle;
    @BindView(R.id.main_toobar)
    Toolbar mainToobar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindViews({R.id.tv_shop, R.id.tv_message, R.id.tv_mail_list, R.id.tv_me})
    TextView[] textViews;
    private ActivityUtils mActivityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(MainActivity.this);

        initView();//初始化视图
    }

    private void initView() {
        if (CachePreferences.getUser().getName() == null) {
            viewpager.setAdapter(unLoginAdapter);
            viewpager.setCurrentItem(0);
        } else {
            viewpager.setAdapter(fragmentLoginAdapter);
            viewpager.setCurrentItem(0);
        }


        //刚刚进来默认选择市场
        textViews[0].setSelected(true);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (TextView textView : textViews) {
                    textView.setSelected(false);
                }
                //更改title，设置选择效果
                mainTitle.setText(textViews[position].getText());
                textViews[position].setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /*************************** 底部栏点击事件 ***************************/
    @OnClick({R.id.tv_shop,R.id.tv_message,R.id.tv_mail_list,R.id.tv_me})
    public void onClick(TextView view){

        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setSelected(false);
            textViews[i].setTag(i);
        }
        //设置选择效果
        view.setSelected(true);
        //参数false代表瞬间切换，而不是平滑过度
        viewpager.setCurrentItem((Integer) view.getTag(), false);
        //设置一下toolbar的title
        mainTitle.setText(textViews[(Integer) view.getTag()].getText());
    }

    private FragmentPagerAdapter fragmentLoginAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ShopFragment();
                case 1:
                    return new HxConversationListFragment();
                case 2:
                    return new HxContactListFragment();
                case 3:
                    return new MeFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    private FragmentStatePagerAdapter unLoginAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new ShopFragment();
                case 1:
                    return new UnLoginFragment();
                case 2:
                    return new UnLoginFragment();
                case 3:
                    return new MeFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };


    //点击两次返回退出程序
    long lastTime = 0;

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if ((curTime - lastTime) > 2000) {
            Toast.makeText(this, "再点击退出", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
        lastTime = curTime;
    }
}
