package com.brandhype.medixfora.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


import com.brandhype.medixfora.models.CartItem;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Tanmoy Banerjee on 25-07-2017.
 */

public class GlobalClass extends Application {

    private String name;

    public CartItem getCartitem() {
        return cartitem;
    }

    public void setCartitem(CartItem cartitem) {
        this.cartitem = cartitem;
    }

    private CartItem cartitem;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //--for function >64K
        MultiDex.install(this);
       }

    @Override
    public void onCreate() {
        super.onCreate();
        //for crash analytics
        Fabric.with(this, new Crashlytics());
    }


    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }


}
