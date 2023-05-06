package com.example.mycartoon;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mycartoon.databinding.ActivityHomeBinding;

public class Home_activity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomeBinding binding;

    private ImageView muc1;
    private ImageView muc2;
    private ImageView muc3;

    private ImageView muc4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Lấy đối tượng ImageView của từng mục
        muc1 = findViewById(R.id.muc1);
        muc2 = findViewById(R.id.muc2);
        muc3 = findViewById(R.id.muc3);
        muc4 = findViewById(R.id.muc4);

        // Đăng ký sự kiện nhấn vào mỗi mục
        muc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_activity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });

        muc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển tới activity Muc2Activity
            }
        });

        muc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển tới activity Muc3Activity
            }
        });

        muc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển tới activity Muc4Activity
            }
        });
    }
}