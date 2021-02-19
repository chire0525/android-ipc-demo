package com.tallyho.ipcdemo.binderpool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.tallyho.ipcdemo.ICompute;
import com.tallyho.ipcdemo.ISecurityCenter;
import com.tallyho.ipcdemo.R;

public class BinderPoolActivity extends AppCompatActivity {

    private static final String TAG = "BinderPoolActivity";

    private ISecurityCenter mSecurityCenter;
    private ICompute mCompute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);

        new Thread(this::testBinderPool).start();
    }

    private void testBinderPool() {
        BinderPool binderPool = BinderPool.getInstance(BinderPoolActivity.this);

        Log.d(TAG, "Visit ISecurityCenter.");
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        mSecurityCenter = SecurityCenterImpl.asInterface(securityBinder);
        String msg = "hello world android";
        Log.d(TAG, "Content: " + msg);
        try {
            String password = mSecurityCenter.encrypt(msg);
            Log.d(TAG, "Encrypt result: " + password);
            Log.d(TAG, "Decrypt result: " + mSecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Visit ICompute");
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        mCompute = ComputeImpl.asInterface(computeBinder);
        try {
            Log.d(TAG, "3+5=" + mCompute.add(3, 5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}