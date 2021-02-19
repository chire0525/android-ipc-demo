package com.tallyho.ipcdemo.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tallyho.ipcdemo.MyConsts;

public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    private static class MessengerHandler extends Handler {

        public MessengerHandler(Looper myLooper) {
            super(myLooper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MyConsts.MSG_FROM_CLIENT:
                    Log.i(TAG, "Receive msg from client: " + msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, MyConsts.MSG_FROM_SERVICE);
                    Bundle data = new Bundle();
                    data.putString("reply", "Message received. Will reply later.");
                    replyMessage.setData(data);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler(Looper.myLooper()));

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
