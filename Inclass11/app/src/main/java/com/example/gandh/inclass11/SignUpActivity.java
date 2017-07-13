package com.example.gandh.inclass11;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class SignUpActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    StorageReference mRef,mRef_c,mRef_d,mRef_e;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button cancel,signUp;
    EditText firstName,lastName,email,choosePassword,repeatPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        firstName =(EditText)findViewById(R.id.editTextName);
        lastName=(EditText)findViewById(R.id.editTextLastName);
        signUp = (Button)findViewById(R.id.button3);
        cancel = (Button)findViewById(R.id.button2);
        email =(EditText)findViewById(R.id.editTextEmail);
        choosePassword=(EditText)findViewById(R.id.editTextChoosePassword);
        repeatPassword=(EditText)findViewById(R.id.editTextRepeatPassword);
        mAuth = FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choosePassword.getText().toString().equals(repeatPassword.getText().toString())) {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), choosePassword.getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("signup demo", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {

                                    }
                                    else{
                                       storedpref_setter(email.getText().toString(), choosePassword.getText().toString());
                                        finish();
                                    }

                                    // ...
                                }
                            }).addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("demo",e.toString())
;                        }
                    });
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void storedpref_setter(String email, String password)
    {

        editor.clear();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.commit();
        Toast.makeText(this," details stored",Toast.LENGTH_SHORT).show();
    }
}
