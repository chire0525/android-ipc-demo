// IBinderPool.aidl
package com.tallyho.ipcdemo;

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}