package com.hanway.game.evening.app.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class DataProviderResultReceiver extends ResultReceiver {
    private Receiver receiver;

    public DataProviderResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }
}