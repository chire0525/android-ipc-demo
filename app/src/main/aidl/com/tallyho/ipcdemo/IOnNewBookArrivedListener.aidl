// IOnNewBookArrivedListener.aidl
package com.tallyho.ipcdemo;

import com.tallyho.ipcdemo.Book;

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}