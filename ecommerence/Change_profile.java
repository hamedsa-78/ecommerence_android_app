package com.example.eating_food;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Change_profile extends AppCompatActivity {

    //variables
    private Uri image_uri;
    private String download_uri;
    private String path;
    private int code_img_change=0;
    //Views
    private EditText change_name,change_password;
    private TextView change_profile_img_txt;
    private TextView Close,Update;
    private CircleImageView img_profile;
    private Button change_inform_btn;

    //working with Firebase database
   private StorageReference storageReference;
   private DatabaseReference databaseReference;
   private DatabaseReference databaseReference_for_change;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        //Images functions
        img_profile=findViewById(R.id.prof_img);
        change_profile_img_txt=findViewById(R.id.change_profile_img_txt);

        //Tex views
        Close=findViewById(R.id.change_close);
        Update=findViewById(R.id.change_update);

        //Edit text
        change_name=findViewById(R.id.change_username);
        change_password=findViewById(R.id.change_password);
        change_inform_btn=findViewById(R.id.change_inform_btn);

        //initial Dtabase,s
        databaseReference= FirebaseDatabase.getInstance().getReference().child("user").child(Prevalent.phonekey);
        storageReference= FirebaseStorage.getInstance().getReference().child("product_image");

        databaseReference_for_change=FirebaseDatabase.getInstance().getReference().child("user");

        path=getIntent().getStringExtra("path_of_image");
        if(!path.equals(""))
            Picasso.get().load(path).into(img_profile);

        // view .OnClickListeners
        change_profile_img_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,code_img_change);
            }
        });
        change_inform_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String new_username=change_name.getText().toString();
                String new_password=change_password.getText().toString();

                databaseReference.child("username").setValue(new_username);
                databaseReference.child("password").setValue(new_password);
                //put this in sign_in_act class
                /*Query query=databaseReference_for_change.orderByKey().equalTo(new_username);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){  //******************* for learning , actully this should be checked on sign_in_act class; this if does nothing

                            databaseReference_for_change.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    HashMap<String,Object> HM=new HashMap<>();

                                    HM.put("username",dataSnapshot.child("username").getValue().toString());
                                    //HM.put("password",dataSnapshot.child("password").getValue().toString());
                                    //if(dataSnapshot.child("profile_uri").exists())
                                    //  HM.put("profile_uri",dataSnapshot.child("profile_uri").getValue().toString());

                                   // databaseReference.child(new_username).setValue(HM);
                                    //  databaseReference.removeValue();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                          else{
                            Toast.makeText(Change_profile.this,"this phonekey is already exixst please try another phonekey",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

            }
        });
    }

    // ****************************** End of Create Function *******************************



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==code_img_change){
            if(resultCode== Activity.RESULT_OK){
                image_uri=data.getData();
                img_profile.setImageURI(image_uri);
                Save_image_to_Firebase();

            }
        }
    }
    private void Save_image_to_Firebase() {

        //get current time for unique key
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss");
        String product_random_key= time.format(calendar.getTime());

        //complete initialing refrences and introduce upload task
        final StorageReference filepath=storageReference.child(image_uri.getLastPathSegment()+product_random_key+".jpg");
        final UploadTask uploadTask= filepath.putFile(image_uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Change_profile.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        download_uri=uri.toString();
                        if(download_uri.equals("")){
                            Toast.makeText(Change_profile.this,"Unfortunality failed,try again plese",Toast.LENGTH_LONG).show();
                        } else {
                            HashMap<String, Object> s = new HashMap<>();
                            s.put("profile_uri", download_uri);
                            s.put("username",Prevalent.usernamekey);
                            s.put("password",Prevalent.passwordkey);
                            databaseReference.setValue(s);
                            Toast.makeText(Change_profile.this,"Wrote succesfully",Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Change_profile.this,e.toString()+ " try again please",Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

    }
}
