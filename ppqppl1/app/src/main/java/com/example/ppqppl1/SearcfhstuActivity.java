package com.example.ppqppl1;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ppqppl1.databinding.ActivitySearcfhstuBinding;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import bean.Student;

public class SearcfhstuActivity extends AppCompatActivity {

    private ActivitySearcfhstuBinding binding;


    private String find_url = AdminActivity.url_sercver+"/findbyid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearcfhstuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

//    private void findById(String Id) {
//        String idStr = JSON.toJSONString(Id);
//        OkHttpClient mOkHttpClient = new OkHttpClient();
//        FormEncodingBuilder builder = new FormEncodingBuilder();
//        builder.add("id", idStr);
//        final Request request = new Request.Builder()
//                .url(find_url)
//                .post(builder.build())
//                .build();
//        Call call = mOkHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.v("Fail", e.getMessage());
//                stu_find.clear();
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                String responseStr = response.body().string();
////                stu_find.clear();
//                stu_find = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Student.class);
//            }
//        });
//    }

}