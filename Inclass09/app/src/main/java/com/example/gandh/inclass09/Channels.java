package com.example.gandh.inclass09;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gandh on 3/27/2017.
 */

public class Channels extends AppCompatActivity implements adaptor.actionfromadaptor {
    Button add;
    ListView ls;
    OkHttpClient client;
    SharedPreferences.Editor editor;
    ArrayList<channel_data> datalist = new ArrayList<>();
    String token;

    adaptor adaptor;
    void trueadaptor()
    {
        ArrayList<channel_data> datalisttrue = new ArrayList<>();
        for (channel_data c :
                datalist) {
            if(c.isAdded()) {
                datalisttrue.add(c);
                Log.d("asdjak",c.toString());
            }
        }
        adaptor = new adaptor(Channels.this,R.layout.component,datalisttrue,Channels.this);

        ls.setAdapter(adaptor);
    }
    void alladaptor()
    {


        adaptor = new adaptor(Channels.this,R.layout.component,datalist,Channels.this);
        ls.setAdapter(adaptor);
        adaptor.setNotifyOnChange(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channels);
        ls = (ListView) findViewById(R.id.lv1);
        add = (Button) findViewById(R.id.button3);

        client = new OkHttpClient();
        token = getIntent().getExtras().getString("tag");
        alladaptor();

       final Request request2 = new Request.Builder()
                .url("http://52.90.79.130:8080/Groups/api/get/subscriptions")
                .header("Authorization","BEARER "+token)
                .build();


        client.newCall(request2).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("demo",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String s= response.body().string();
                try {
                    Log.d("demo",s);
                    Log.d("demo","BEARER "+token);
                    JSONObject son1 = new JSONObject(s);
                    JSONArray arra1 = son1.getJSONArray("data");
                    datalist = new ArrayList<>();
                    for(int i=0; i<arra1.length();i++)
                    {
                        channel_data channel = new channel_data();
                        JSONObject son2 = arra1.getJSONObject(i);
                        JSONObject son3 = son2.getJSONObject("channel");
                        channel.setId(son3.getString("channel_id"));
                        channel.setName(son3.getString("channel_name"));
                        channel.setAdded(true);

                        datalist.add(channel);

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        trueadaptor();

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              final  Request request1 = new Request.Builder()
                        .url("http://52.90.79.130:8080/Groups/api/get/channels")
                        .header("Authorization","BEARER "+token)
                        .build();
                alladaptor();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                client.newCall(request1).enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {

                    }


                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String s= response.body().string();
                        try {
                            JSONObject son1 = new JSONObject(s);
                            JSONArray arra1 = son1.getJSONArray("data");
                            for(int i=0; i<arra1.length();i++)
                            {
                                channel_data channel = new channel_data();
                                JSONObject son3 = arra1.getJSONObject(i);

                                channel.setId(son3.getString("channel_id"));
                                channel.setName(son3.getString("channel_name"));
                                channel.setAdded(false);
                                datalist.add(channel);

                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    alladaptor();

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

                    }
                });

                    }

                });
            }


    @Override
    public void intentgenerator(int position) {
        Intent ib = new Intent(Channels.this,Chat.class);
        ib.putExtra("key",datalist.get(position).getName());
        ib.putExtra("key1",datalist.get(position).getId());
        ib.putExtra("tag",token);
        startActivity(ib);
    }

    @Override
    public void channeladder(final int position) {
        datalist.get(position).setAdded(true);
        client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("channel_id",datalist.get(position).getId())
                .build();

        Request request = new Request.Builder()
                .url("http://52.90.79.130:8080/Groups/api/subscribe/channel")
                .header("Authorization","BEARER "+token)
                .header("Content-Type","application/x-www-form-urlencoded")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String s = response.body().string();
                try {
                    JSONObject son1 = new JSONObject(s);
                    if(son1.getString("status").equals(0))
                    {
                        Toast.makeText(Channels.this,"subscription failed",Toast.LENGTH_SHORT).show();
                    }
                    else if(son1.getString("status").equals(1))
                    {
                        Toast.makeText(Channels.this,"subscription sucessful",Toast.LENGTH_SHORT).show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                datalist.get(position).setAdded(true);
                                alladaptor();

                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
}
