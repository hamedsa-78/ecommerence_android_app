package com.example.eating_food;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class main_adapter_Cart extends RecyclerView.Adapter <main_adapter_Cart.view_holder_this>

{

    public   int tt_cost ;
    private ArrayList <Jozi_product> jozi_products ;
    private ArrayList <Jozi_product> item_copy ;
    private DatabaseReference database ;
    View checkBoxView ;
    CheckBox checkBox ;



    public main_adapter_Cart(ArrayList<Jozi_product> jozi_products , int tt_cot)
    {

        this.jozi_products = jozi_products;

        item_copy = new ArrayList<>();
        item_copy.addAll(jozi_products);

        this.tt_cost = tt_cot ;
        database = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public main_adapter_Cart.view_holder_this onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_card_view,parent,false);
        return new view_holder_this(view);

    }
    public void onBindViewHolder(@NonNull final main_adapter_Cart.view_holder_this holder, final int position)
    {
        holder.pro_cart_name.setText(jozi_products.get(position).getProduct_name());
        holder.Total_cost_cart.setText(jozi_products.get(position).getTotal_cost());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CharSequence [] options = new CharSequence[]{"Edit", "remove"};
                final boolean [] checkedItems = {false , false};
                final AlertDialog.Builder builder = new AlertDialog.Builder(jozi_products.get(position).getActivity());
                builder.setTitle("Options");
                builder.setView(checkBoxView);


                builder.setMultiChoiceItems(options , checkedItems ,  new DialogInterface.OnMultiChoiceClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which , boolean ischecked)
                    {

                        if(which == 1)
                        {
                            //holder.itemView.setVisibility(View.INVISIBLE);   /// این سبک البته بیشتر برای وقتی به کار می رود که کلید را نداشته باشیم
                            Toast.makeText(holder.itemView.getContext() , String.valueOf(position) , Toast.LENGTH_SHORT).show();

                            Query applesQuery = database.child("card_list").child("user_view").orderByChild("product_name").equalTo(jozi_products.get(position).getProduct_name());
                            Query admin_view_rmove_query = database.child("cart_list").child("admin_view").orderByChild("product_name").equalTo(jozi_products.get(position).getProduct_name());
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren())
                                    {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError)
                                {

                                }
                            });
                            admin_view_rmove_query.addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren())
                                    {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError)
                                {

                                }
                            });
                            //jozi_products.remove(jozi_products.get(position));
                            Remove_and_retreive_item_views(position);
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // The user clicked OK      // برای چک کردن اینکه آیتم سلکت شده یا نه کافی است همان لیست اولیه مان را عنصرش را بگیریم ، اتوماتیک
                                      //خودش مشخص خواهد کرد که عوض شده یا نه در مثال زیر می بینیم که عنصر Edit  را می توانیم ببینیم که سلکت شده یا نه .
                       Toast.makeText(jozi_products.get(0).getActivity() , checkedItems[0] ? "T" : "F" , Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return jozi_products.size();
    }

    public class view_holder_this extends RecyclerView.ViewHolder
    {
        //ویژگی های این سری تصاویری که می خواهیم نشان دهیم چیستند ؟
        private TextView pro_cart_name ;
        private TextView Total_cost_cart;

        public view_holder_this(@NonNull View itemView)
        {
            super(itemView);
            pro_cart_name = itemView.findViewById(R.id.product_name_Cart);
            Total_cost_cart = itemView.findViewById(R.id.Total_cost_Cart);
        }
    }

    private void  Remove_and_retreive_item_views(int position)
    {
        jozi_products.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, jozi_products.size());
    }
}
