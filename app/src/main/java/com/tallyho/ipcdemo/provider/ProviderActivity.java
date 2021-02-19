package com.tallyho.ipcdemo.provider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.tallyho.ipcdemo.Book;
import com.tallyho.ipcdemo.R;

public class ProviderActivity extends AppCompatActivity {

    private static final String TAG = "ProviderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        Uri bookUri = BookProvider.BOOK_CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put("_id", 6);
        values.put("name", "Test Book");
        getContentResolver().insert(bookUri, values);
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while (bookCursor.moveToNext()) {
            Book book = new Book(bookCursor.getInt(0), bookCursor.getString(1));
            Log.d(TAG, "Query book: " + book.toString());
        }
        bookCursor.close();
    }
}