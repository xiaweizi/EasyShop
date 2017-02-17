package com.xiaweizi.easyshop.main.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.xiaweizi.easyshop.R;
import com.xiaweizi.easyshop.commons.ActivityUtils;
import com.xiaweizi.easyshop.main.details.GoodsDetailActivity;
import com.xiaweizi.easyshop.model.GoodsInfo;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by 下位子 on 2017/2/7.
 */

public class ShopFragment extends MvpFragment<ShopView, ShopPresent> implements ShopView{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    PtrClassicFrameLayout refreshLayout;
    @BindView(R.id.tv_load_error)
    TextView tvLoadError;
    @BindString(R.string.load_more_end)
    String load_more_end;
    @BindString(R.string.refresh_more_end)
    String refresh_more_end;

    private ActivityUtils activityUtils;
    private ShopAdapter adapter;
    /**
     * 获取商品时,商品的类型,获取全部商品时为空
     */
    private String pageType = "";

    @Override
    public ShopPresent createPresenter() {
        return new ShopPresent();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        adapter = new ShopAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        recyclerView.setAdapter(adapter);
    }


    private void initView() {
        //recyclerVie初始化
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //下拉刷新初始化
        //使用本对象作为key，来记录上一次刷新时间，如果两次下拉间隔太近，不会触发刷新方法
        refreshLayout.setLastUpdateTimeRelateObject(this);
        refreshLayout.setBackgroundResource(R.color.recycler_bg);
        //关闭header所耗时长
        refreshLayout.setDurationToCloseHeader(1500);
        refreshLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                presenter.loadData(pageType);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.refreshData(pageType);
            }
        });

        //商城页的点击事件
        adapter.setListener(new ShopAdapter.OnItemClickedListener() {
            @Override
            public void onPhotoClicked(GoodsInfo goodsInfo) {
                Intent intent = GoodsDetailActivity.getStartIntent(getContext(), goodsInfo.getUuid(), 0);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //当前页面没有数据，刷新
        if (adapter.getItemCount() == 0){
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.autoLoadMore();
                }
            }, 200);
        }
    }

    @Override
    public void showRefresh() {
        tvLoadError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showRefreshError(String msg) {
        refreshLayout.refreshComplete();
        if (adapter.getItemCount() > 0){
            activityUtils.showToast(msg);
            return;
        }
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshEnd() {
        activityUtils.showToast(refresh_more_end);
        refreshLayout.refreshComplete();
    }

    @Override
    public void hideRefresh() {
        refreshLayout.refreshComplete();
    }

    @Override
    public void showLoadMoreLoading() {
        tvLoadError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        refreshLayout.refreshComplete();
        if (adapter.getItemCount() > 0){
            activityUtils.showToast(msg);
            return;
        }
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreEnd() {
        activityUtils.showToast(load_more_end);
        refreshLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        refreshLayout.refreshComplete();
    }

    @Override
    public void addMoreData(List<GoodsInfo> data) {
        adapter.addData(data);
    }

    @Override
    public void addRefreshData(List<GoodsInfo> data) {
        adapter.clear();
        if (data != null){
            adapter.addData(data);
        }
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
