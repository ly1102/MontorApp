package com.cuit.monitorapp.main.cuit.monitorapp;

import android.app.Application;

/**
 * Created by syche on 2018/5/18.
 */

public class Auth extends Application {
    private String objectId;
    private String mac;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
