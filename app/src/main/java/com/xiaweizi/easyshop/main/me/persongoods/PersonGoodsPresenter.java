package com.xiaweizi.easyshop.main.me.persongoods;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.xiaweizi.easyshop.main.shop.ShopView;
import com.xiaweizi.easyshop.model.CachePreferences;
import com.xiaweizi.easyshop.model.GoodsResult;
import com.xiaweizi.easyshop.network.EasyShopClient;
import com.xiaweizi.easyshop.network.UICallBack;

import java.io.IOException;

import okhttp3.Call;

/**
 * 工程名：  MyApplication
 * 包名：    com.xiaweizi.easyshop.main.shop
 * 类名：    PersonGoodsPresenter
 * 创建者：  夏韦子
 * 创建日期： 2017/2/17
 * 创建时间： 12:05
 */

public class PersonGoodsPresenter extends MvpNullObjectBasePresenter<ShopView> {
    /**
     * 获取商品时,分页下标
     */
    private int pageInt = 1;

    private Call call;

    public void refreshData(String type) {
        getView().showRefresh();
        call = EasyShopClient.getInstance().getPersonData(1, type, CachePreferences.getUser().getName());
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showRefreshError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body, GoodsResult.class);
                switch (goodsResult.getCode()) {
                    case 1:
                        if (goodsResult.getData().size() == 0) {
                            getView().showRefreshEnd();
                        } else {
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

    public void loadData(String type) {
        getView().showLoadMoreLoading();
        if (pageInt == 0) pageInt = 1;
        call = EasyShopClient.getInstance().getPersonData(pageInt, type, CachePreferences.getUser().getName());
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showLoadMoreError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body, GoodsResult.class);
                switch (goodsResult.getCode()) {
                    case 1:
                        if (goodsResult.getData().size() == 0) {
                            getView().showLoadMoreEnd();
                        } else {
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
