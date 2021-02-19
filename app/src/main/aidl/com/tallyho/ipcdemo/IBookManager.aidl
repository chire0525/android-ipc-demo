// IBookManager.aidl
package com.tallyho.ipcdemo;

import com.tallyho.ipcdemo.Book;
import com.tallyho.ipcdemo.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}