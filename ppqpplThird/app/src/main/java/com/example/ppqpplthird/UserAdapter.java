package com.example.ppqpplthird;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;

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
