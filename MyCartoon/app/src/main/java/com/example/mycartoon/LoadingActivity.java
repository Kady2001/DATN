package com.example.mycartoon;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;



    public class LoadingActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_loading);

            ProgressBar loadingProgressBar = findViewById(R.id.loading_progress_bar);
            loadingProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
    }
