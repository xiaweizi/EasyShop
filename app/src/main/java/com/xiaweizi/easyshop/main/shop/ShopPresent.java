package com.xiaweizi.easyshop.main.shop;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.xiaweizi.easyshop.commons.LogUtils;
import com.xiaweizi.easyshop.model.GoodsResult;
import com.xiaweizi.easyshop.network.EasyShopClient;
import com.xiaweizi.easyshop.network.UICallBack;

import java.io.IOException;

import okhttp3.Call;

/**
 * 工程名：  MyApplication
 * 包名：    com.xiaweizi.easyshop.main.me.person
 * 类名：    ShopPresent
 * 创建者：  夏韦子
 * 创建日期： 2017/2/16
 * 创建时间： 10:34
 */

public class ShopPresent extends MvpNullObjectBasePresenter<ShopView> {
    private Call call;

    private int pageInt = 1;

    //下拉刷新
    public void refreshData(String type){
        getView().showRefresh();
        call = EasyShopClient.getInstance().getData(1, type);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showRefreshError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                LogUtils.i("body" + body);
                GoodsResult goodsResult = new Gson().fromJson(body, GoodsResult.class);
                switch (goodsResult.getCode()){
                    case 1:
                        if (goodsResult.getData().size() == 0){
                            getView().addRefreshData(goodsResult.getData());
                            getView().showRefreshEnd();
                        }else {
                            getView().addRefreshData(goodsResult.getData());
                            getView().hideRefresh();
                        }
                        pageInt = 2;
                        break;
                    default:
                        getView().showRefreshError(goodsResult.getMessage());
                }
            }
        });
    }

    //上拉加载更多
    public void loadData(String type){
        if (pageInt == 0) pageInt = 1;
        getView().showLoadMoreLoading();
        call = EasyShopClient.getInstance().getData(pageInt, type);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showLoadMoreError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body, GoodsResult.class);
                switch (goodsResult.getCode()){
                    case 1:
                        if (goodsResult.getData().size() == 0){
                            getView().showLoadMoreEnd();
                        }else {
                            getView().addMoreData(goodsResult.getData());
                            getView().hideLoadMore();
                        }
                        pageInt++;
                        break;
                    default:
                        getView().showLoadMoreError(goodsResult.getMessage());
                }
            }
        });
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

}
