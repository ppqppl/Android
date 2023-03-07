package com.example.ppqpplsecond;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    List<String> nums = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        for (int i=15 ;i < 50 ;i++){
            nums.add("158438221"+i);
        }


        ListView lv = findViewById(R.id.lv);

        lv.setAdapter(new PhoneAdapter());
        lv.setOnItemClickListener((parent, view, i, l) -> {

            String num = nums.get(i);

            Intent inten = getIntent();
            inten.putExtra("num",num);

            setResult(1,inten);
            finish();

        });

    }

    class PhoneAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return nums.size();
        }

        @Override
        public String getItem(int i) {
            return nums.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null){
                view = View.inflate(ContactActivity.this,R.layout.list_view_item,null);

                viewHolder = new ViewHolder();
                viewHolder.phoneBuml = view.findViewById(R.id.phone_num);
                view.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.phoneBuml.setText(getItem(i));
            return view;
        }
    }

    class ViewHolder{
        TextView phoneBuml;
    }

}