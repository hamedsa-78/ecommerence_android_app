package com.example.eating_food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;

import io.paperdb.Paper;

public class sign_in_act extends AppCompatActivity   {

            int type_person=1;

            Button btn_sign_in;
            SharedPreferences setting;
            TextView admin_user;
            EditText username,password;
           FirebaseDatabase database;
           EditText phone;
          DatabaseReference ttb;
          CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_act);

        setting = getSharedPreferences("Data", Context.MODE_PRIVATE);

        Paper.init(this);
        btn_sign_in = findViewById(R.id.sign_log);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);

        admin_user=findViewById(R.id.change_log_statement);

        final ProgressDialog mdialog=new ProgressDialog(sign_in_act.this);

        mdialog.setMessage("Fetching...");

        checkBox = findViewById(R.id.remindchbox);

        final FirebaseDatabase database=FirebaseDatabase.getInstance();  //first getting instance
        final DatabaseReference ttb=database.getReference("user"); // then getting refrence
        final DatabaseReference admin_ref=database.getReference("admin");

        phone.setText(setting.getString(Prevalent.phonekey,""));

        username.setText(setting.getString(Prevalent.usernamekey,""));

        password.setText(setting.getString(Prevalent.passwordkey,""));

        btn_sign_in.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                if( true)
                {
                    mdialog.show();
                    if (type_person % 2 ==0) // for admin
                    {
                        admin_ref.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(phone.getText().toString()).exists()) {
                                    admins ad = dataSnapshot.child(phone.getText().toString()).getValue(admins.class);
                                    if (ad.getPassword().equals(password.getText().toString()) && ad.getUsername().equals(username.getText().toString().trim())) {
                                        Toast.makeText(sign_in_act.this, "Shil", Toast.LENGTH_SHORT).show();
                                        if (checkBox.isChecked()) {
                                            SharedPreferences.Editor edit = setting.edit();

                                            edit.putString(Prevalent.phonekey, phone.getText().toString());
                                            edit.putString(Prevalent.usernamekey, username.getText().toString());
                                            edit.putString(Prevalent.passwordkey, password.getText().toString());
                                            edit.apply();


                                        }
                                        Intent intent = new Intent(sign_in_act.this, Catagori.class);
                                        startActivity(intent);

                                    }
                                }
                                else
                                {
                                    mdialog.dismiss();
                                    Toast.makeText(sign_in_act.this, "Nop", Toast.LENGTH_SHORT).show();
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });//******************************************************************************************
                    }
                    else // for user
                    {
                        ttb.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(phone.getText().toString()).exists())
                                {
                                    mdialog.dismiss();

                                    users us = dataSnapshot.child(phone.getText().toString()).getValue(users.class);//*****************************************************
                                    if (us.getPassword().equals(password.getText().toString()) && us.getUsername().equals(username.getText().toString().trim())) {


                                        Toast.makeText(sign_in_act.this, "Shil", Toast.LENGTH_SHORT).show();
                                        if (checkBox.isChecked()) {
                                            SharedPreferences.Editor edit = setting.edit();

                                            edit.putString(Prevalent.phonekey, phone.getText().toString());
                                            edit.putString(Prevalent.usernamekey, username.getText().toString());
                                            edit.putString(Prevalent.passwordkey, password.getText().toString());
                                            edit.apply();
                                        }
                                        Prevalent.current_online_user=us;
                                        Prevalent.usernamekey=us.getUsername();
                                        Prevalent.phonekey=phone.getText().toString();
                                        Prevalent.passwordkey=password.getText().toString();



                                        Intent intent = new Intent(sign_in_act.this, home.class);
                                        startActivity(intent);
                                    }

                                } else
                                {
                                    mdialog.dismiss();
                                    Toast.makeText(sign_in_act.this, "Nop", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
                    }
                }

                else
                {
                    Toast.makeText(getBaseContext() , "check netwotk connection please ", Toast.LENGTH_LONG).show();
                }

            }
        });
        admin_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_user.setText("Log as User");
                btn_sign_in.setText("Log admin");
                type_person++;
            }
        });
    }
    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }


}
