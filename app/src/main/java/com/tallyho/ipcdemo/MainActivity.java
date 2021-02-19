package com.tallyho.ipcdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.tallyho.ipcdemo.aidl.BookManagerActivity;
import com.tallyho.ipcdemo.binderpool.BinderPoolActivity;
import com.tallyho.ipcdemo.messenger.MessengerActivity;
import com.tallyho.ipcdemo.provider.ProviderActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button messengerBtn = findViewById(R.id.messenger_button);
        messengerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MessengerActivity.class);
            startActivity(intent);
        });

        Button aidlBtn = findViewById(R.id.aidl_button);
        aidlBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookManagerActivity.class);
            startActivity(intent);
        });

        Button providerBtn = findViewById(R.id.provider_button);
        providerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProviderActivity.class);
            startActivity(intent);
        });

        Button binderPoolBtn = findViewById(R.id.binder_pool_button);
        binderPoolBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, BinderPoolActivity.class);
            startActivity(intent);
        });

    }

}