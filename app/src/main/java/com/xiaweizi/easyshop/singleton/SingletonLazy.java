package com.xiaweizi.easyshop.singleton;

/**
 * Created by ljkj on 2017/2/7.
 */

public class SingletonLazy {
    private static SingletonLazy mSingletonLazy = null;

    private SingletonLazy(){

    }

    public static SingletonLazy getInstance(){
        if (mSingletonLazy == null){
            synchronized (SingletonLazy.class){
                if (mSingletonLazy == null){
                    mSingletonLazy = new SingletonLazy();
                }
            }
        }
        return mSingletonLazy;
    }
}
