package com.example.mycartoon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LaravaActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_PATH = "com.example.mycartoon.EXTRA_IMAGE_PATH";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }
}