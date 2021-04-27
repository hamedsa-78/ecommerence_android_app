package com.example.eating_food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cart_list extends AppCompatActivity
    {


        private RecyclerView recyclerView;
        private SearchView searchbar;
        private RecyclerView.LayoutManager layoutManager;
        private RecyclerView.Adapter main_adapter;
        public ArrayList <Jozi_product> mProgramList;
        public ArrayList <Jozi_product> item_copy;
        private DatabaseReference databaseReference;

        private TextView total_cost ;
        private TextView dollar ;

        private int tt_cost = 0 ;

        private Button buy ;

        private Button calc_total ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        Toast.makeText(this , "Cart_list + related to main_adapter_cart " , Toast.LENGTH_LONG).show();

        buy = findViewById(R.id.Buy_Cart);
        calc_total = findViewById(R.id.calc_total_cost);
        searchbar = findViewById(R.id.search_bar);

        total_cost = findViewById(R.id.total_cost);
        dollar     = findViewById(R.id.dollar);

        recyclerView = findViewById(R.id.recycler_menue_Cart);
        layoutManager = new LinearLayoutManager(this);          //  چه کسی این شی های نمایش داده شده را مدیریت می کند ؟
                                                                       // همین کلاس
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // الکی
        databaseReference = FirebaseDatabase.getInstance().getReference().child("card_list").child("user_view");
        mProgramList = new ArrayList<>();
        //

        item_copy = new ArrayList<>();
        Fetch_jozi_products();



        main_adapter = new main_adapter_Cart(mProgramList , tt_cost);
        recyclerView.setAdapter(main_adapter);

        calc_total.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dollar.setText("$ " + tt_cost);
            }
        });


        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                filter(query );
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                filter(newText );

                return true;
            }
        });

    }

        public void filter(String text)
        {
           mProgramList.clear();

            if(!text.isEmpty())
            {
                for (int i = 0 ; i < item_copy.size() ; i++)
                {
                    if (item_copy.get(i).getProduct_name().equals(text))
                    {
                        mProgramList.add(item_copy.get(i));
                    }
                }
            }
            else
            {
                mProgramList.addAll(item_copy);
            }
            main_adapter.notifyDataSetChanged();
        }

        private void Fetch_jozi_products()
        {

            databaseReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                    Iterator <DataSnapshot> items = dataSnapshot.getChildren().iterator();

                    while (items.hasNext())
                    {
                        DataSnapshot item = items.next();

                        String pro_name = item.child("product_name").getValue().toString();
                        String total = item.child("product_price").getValue()  .toString();
                        tt_cost += Integer.parseInt(total);


                       // final int pr_n = Integer.parseInt(p);
                        //int pr_p = Integer.valueOf(item.child("product_price").toString());
                      // final int total = pr_n * pr_n ;

                      //  String total_cost = String.valueOf(total);
                         Jozi_product jozi_product ;


                        jozi_product = new Jozi_product(pro_name , total , item.getKey() , Cart_list.this);
                        mProgramList.add(jozi_product);
                        item_copy.add(jozi_product);

                    }

                  /*  if (dataSnapshot.exists())
                    {

                       /* for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        {
                           //  Jozi_product jozi_product ;

                          //  String pro_name = dataSnapshot1.child("product_name").getValue().toString();
                         //   String total_cost = String.valueOf(Integer.parseInt(dataSnapshot1.child("admin_view").child("product_number").toString()) * Integer.parseInt(dataSnapshot1.child("admin_view").child("product_price").toString()));

                             String total_cost = "54" ;
                           //  jozi_product = new Jozi_product(pro_name,total_cost);

                           //  mProgramList.add(jozi_product);

                        }
                    }*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });

           // for (int i=0;i<12;i++)
            //mProgramList.add(new Jozi_product("sadsd","464"));

        }

    }
