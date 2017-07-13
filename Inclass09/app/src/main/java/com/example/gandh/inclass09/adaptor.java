package com.example.gandh.inclass09;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gandh on 3/27/2017.
 */

public class adaptor extends ArrayAdapter<channel_data> {

    Context context;
    ArrayList<channel_data> datalist;
    actionfromadaptor intf;



    interface actionfromadaptor
{
    void intentgenerator(int position);
    void channeladder(int position);
}

    public adaptor(Context context, int resource, ArrayList<channel_data> datalist,actionfromadaptor intf) {
        super(context, resource, datalist);
        this.context = context;
        this.datalist= datalist;
        Log.d("test", ""+ datalist.size());
        this.intf = intf;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("test", ""+position);
        if(convertView==null) {
            Log.d("test", "null");
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.component, parent, false);
        }
        TextView text = (TextView) convertView.findViewById(R.id.text);
        Button btn = (Button) convertView.findViewById(R.id.but);
        text.setText(datalist.get(position).getName());
        btn.setEnabled(true);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intf.intentgenerator(position);
            }
        });
        if(!datalist.get(position).isAdded()) {
            btn.setText("Join");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    intf.channeladder(position);
                }
            });
        }
Log.d("test", "returning");

        return convertView;

    }
}
