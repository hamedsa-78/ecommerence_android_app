package com.example.eating_food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sign_up extends AppCompatActivity {
        Button submit;
        EditText phone,username,password;

        CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        submit=findViewById(R.id.submit);
        phone=findViewById(R.id.phone_ed);
        username=findViewById(R.id.user_ed);
        password=findViewById(R.id.pass_ed);

        checkBox=findViewById(R.id.checkBox);

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference ttb=database.getReference("user");
        final DatabaseReference admin_ref =database.getReference("admin");
        final ProgressDialog mdialog =new ProgressDialog(sign_up.this);
        mdialog.setMessage("Fetching...");

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mdialog.show();
                if (checkBox.isChecked()) {
                    admin_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(phone.getText().toString()).exists()) {
                                mdialog.dismiss();
                                Toast.makeText(sign_up.this, "already exist", Toast.LENGTH_SHORT).show();
                            } else {
                                admins admins = new admins(username.getText().toString().trim(), password.getText().toString().trim());
                                admin_ref.child(phone.getText().toString().trim()).setValue(admins);
                                mdialog.dismiss();
                                Toast.makeText(sign_up.this, "ok,Khosh galipsooz", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    ttb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(phone.getText().toString()).exists()) {
                                mdialog.dismiss();
                                Toast.makeText(sign_up.this, "already exist", Toast.LENGTH_SHORT).show();
                            } else {
                                users us = new users(username.getText().toString().trim(), password.getText().toString().trim());
                                ttb.child(phone.getText().toString().trim()).setValue(us);
                                mdialog.dismiss();
                                Toast.makeText(sign_up.this, "ok,Khosh galipsooz", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
