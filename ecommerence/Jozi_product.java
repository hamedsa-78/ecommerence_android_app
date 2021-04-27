package com.example.eating_food;

import android.app.Activity;
import android.widget.SearchView;

public class Jozi_product
 {
     private String product_name,Total_cost , key;

     private Activity activity ;


     public String getKey()
     {
         return key;
     }

     public Jozi_product(String product_name, String total_cost, String key, Activity activity)
     {
         this.product_name = product_name;
         Total_cost = total_cost;
         this.key = key ;
         this.activity = activity;
     }

     public Activity getActivity()
     {
         return activity;
     }

     public String getProduct_name()
     {
         return product_name;
     }

     public void setProduct_name(String product_name)
     {
         this.product_name = product_name;
     }

     public Jozi_product(String product_name, String total_cost)
     {
         this.product_name = product_name;
         Total_cost = total_cost;
     }

     public String getTotal_cost()
     {
         return Total_cost;
     }

     public Jozi_product()
     {

     }

     public void setTotal_cost(String total_cost)
     {
         Total_cost = total_cost;
     }
 }
