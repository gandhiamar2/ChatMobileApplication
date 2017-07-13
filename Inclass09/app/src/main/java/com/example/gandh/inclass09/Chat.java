package com.example.gandh.inclass09;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

public class Chat extends AppCompatActivity {

    Button save;
    ListView ls;
    EditText tv;
    TextView tv1;
    String token, id, name;
    OkHttpClient client;
    ArrayList<chat_data> chat_list = new ArrayList<>();
    ListView lv2;
    Adaptor2 adaptor2;

    void alldata()
    {
        client = new OkHttpClient();
        final Request request2 = new Request.Builder()
                .url("http://52.90.79.130:8080/Groups/api/get/messages?channel_id="+id)
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
                    chat_list = new ArrayList<>();
                    for(int i=0; i<arra1.length();i++)
                    {
                        chat_data cd = new chat_data();
                        JSONObject son2 = arra1.getJSONObject(i);
                        JSONObject son3 = son2.getJSONObject("user");
                        cd.setMsg(son2.getString("messages_text"));
                        cd.setTime(son2.getString("msg_time"));
                        cd.setUser(son3.getString("fname")+son3.getString("lname"));

                        Log.d("demo",cd.getUser());
                        chat_list.add(cd);


                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adaptor2 = new Adaptor2(Chat.this,R.layout.chatbox,chat_list);
                            ls.setAdapter(adaptor2);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        ls = (ListView) findViewById(R.id.lv2);
        save = (Button) findViewById(R.id.send);
        tv1 = (TextView) findViewById(R.id.title);
        tv = (EditText) findViewById(R.id.entertext);
        id = getIntent().getExtras().getString("key1");
        name = getIntent().getExtras().getString("key");
        token = getIntent().getExtras().getString("tag");
        Log.d("tokendasdnasj",token);
        alldata();


        token = getIntent().getExtras().getString("tag");

        adaptor2 = new Adaptor2(Chat.this,R.layout.chatbox,chat_list);
        ls.setAdapter(adaptor2);
        adaptor2.setNotifyOnChange(true);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                //System.out.println();

                RequestBody requestBody = new FormBody.Builder()
                        .add("msg_text",tv.getText().toString())
                        .add("msg_time",dateFormat.format(date))
                        .add("channel_id",id)
                        .build();

                Request request = new Request.Builder()
                        .url("http://52.90.79.130:8080/Groups/api/post/message")
                        .post(requestBody)
                        .header("Authorization","BEARER "+token)
                        .header("Content-Type","application/x-www-form-urlencoded")
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
                            if(son1.getString("status").equals("1"))
                            {
                                alldata();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });





    }
}
