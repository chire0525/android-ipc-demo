package com.tallyho.ipcdemo.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tallyho.ipcdemo.Book;
import com.tallyho.ipcdemo.IBookManager;
import com.tallyho.ipcdemo.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {

    private static final String TAG = "BMS";

    private final AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);

    private final CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private final RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();

    private final Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() {
            SystemClock.sleep(2000);
            return mBookList;
        }

        @Override
        public void addBook(Book book) {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) {
            mListenerList.register(listener);
            final int N = mListenerList.beginBroadcast();
            Log.d(TAG, "Register, current listener list size: " + N);
            mListenerList.finishBroadcast();
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) {
            mListenerList.unregister(listener);
            final int N = mListenerList.beginBroadcast();
            Log.d(TAG, "Unregister, current listener list size: " + N);
            mListenerList.finishBroadcast();
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "IOS"));
        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener listener = mListenerList.getBroadcastItem(i);
            if (listener != null) {
                listener.onNewBookArrived(book);
            }
        }
        mListenerList.finishBroadcast();
    }

    /** Add a new book every 5 seconds. */
    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            // do background processing here...
            while (!mIsServiceDestroyed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book #" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
