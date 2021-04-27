package com.example.eating_food;

import android.app.Activity;
import android.net.Uri;

public class Products {

    private String txt_name,txt_descriptions,txt_price,key;

    private String Image_uri;
    private Activity mother_Activity;

    public String getKey() {
        return key;
    }

    public Activity getMother_Activity() {
        return mother_Activity;
    }

    public String getTxt_name() {
         return txt_name;
     }

     public String getTxt_descriptions() {
         return txt_descriptions;
     }

     public String getTxt_price() {
         return txt_price;
     }

     public String getImage_uri() {
         return Image_uri;
     }



    public Products(String txt_name, String txt_descriptions, String txt_price, String image_uri,String key , Activity mother_Activity) {
        this.txt_name = txt_name;
        this.txt_descriptions = txt_descriptions;
        this.txt_price = txt_price;
        this.key = key;
        Image_uri = image_uri;
        this.mother_Activity = mother_Activity;
    }
}
