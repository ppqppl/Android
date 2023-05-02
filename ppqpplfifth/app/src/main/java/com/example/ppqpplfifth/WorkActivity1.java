package com.example.ppqpplfifth;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ppqpplfifth.databinding.ActivityWork1Binding;

public class WorkActivity1 extends AppCompatActivity {

    private ActivityWork1Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWork1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

}