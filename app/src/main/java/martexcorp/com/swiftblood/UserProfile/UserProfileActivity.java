package martexcorp.com.swiftblood.UserProfile;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


import martexcorp.com.swiftblood.DonorList.DonorList;
import martexcorp.com.swiftblood.EntryPoint.ProfileSignUp;
import martexcorp.com.swiftblood.MainActivity;
import martexcorp.com.swiftblood.R;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserList,mDonorList,userUid,donorUid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView conf_pseudo,pseudo_conf,conf_town,conf_tel,conf_blood_grp,conf_gender,conf_status;
    SwitchCompat donating_switch;
    private String g_pseudo,g_town,g_tel,g_bloodgroup; //global variables;
    public static  boolean donating;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        setTitle(R.string.user_profile_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        donating_switch = findViewById(R.id.donating_switch);
        conf_pseudo = findViewById(R.id.conf_pseudo);
        pseudo_conf = findViewById(R.id.username_title);
        conf_town = findViewById(R.id.conf_town);
        conf_tel = findViewById(R.id.conf_tel);
        conf_blood_grp = findViewById(R.id.conf_blood_grp);
        conf_gender = findViewById(R.id.conf_gender);
        conf_status = findViewById(R.id.conf_status);


        donating_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    add_as_donor(user.getUid(),g_pseudo,g_town,g_tel,g_bloodgroup);
                    //Toast.makeText(UserProfileActivity.this, "Successfully Added to the Donor List.", Toast.LENGTH_SHORT).show();
                    donating=true;
                }else{
                    remove_as_donor(user.getUid());
                    //Toast.makeText(UserProfileActivity.this, "Successfully Removed from the Donor List.", Toast.LENGTH_SHORT).show();
                    donating=false;

                }
            }
        });


        loadUserDetails();

    }

    public void loadUserDetails(){

        mUserList = mRootRef.child("Users");
        userUid = mUserList.child(user.getUid());

        userUid.addValueEventListener(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name =  dataSnapshot.child("username").getValue().toString();
                String tel =  dataSnapshot.child("tel").getValue().toString();
                String bloodgrp =  dataSnapshot.child("bloodGrp").getValue().toString();
                String sex = dataSnapshot.child("sex").getValue().toString();
                String town = dataSnapshot.child("town").getValue().toString();
                conf_pseudo.setText(name);
                pseudo_conf.setText(name);
                conf_town.setText(town);
                conf_tel.setText(tel);
                conf_blood_grp.setText(bloodgrp);
                conf_gender.setText(sex);
                donating(user.getUid());

                g_pseudo = name;
                g_town = town;
                g_tel=tel;
                g_bloodgroup = bloodgrp;


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        //adapter.notifyDataSetChanged();
    }

    public void donating (String uid){
         mDonorList = mRootRef.child("DonorList");
         donorUid = mDonorList.child(uid);
         donorUid.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(!dataSnapshot.exists()) {
                     //Not Donating Now
                     conf_status.setText(R.string.donating_status_free);
                     donating_switch.setChecked(false);
                     donating = false;
                 }else{
                     //Donating Now
                     conf_status.setText(R.string.donating_status_donating);
                     donating_switch.setChecked(true);
                     donating= true;
                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 Log.d(TAG, databaseError.getMessage());
             }
         });


    }

    public void add_as_donor(String uid, String pseudo, String town, String tel, String bloodgrp) {

        final DatabaseReference DonorsRef = mRootRef.child("DonorList");

        Map<String, Object> donorNode = new HashMap<>();
        donorNode.put(uid, new DonorList(pseudo, town, bloodgrp, tel));

        DonorsRef.updateChildren(donorNode);
    }

    public void remove_as_donor(String uid) {

        mDonorList = mRootRef.child("DonorList");
        mDonorList.child(uid).removeValue();

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void editProfile(View view){

        Intent i = new Intent(this, EditProfileActivity.class);
        startActivity(i);


    }
}
