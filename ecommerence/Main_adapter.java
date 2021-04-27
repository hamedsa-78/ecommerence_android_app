package com.example.eating_food;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
class Main_adapter extends RecyclerView.Adapter<Main_adapter.ViewHoldwer>
{
  private ArrayList<Products> products;

    public Main_adapter(ArrayList<Products> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public Main_adapter.ViewHoldwer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //now we have to pass our drawable
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view , parent ,false);
        return new ViewHoldwer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Main_adapter.ViewHoldwer holder, final int position) {
            holder.txt_name.setText(products.get(position).getTxt_name());
            holder.txt_descriptions.setText(products.get(position).getTxt_descriptions());
            holder.txt_price.setText(products.get(position).getTxt_price());
          //  Picasso.get().load(products.get(position).getImage_uri()).into(holder.produxts_img);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(products.get(position).getMother_Activity(),Show_all_informations.class);
                    intent.putExtra("Products_Key",products.get(position).getKey());
                    products.get(position).getMother_Activity().startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount()
    {
        return products.size();
             }

    public class ViewHoldwer extends RecyclerView.ViewHolder
    {
        private TextView txt_name,txt_descriptions,txt_price;
        private ImageView produxts_img;

        public  ViewHoldwer(@NonNull View itemView)
        {
            super(itemView);

            txt_name=itemView.findViewById(R.id.name_home);
            txt_descriptions=itemView.findViewById(R.id.descriptin_home);
            txt_price=itemView.findViewById(R.id.price_home);
            produxts_img=itemView.findViewById(R.id.pr_view_img);
        }
    }
}
