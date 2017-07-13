package com.example.gandh.inclass11;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity implements Adaptor_recycler.fromadaptor{
    TextView tv;
    ArrayList<Message> messages;
    RecyclerView rcl1;
    EditText input;
    String unique;
    String password, email;
    DatabaseReference mDatabase,child;
    StorageReference str;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    SimpleDateFormat sdf;
    ImageView ib1,ib2,ib3;
    Adaptor_recycler adaptor_recycler;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        child = mDatabase.child("messagelist");
        str = FirebaseStorage.getInstance().getReference();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        input = (EditText) findViewById(R.id.editText3);
        ib1= (ImageView) findViewById(R.id.imageButton);
        ib2= (ImageView) findViewById(R.id.imageButton2);
        ib3= (ImageView) findViewById(R.id.imageButton3);
        rcl1 = (RecyclerView) findViewById(R.id.rc1);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        rcl1.setLayoutManager(lm);
        storedpref_getter();
        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                String unique= UUID.randomUUID().toString();
                message.unique=unique;
                message.setMessage_text(input.getText().toString());
                message.setUser(email);
                message.image=false;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                message.setTime(sdf.format(new Date()));
                Map<String,Object> childupdate = new HashMap<>();
                childupdate.put("user",message.getUser());
                childupdate.put("message_text",message.getMessage_text());
                childupdate.put("image",message.image);
                childupdate.put("unique",message.unique);
                childupdate.put("time",message.getTime());
                Map<String,Object> childupdate1 = new HashMap<>();
                childupdate1.put("/"+message.unique,childupdate);
                child.updateChildren(childupdate1);

            }
        });
        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final Message message = new Message();
                String unique= UUID.randomUUID().toString();
                message.unique=unique;
                ib3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        message.setMessage_text(input.getText().toString());
                        message.setUser(email);
                        message.image=true;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        message.setTime(sdf.format(new Date()));
                        Map<String,Object> childupdate = new HashMap<>();
                        childupdate.put("user",message.getUser());
                        childupdate.put("message_text",message.getMessage_text());
                        childupdate.put("time",message.getTime());
                        childupdate.put("image",message.image);
                        childupdate.put("unique",message.unique);
                        Map<String,Object> childupdate1 = new HashMap<>();
                        childupdate1.put("/"+message.unique,childupdate);
                        child.updateChildren(childupdate1);

                    }
                });
                image_adder(message);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages= new ArrayList<Message>();
                for (DataSnapshot snapshot  :
                        dataSnapshot.getChildren()  ) {
                        Message message;
                    message = snapshot.getValue(Message.class);
                    messages.add(message);
                }
                adaptor_recycler = new Adaptor_recycler(ChatActivity.this,messages,ChatActivity.this,email);
                rcl1.setAdapter(adaptor_recycler);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                editor.clear().commit();
                finish();
            }
        });
    }

    void storedpref_getter()
    {

         email = sharedPreferences.getString("email","a");
         password = sharedPreferences.getString("password","a");


    }


    @Override
    public void cooment_adder(final int position) {

        RelativeLayout l1 = new RelativeLayout(this);
        final EditText editText_city = new EditText(this);
        editText_city.setId(View.generateViewId());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        l1.setLayoutParams(layoutParams);
        l1.addView(editText_city,lp);


        new AlertDialog.Builder(this)
                .setTitle( "comment" )
                .setView(l1)
                .setPositiveButton("set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Comment comment = new Comment();
                        comment.setUser(email);
                        comment.setTime(sdf.format(new Date()));
                        comment.setMessage_text(editText_city.getText().toString());
                        comment.unique_c= UUID.randomUUID().toString();
                        messages.get(position).comments.put(comment.unique_c,comment);

                        child.child(messages.get(position).unique).child("comments").setValue(messages.get(position).comments);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

    @Override
    public void delete(int position) {
            child.child(messages.get(position).unique).removeValue();
    }

    @Override
    public void imageadder(int position) {

    }


    public void image_adder(Message ms) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        unique = ms.unique;
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            try {
                final Uri imageUri = data.getData();

                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                storeImageToFirebase(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void storeImageToFirebase(Bitmap imagefile)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
       // Bitmap bitmap = BitmapFactory.decodeFile(imagefile, options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagefile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        UploadTask uploadTask = str.child(unique).putBytes(bytes);
unique=null;
        System.out.println("Stored image with length: " + bytes.length);
    }
}
