package com.xiaweizi.easyshop;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    MyAdapter adapter;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.rl)
    RefreshLayout rl;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        adapter = new MyAdapter();
        lv.setAdapter(adapter);
        rl.setOnLoadListener(new RefreshLayout.onLoadListener() {
            @Override
            public void onLoad() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    List<String> list = new ArrayList<String>();
                                    Random random = new Random();
                                    for (int i = 0; i < 5; i++) {
                                        list.add("第" + random.nextInt() + "个数据");
                                    }
                                    adapter.addDateToAdapter(list);
                                    lv.setSelection(adapter.getCount() - 1);
                                    adapter.notifyDataSetInvalidated();
                                    rl.setLoading(false);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ).start();
            }
        });
        rl.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        rl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<String> list = new ArrayList<String>();
                Random random = new Random();
                for (int i = 0; i < 5; i++) {
                    list.add("第" + random.nextInt() + "个数据");
                }
                adapter.addDateToFirst(list);
                adapter.notifyDataSetInvalidated();
                rl.setRefreshing(false);
            }
        });

    }

    public class MyAdapter extends BaseAdapter {

        private List<String> mList;

        public MyAdapter() {
            mList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                mList.add("第" + i + "个数据");
            }
        }

        public void addDateToAdapter(List<String> list) {
            mList.addAll(list);
        }

        public void addDateToFirst(List<String> list) {
            mList.addAll(0, list);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(MainActivity.this, android.R.layout.simple_list_item_1, null);
            TextView tv = (TextView) view.findViewById(android.R.id.text1);
            tv.setText(mList.get(position));
            return view;
        }
    }
}
