package com.example.mycartoon;
import android.content.Intent;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ProgressBar loadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadingProgressBar = findViewById(R.id.loading_progress_bar);

        // Start a thread to simulate a long running task
        new Thread(() -> {
            try {
                // Perform some task here such as fetching data from server
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Navigate to main activity when the task is finished
            Intent intent = new Intent(MainActivity.this, Home_activity.class);
            startActivity(intent);
            finish();
        }).start();
    }
}