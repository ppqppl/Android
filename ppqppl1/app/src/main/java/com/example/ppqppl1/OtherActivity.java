package com.example.ppqppl1;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.ppqppl1.GetGpsActivity.data;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import bean.Stu;

public class OtherActivity extends AppCompatActivity {

    private TextView title;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        lv = findViewById(R.id.stulist);
        title = findViewById(R.id.titletext);

        String num_total = String.valueOf(data.size());
        title.setText(num_total);
        lv.setAdapter(new UserAdapter(data,this));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("测试","item"+i+"点击成功");
            }
        });

        View v = findViewById(R.id.container);
        v.getBackground().setAlpha(175);
        // 半透明状态栏
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    public class UserAdapter extends BaseAdapter {
        private List<Stu> data;
        private Context context;

        public UserAdapter(List<Stu> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null){
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.list_view_item,viewGroup,false);
                viewHolder.nameView = view.findViewById(R.id.nameview);
                viewHolder.proView = view.findViewById(R.id.proview);
                view.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.nameView.setText(data.get(i).getName());
            viewHolder.proView.setText(data.get(i).getPro());
            return view;
        }

        private final class ViewHolder{
            TextView nameView;
            TextView proView;
        }

    }
}