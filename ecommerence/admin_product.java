package com.example.eating_food;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class admin_product extends AppCompatActivity
{

    ImageView select_img;
    Button add_product_btn;
    String save_current_date,save_current_time;
    EditText productname,descriptions,price;

    int req_code = 0;
    String download_img_url = "";
    String pr_name_st,des_st,price_st;
    String product_random_key;

    DatabaseReference write;
    StorageReference product_img_ref;
    Uri Image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);
        Toast.makeText(this , "admin_product " , Toast.LENGTH_LONG).show();

        product_img_ref = FirebaseStorage.getInstance().getReference().child("product_image");

        select_img = findViewById(R.id.seleect_img);
        //**********************************************************************************************

        write = FirebaseDatabase.getInstance().getReference().child("Products");
        productname = findViewById(R.id.product_name);
        descriptions = findViewById(R.id.descriptions);
        price = findViewById(R.id.price);
        add_product_btn = findViewById(R.id.add_product_btn);

        select_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               Intent intent = new Intent();
               intent.setAction(Intent.ACTION_GET_CONTENT);
               intent.setType("image/*");
               startActivityForResult(intent,req_code);
            }
        });
        add_product_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pr_name_st=productname.getText().toString();
                des_st=descriptions.getText().toString();
                price_st=price.getText().toString();
                if(Image_uri == null)
                {
                    Toast.makeText(admin_product.this,"first set the image ", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(pr_name_st))
                {
                    Toast.makeText(admin_product.this,"enter the products name, please ", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(price_st))
                {
                    Toast.makeText(admin_product.this,"enter the price of this product, please ", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    //**store the values.
                    //first we need to ge time to store it on a storage database as a key .
                    Calendar calendar=Calendar.getInstance();

                    SimpleDateFormat current_date=new SimpleDateFormat("MMM dd,yyyy");

                    save_current_date=current_date.format(calendar.getTime());

                    SimpleDateFormat current_time=new SimpleDateFormat("HH:mm:ss");

                    save_current_time=current_time.format(calendar.getTime());

                    product_random_key=save_current_date+save_current_time;
                    //////*************************///*******************************************************************************************
                    final StorageReference filepath = product_img_ref.child(Image_uri.getLastPathSegment()+product_random_key+".jpg");
                    final UploadTask uploadTask = filepath.putFile(Image_uri); //uplode

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    download_img_url = uri.toString();
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            Toast.makeText(admin_product.this,e.toString(),Toast.LENGTH_SHORT).show();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                        {
                                    if(task.isSuccessful())
                                    {
                                        save_informatin_into_firebase();
                                    }
                        }

                    });

                }
            }
        });
    }

    private void save_informatin_into_firebase()
    {

        HashMap<String,Object> will_send=new HashMap<>();

        will_send.put("product_id",product_random_key);
        will_send.put("date",save_current_date);
        will_send.put("time",save_current_time);
        will_send.put("description",des_st);
        will_send.put("product_name",pr_name_st);
        will_send.put("price",price_st);
        will_send.put("image_uri",download_img_url);
        if(download_img_url.equals("")){
            Toast.makeText(this,"Unsuccessfull uploading please try again.",Toast.LENGTH_LONG).show();
        }
        else {
            write.child(product_random_key).setValue(will_send);
            Toast.makeText(this, "writed succesfult", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == req_code)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                ///////*****
                Image_uri = data.getData();
                select_img.setImageURI(Image_uri);
            }
        }
    } /// end of on startactivity for result
}






