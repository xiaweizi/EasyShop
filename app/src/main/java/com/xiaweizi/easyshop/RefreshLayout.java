package com.xiaweizi.easyshop;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by ljkj on 2017/2/6.
 */

public class RefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {
    //listview实例
    private ListView mListView;
    //上拉接口监听器，到了最顶部得上拉加载操作
    private onLoadListener mOnLoadListener;
    //ListView的加载中footer
    private View mListViewFooter;
    //是否再加载中
    private boolean isLoading = false;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //一个圆形进度条
        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.list_footer, null);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //初始化ListView对象
        if (mListView == null){
            getListView();
        }
    }

    private void getListView() {
        int childs = getChildCount();
        if (childs > 0){
            View childView = getChildAt(0);
            if (childView instanceof ListView){
                mListView = (ListView)childView;
                //设置滚动监听器给ListView
                mListView.setOnScrollListener(this);
            }
        }
    }

    //设置加载状态，添加或者移除加载更多圆形进度条
    public void setLoading(boolean loading){
        isLoading = loading;
        if (isLoading){
            mListView.addFooterView(mListViewFooter);
        }else {
            mListView.removeFooterView(mListViewFooter);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        //判断是否到了最底部，并且不是再加载数据得状态
        if ((mListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1)
                && (isLoading == false)){
            //首先设置加载状态
            setLoading(true);
            //调用加载数据得方法
            mOnLoadListener.onLoad();
        }
    }

    //设置监听器
    public void setOnLoadListener(onLoadListener listener){
        mOnLoadListener = listener;
    }


    public interface onLoadListener {
        void onLoad();
    }

}
