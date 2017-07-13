package com.example.gandh.inclass11;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

DatabaseReference mDatabase;
    StorageReference mRef,mRef_c,mRef_d,mRef_e;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button login,signUp;
    EditText email,password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        login =(Button)findViewById(R.id.buttonLogin);
        signUp = (Button)findViewById(R.id.buttonSignUp);
        email =(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);
        mAuth = FirebaseAuth.getInstance();


        if(storedpref_getter().equals("a"))
        {
            login.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onClick(View v) {
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.d("demo", "signInWithEmail:failed", task.getException());
                                    }
                                    else {
                                        Log.d("demo", "signInWithEmail");
                                        storedpref_setter(email.getText().toString(), password.getText().toString());
                                        Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                                        startActivity(intent);
                                        MainActivity.this.recreate();
                                    }
                                }
                            });

                }
            });

            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                    startActivity(intent);
                    MainActivity.this.recreate();
                }
            });
        }
        else
        {
           Intent intent = new Intent(MainActivity.this,ChatActivity.class);
            startActivity(intent);

        }
    }

    void storedpref_setter(String email, String password)
    {

        editor.clear();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.commit();
        Toast.makeText(this," details stored",Toast.LENGTH_SHORT).show();
    }

    String storedpref_getter()
    {

        String city_key = sharedPreferences.getString("email","a");
        return city_key;

    }
}
