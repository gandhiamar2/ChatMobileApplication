package com.example.gandh.inclass09;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText email, email2, pass, pass2, fname , lname;
    Button login, signup;
    OkHttpClient client ;
    SharedPreferences sp ;
    SharedPreferences.Editor editor;
    void loginchecker()
    {
      String token=  sp.getString("token","none");
        if(token.equals("none"))
        {

        }
        else
        {
            Intent ia = new Intent(this,Channels.class);
            ia.putExtra("tag",token);

            startActivity(ia);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.email1);
        email2 = (EditText) findViewById(R.id.email2);
        pass = (EditText) findViewById(R.id.password1);
        pass2 = (EditText) findViewById(R.id.password2);
        fname = (EditText) findViewById(R.id.namef);
        lname = (EditText) findViewById(R.id.namel);
        login = (Button) findViewById(R.id.button);
        signup = (Button) findViewById(R.id.button2);
         sp = getSharedPreferences("logindata",MODE_PRIVATE);
         editor = sp.edit();
       // editor.clear().apply();
        loginchecker();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                client = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("email",email.getText().toString())
                        .add("password",pass.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url("http://52.90.79.130:8080/Groups/api/login")
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
                            Log.d("demo",son1.getString("data"));
                            editor.putString("token",son1.getString("data"));
                            editor.apply();
                            loginchecker();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });


            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                client = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("email",email2.getText().toString())
                        .add("password",pass2.getText().toString())
                        .add("fname",fname.getText().toString())
                        .add("lname",lname.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url("http://52.90.79.130:8080/Groups/api/signUp")
                        .post(requestBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String s= response.body().string();
                        try {
                            JSONObject son1 = new JSONObject(s);
                            editor.putString("token",son1.getString("data"));
                            editor.apply();
                            loginchecker();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }


}
