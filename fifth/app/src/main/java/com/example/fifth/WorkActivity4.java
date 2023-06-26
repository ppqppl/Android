package com.example.fifth;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fifth.databinding.ActivityWork14Binding;

public class WorkActivity4 extends AppCompatActivity {

    private ActivityWork14Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWork14Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

}