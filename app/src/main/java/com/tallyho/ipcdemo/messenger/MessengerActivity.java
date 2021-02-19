package com.tallyho.ipcdemo.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.tallyho.ipcdemo.MyConsts;
import com.tallyho.ipcdemo.R;

public class MessengerActivity extends AppCompatActivity {

    private static final String TAG = "MessengerActivity";

    private Messenger mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain(null, MyConsts.MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "Hello, this is client.");
            msg.setData(data);
            msg.replyTo = mMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    private static class MessengerHandler extends Handler {

        public MessengerHandler(Looper myLooper) {
            super(myLooper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MyConsts.MSG_FROM_SERVICE:
                    Log.i(TAG, "Receive msg from service: " + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler(Looper.getMainLooper()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}