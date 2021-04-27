package com.example.eating_food;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Catagori extends AppCompatActivity implements View.OnClickListener {

    ImageView hats,glasses,books,mobiles;
    int reqh,reqg,reqb,reqm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagori);

        hats=findViewById(R.id.hats);
        glasses=findViewById(R.id.glasses);
        books=findViewById(R.id.books);
        mobiles=findViewById(R.id.mobiles);

        hats.setOnClickListener(this);
        glasses.setOnClickListener(this);
        books.setOnClickListener(this);
        mobiles.setOnClickListener(this);

        reqh=0;
        reqg=1;
        reqb=2;
        reqm=3;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==hats.getId()){
            Intent intent=new Intent(Catagori.this,admin_product.class);
            startActivity(intent);
        }
        if(v.getId()==glasses.getId()){
            Intent intent=new Intent(Catagori.this,admin_product.class);
            startActivity(intent);
        }
        if(v.getId()==books.getId()){
            Intent intent=new Intent(Catagori.this,admin_product.class);
            startActivity(intent);

        }
        if(v.getId()==mobiles.getId()){
            Intent intent=new Intent(Catagori.this,admin_product.class);
            startActivity(intent);

        }
    }

}
