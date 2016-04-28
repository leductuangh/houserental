package com.example.houserental.function;

import core.base.BaseApplication;

/**
 * Created by Tyrael on 4/26/16.
 */
public class HouseRentalApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initActiveAndroidDB();
    }
}
