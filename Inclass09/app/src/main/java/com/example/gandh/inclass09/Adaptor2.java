package com.example.gandh.inclass09;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gandh on 3/27/2017.
 */

public class Adaptor2 extends ArrayAdapter {
Context context;
    ArrayList<chat_data> cd ;

    public Adaptor2(Context context, int resource, ArrayList<chat_data> cd) {
        super(context, resource,cd);
        this.context = context;
        this.cd = cd;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chatbox, parent, false);
        }

       TextView tv= (TextView)  convertView.findViewById(R.id.username);
        TextView tv1= (TextView)  convertView.findViewById(R.id.msg);
        TextView tv2= (TextView)  convertView.findViewById(R.id.time);

        tv.setText(cd.get(position).getUser());
        tv1.setText(cd.get(position).getMsg());
        tv2.setText(cd.get(position).getTime());


        return convertView;
    }
}
