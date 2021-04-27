package com.example.eating_food;
// we can write into data base by using Datarefrence and read by using  data snapshot and we have to get the location of our data base by using FirebaseDatabase.getInstance
// and getting the (root) of our data base by using FirebaseDatabase.getRefrence and put it into DatabaseRefrence .
//every Data that we will read is in the form of Datasnapshot  and if we want to write into our database we have to use DataRefrence s  object
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
      Button sign_in,sign_up;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Paper.init(this);

        sign_in=findViewById(R.id.sign_in);
        sign_up=findViewById(R.id.sign_up);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(MainActivity.this,sign_in_act.class);
              startActivity(intent);
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,sign_up.class);

                startActivity(intent);
            }
        });



      //  String passwordkey=Paper.book().read(Prevalent.passwordkey);
       // String usernamekey=Paper.book().read(Prevalent.usernamekey);
       /* if(!phonekey.equals("")){
            Intent intent=new Intent(this,sign_in_act.class);
            startActivity(intent);

        }*/
    }


}
