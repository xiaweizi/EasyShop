package com.xiaweizi.easyshop.singleton;

/**
 * Created by ljkj on 2017/2/7.
 */

public class SingletonHun {
    private static SingletonHun mSingletonHun = new SingletonHun();

    private SingletonHun(){

    }

    public static SingletonHun getInstance(){
        return mSingletonHun;
    }
}
