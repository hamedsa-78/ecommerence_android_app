package com.example.eating_food;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.snackbar.Snackbar;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private TextView welcome_messege;

        private AppBarConfiguration mAppBarConfiguration;
        private SharedPreferences settings;
        private DatabaseReference databaseReference;

        private RecyclerView recyclerView;
        private RecyclerView.LayoutManager layoutManager;
        private RecyclerView.Adapter main_adapter;


        ArrayList<Products> products;

        String product_name_st,description_st,price_st;
        String Img_uri;
        private String path;
        private StorageReference product_img_ref;
        private DatabaseReference databaseReference_for_prof_show=FirebaseDatabase.getInstance().getReference().child("user");

        private ImageView pr_img;

        private com.google.android.material.floatingactionbutton.FloatingActionButton cart_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Home");
        settings=getSharedPreferences("Data", Context.MODE_PRIVATE);
        setSupportActionBar(toolbar);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Products");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);



        recyclerView=findViewById(R.id.recycler_menue);  //*************************

        products=new ArrayList<>();
        Fething_to_products();  //**************************

        layoutManager = new LinearLayoutManager(this);
        main_adapter = new Main_adapter(products);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(main_adapter);

        product_img_ref= FirebaseStorage.getInstance().getReference().child("product_image");

        cart_button = findViewById(R.id.fab);
        cart_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(home.this , Cart_list.class);
                startActivity(intent);
            }
        });

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.nav_app_bar_open_drawer_description,R.string.navigation_drawer_close);
        NavigationView navigationView = findViewById(R.id.nav_view);   // ************************

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Passing each menu ID as a set of I
        // ds because each
        // menu should be considered as top level destinations.

       /* mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/
    }

    private void Fething_to_products() {

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                product_name_st=dataSnapshot.child("Apr 01,202003:16:15").child("product_name").getValue().toString();
                description_st=dataSnapshot.child("Apr 01,202003:16:15").child("description").getValue().toString();
                price_st=dataSnapshot.child("Apr 01,202003:16:15").child("price").getValue().toString();
                Img_uri=  dataSnapshot.child("Apr 01,202003:16:15").child("image_uri").getValue().toString();

               // products.add(new Products(product_name_st,description_st,price_st,Img_uri));
                for(int i=0;i<6;i++)
                products.add(new Products(product_name_st,description_st,price_st,Img_uri,"Apr 01,202003:16:15",home.this));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        Toast.makeText(this , "Create option menu  " , Toast.LENGTH_LONG).show();

        //set Hi (Username , s name)
        welcome_messege=findViewById(R.id.user_name_for_welcome);
        welcome_messege.setText("Hi " + Prevalent.usernamekey);

        //set Profile ,s image

        pr_img = findViewById(R.id.profile_image);  // ******************

        databaseReference_for_prof_show.addValueEventListener(new ValueEventListener()  //  عکس
        {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(Prevalent.phonekey).child("profile_uri").exists())
                {
                    //  String path=dataSnapshot.child("user").child(Prevalent.phonekey).child("profile_uri").getValue().toString(); // error below is right
                    path = dataSnapshot.child(Prevalent.phonekey).child("profile_uri").getValue().toString();
                        if (!path.equals(""))
                            Picasso.get().load(path).fit().into(pr_img);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*  int id=item.getItemId();
      if(id==R.id.nav_logout){
            return true;
        }*/
        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

   @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
   {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

      /*  if (id == R.id.nav_cart) {

        }
        else if (id == R.id.orders) {

        }
        else if (id == R.id.catagories) {

        }*/
         if (id == R.id.nav_setting) {

            Intent intent=new Intent(home.this,Change_profile.class);
            intent.putExtra("path_of_image",path);
            startActivity(intent);
        }

        else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor=settings.edit();
            editor.clear();
            editor.apply();
            Intent intent=new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }
        DrawerLayout drawer=findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}

