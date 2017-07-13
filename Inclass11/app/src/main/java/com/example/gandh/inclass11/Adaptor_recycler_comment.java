package com.example.gandh.inclass11;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ocpsoft.pretty.time.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by gandh on 4/5/2017.
 */

public class Adaptor_recycler_comment extends RecyclerView.Adapter {
    int viewtype;
    Map<String,Comment> comments = new HashMap<>();
    Context context;

    View v;
    String email;


    Adaptor_recycler_comment(Context context, Map<String,Comment> five_day_fcast, String email){
        this.comments = five_day_fcast;

        this.context = context;
            this.email = email;

    }



    class View_holder extends RecyclerView.ViewHolder{
        TextView user,time,msg;

        View v;
        int view_type;
        public View_holder(View itemView, int view_type) throws ParseException {
            super(itemView);
            v = itemView;
            this.view_type = view_type;
            user = (TextView) v.findViewById(R.id.user1);
            msg = (TextView) v.findViewById(R.id.message1);
            time = (TextView) v.findViewById(R.id.time1);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

             v = inflater.inflate(R.layout.commentlist, parent, false);

        View_holder holder = null;
        try {
            holder = new View_holder(v,viewType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return holder;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
           final View_holder view_holder = (View_holder) holder;
        PrettyTime pt = new PrettyTime();

            Comment mee = comments.get(comments.keySet().toArray()[position]);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        view_holder.user.setText(mee.getUser());

        view_holder.msg.setText(mee.getMessage_text());


        try {
            view_holder.time.setText(pt.format(sd.parse(mee.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
