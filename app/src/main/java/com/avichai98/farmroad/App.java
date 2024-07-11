package com.avichai98.farmroad;

import android.app.Application;

import com.avichai98.farmroad.Manager.RecordsManager;
import com.avichai98.farmroad.Utilities.MSPV;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MSPV.init(this);
        RecordsManager.init();
    }
}
