package com.example.fifth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fifth.databinding.ActivityWork1Binding;

public class Activity_work1 extends AppCompatActivity {

    private ActivityWork1Binding binding;

    private Button work1,work2,work3,work4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWork1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        work1 = findViewById(R.id.btn_work1);
        work2 = findViewById(R.id.btn_work2);
        work3 = findViewById(R.id.btn_work3);
        work4 = findViewById(R.id.btn_work4);

        work1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Activity_work1.this,WorkActivity1.class);
                startActivity(intent);
            }
        });
        work2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Activity_work1.this,WorkActivity2.class);
                startActivity(intent);
            }
        });
        work3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Activity_work1.this,WorkActivity3.class);
                startActivity(intent);
            }
        });
        work4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Activity_work1.this,WorkActivity4.class);
                startActivity(intent);
            }
        });
    }

}