package com.tallyho.ipcdemo.aidl;

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
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;

import com.tallyho.ipcdemo.Book;
import com.tallyho.ipcdemo.IBookManager;
import com.tallyho.ipcdemo.IOnNewBookArrivedListener;
import com.tallyho.ipcdemo.R;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {

    private static final String TAG = "BookManagerActivity";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private IBookManager mRemoteBookManager;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.d(TAG, "Receive new book: " + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                mRemoteBookManager = bookManager;
                // register listener on server side
                bookManager.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteBookManager = null;
            Log.e(TAG, "Binder died.");
        }
    };

    private final IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);

        Button getSizeBtn = findViewById(R.id.get_list_size_button);
        getSizeBtn.setOnClickListener(v -> {
            // invoke time-consuming server side method in a separate thread
            new Thread(() -> {
                List<Book> list;
                try {
                    list = mRemoteBookManager.getBookList();
                    Log.i(TAG, "Query book list: " + list.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    @Override
    protected void onDestroy() {
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                Log.i(TAG, "Unregister listener: " + mOnNewBookArrivedListener);
                mRemoteBookManager.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }

}