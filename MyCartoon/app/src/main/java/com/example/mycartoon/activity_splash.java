package com.example.mycartoon;
import android.content.Intent;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
public class activity_splash  extends AppCompatActivity {

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
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Navigate to main activity when the task is finished
            Intent intent = new Intent(activity_splash.this, MainActivity.class);
            startActivity(intent);
            finish();
        }).start();
    }
}