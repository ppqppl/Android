package com.example.ppqpplfifth;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ppqpplfifth.databinding.ActivityWork2Binding;

public class WorkActivity2 extends AppCompatActivity {

    private ActivityWork2Binding binding;
    private ImageButton img;
    private Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWork2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        img = findViewById(R.id.mainpic);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    img.setImageResource(R.drawable.play);
                    flag = false;
                }
                else{
                    img.setImageResource(R.drawable.stop);
                    flag = true;
                }
            }
        });

    }

}