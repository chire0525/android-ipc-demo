package com.tallyho.ipcdemo.binderpool;

import android.os.RemoteException;

import com.tallyho.ipcdemo.ICompute;

public class ComputeImpl extends ICompute.Stub {

    @Override
    public int add(int a, int b) {
        return a + b;
    }

}
