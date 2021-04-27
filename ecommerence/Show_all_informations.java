package com.example.eating_food;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class Show_all_informations extends AppCompatActivity {

    //Variables
    private String bought_number = "1",product_name,product_price,time;

    // Edit Text
    private TextView pr_name;
    private TextView pr_description;
    private TextView pr_price;
    private ImageView pri_image;
    private Context this_activity ;

    //Database
    private DatabaseReference databaseReference;

    //elegment button
    private ElegantNumberButton elegantNumberButton;

    //Buttons
    private Button finalize_buying;

    private int p = 0;

    private DatabaseReference databaseReference_card_list;

    private  HashMap <String,Object> hashMap ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_informations);
        Toast.makeText(this , "Show all information  " , Toast.LENGTH_LONG).show();

        this_activity = this ;


        // find views
        pr_name = findViewById(R.id.pr_name);
        pr_description=findViewById(R.id.pr_description);
        pr_price=findViewById(R.id.pr_price);
        pri_image=findViewById(R.id.pr_image);
        finalize_buying=findViewById(R.id.Finalize_buying);
        elegantNumberButton=findViewById(R.id.elegmant_number_button);

        //
       final String key = getIntent().getStringExtra("Products_Key");
        databaseReference= FirebaseDatabase.getInstance().getReference("Products");
        databaseReference_card_list =FirebaseDatabase.getInstance().getReference("card_list");

        //get the  datas from last Intent
         databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //Fetch and get the value
               product_name = dataSnapshot.child(key).child("product_name").getValue().toString();
               product_price = dataSnapshot.child(key).child("price").getValue().toString();

               pr_name.setText(product_name);
                pr_price.setText(product_price);
                pr_description.setText(dataSnapshot.child(key).child("description").getValue().toString());

                Picasso.get().load(dataSnapshot.child(key).child("image_uri").getValue().toString()).fit().into(pri_image);
                p = Integer.valueOf(dataSnapshot.child(key).child("price").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //get the number of buying
        elegantNumberButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(Show_all_informations.this , "Test elagment " , Toast.LENGTH_LONG).show();
                bought_number = elegantNumberButton.getNumber();
                int n = Integer.parseInt(bought_number);
                int total = n * p;
                product_price  = String.valueOf(total);
                pr_price.setText(product_price);

            }
        });

        finalize_buying.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar calendar = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat_time=new SimpleDateFormat("HH:mm:ss");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat_data=new SimpleDateFormat("MMM dd,yyyy");

                time = simpleDateFormat_data.format(calendar.getTime())+ simpleDateFormat_time.format(calendar.getTime());

                hashMap = new HashMap<>();
                final String[] key_time = {""};
                final String [] values = new String[3];
                databaseReference_card_list.child("user_view").orderByChild("product_name").equalTo(product_name).addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)   //  اینجا datasnapshot  کل محتوایی که شامل این product_name  ما است را
                            // بر می گرداند یعنی key  آن که user_view  خواهد بود و value  آن کل عبارتی که شامل product_name  است
                            // حالا خود این product_name  در اینجا یک key  دارد که تاریخ و ساعت است و اده های مورد نظر .
                    {
                        if(dataSnapshot.exists())
                        {
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                            {
                             key_time[0] = postSnapshot.getKey().toString();

                             int i =0  ;

                             for (DataSnapshot value_snapshot : postSnapshot.getChildren())
                             {
                                 values[i] = value_snapshot.getValue().toString();
                                 i ++ ;
                             }

                            }
                            databaseReference_card_list.child("user_view").child(key_time[0]).child("product_number").setValue(
                                    String.valueOf(Integer.parseInt(bought_number) + Integer.parseInt(values[1]))
                            );

                            databaseReference_card_list.child("user_view").child(key_time[0]).child("product_price").setValue(
                                    String.valueOf(Integer.parseInt(product_price) + Integer.parseInt(values[2]))
                            );
                        }
                        else
                        {
                            hashMap.put("product_name",product_name);
                            hashMap.put("product_price",product_price);
                            hashMap.put("product_number",bought_number);
                            databaseReference_card_list.child("admin_view").child(time).setValue(hashMap);
                            databaseReference_card_list.child("user_view").child(time).setValue(hashMap);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });

            }
        });

        databaseReference_card_list.child("user_view").addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Toast.makeText(getBaseContext() , "your request sent succesfully" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Toast.makeText(getBaseContext() , "Your old request has been updated" , Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
