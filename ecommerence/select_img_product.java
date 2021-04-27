package com.example.eating_food;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class select_img_product extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_img_product);

        Intent return_intent=getIntent();
        return_intent.putExtra("result",2);
        setResult(Activity.RESULT_OK,return_intent);
        finish();

    }
}
