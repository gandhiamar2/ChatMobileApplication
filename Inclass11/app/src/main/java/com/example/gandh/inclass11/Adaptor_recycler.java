package com.example.gandh.inclass11;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by gandh on 4/5/2017.
 */

public class Adaptor_recycler extends RecyclerView.Adapter {
    int viewtype;
    ArrayList<Message   > messages = new ArrayList<>();
    Context context;
    fromadaptor intf;
    View v;
    String email;


    Adaptor_recycler(Context context, ArrayList<Message> five_day_fcast, fromadaptor intf, String email){
        this.messages = five_day_fcast;
            this.intf = intf;
        this.context = context;
            this.email = email;

    }

    interface fromadaptor{

        void cooment_adder(int position);
        void delete(int position);
        void imageadder(int position);

    }

    class View_holder extends RecyclerView.ViewHolder{
        TextView user,time,msg;
        ImageView im1,im2,im3;
        View v;
        RecyclerView rv2;
        int view_type;
        public View_holder(View itemView, int view_type) throws ParseException {
            super(itemView);
            v = itemView;
            this.view_type = view_type;
            user = (TextView) v.findViewById(R.id.user);
            msg = (TextView) v.findViewById(R.id.message);
            time = (TextView) v.findViewById(R.id.time);
             im1 = (ImageView) v.findViewById(R.id.imageView);
            im2 = (ImageView) v.findViewById(R.id.imageView2);
           im3 = (ImageView) v.findViewById(R.id.imageView3);
            im3.setVisibility(View.INVISIBLE);
            im2.setVisibility(View.INVISIBLE);
            rv2 = (RecyclerView) v.findViewById(R.id.rc2);
            LinearLayoutManager lm = new LinearLayoutManager(context);
            rv2.setLayoutManager(lm);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

             v = inflater.inflate(R.layout.chatlist, parent, false);

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
            Message mee = messages.get(position);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        view_holder.user.setText(mee.getUser());
        if(mee.image)
        {
            view_holder.im3.setVisibility(View.VISIBLE);
            view_holder.msg.setVisibility(View.INVISIBLE);
            StorageReference img = FirebaseStorage.getInstance().getReference();
            StorageReference pathReference = img.child(mee.unique);
            Log.d("demo","sdfnaskj"+position+"dsdas"+pathReference);
            Glide.with(context )
                    .using(new FirebaseImageLoader())
                    .load(pathReference)
                    .into(view_holder.im3);
        }
        try {
            view_holder.time.setText(pt.format(sd.parse(mee.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        view_holder.msg.setText(mee.getMessage_text());
        view_holder.im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intf.cooment_adder(position);
            }
        });
        if(mee.user.equals(email))
        {
            view_holder.im2.setVisibility(View.VISIBLE);
        }
        view_holder.im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intf.delete(position);
            }
        });
        if(mee.comments.size()>0)
        {
            Adaptor_recycler_comment commentadap = new Adaptor_recycler_comment(context,mee.comments,email);
            view_holder.rv2.setAdapter(commentadap);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
