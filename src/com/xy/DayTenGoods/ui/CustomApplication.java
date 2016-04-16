package com.xy.DayTenGoods.ui;

import android.app.Application;
import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.xy.DayTenGoods.common_background.Account;

/**
 * Created by xiaoyu on 2016/3/19.
 */
public class CustomApplication extends Application {
    private static CustomApplication instance;
    private Account account;

    public static final String BAICHUAN_APP_KEY = "23337091";
    public static final String BAICHUAN_APP_SECRET = "a02d9680e085983d7f5f7dbad27318d3";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        AlibabaSDK.asyncInit(this, new InitResultCallback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int code, String message) {
            }
        });
    }

    public static CustomApplication getInstance() {
        return instance;
    }

    public Account getAccount() {
        if (account == null) {
            account = Account.loadAccount();
        }
        return account;
    }
}
