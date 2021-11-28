package martexcorp.com.swiftblood.EntryPoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jbvincey.nestedradiobutton.NestedLinearRadioGroup;
import com.jbvincey.nestedradiobutton.NestedRadioButton;
import com.jbvincey.nestedradiobutton.NestedRadioGroupManager;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import martexcorp.com.swiftblood.DonorList.DonorList;
import martexcorp.com.swiftblood.MainActivity;
import martexcorp.com.swiftblood.R;

public class ProfileSignUp extends AppCompatActivity implements Validator.ValidationListener {
    protected static String bloodgroup, sex;
    FirebaseUser user;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    private int bloodgrp_check_int, sex_check_int;
    private CheckBox terms_checkbox_setup;
    private Switch donor_switch;
    private Button finish_setup, pick_age;
    private Validator validator;

    private NestedLinearRadioGroup bloodgrp_radio, sex_radio;

    @NotEmpty
    private EditText name_setup,town_setup,tel_setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sign_up);
        terms_checkbox_setup = findViewById(R.id.terms_checkbox_setup);
        donor_switch = findViewById(R.id.donor_switch);
        finish_setup = findViewById(R.id.finish_setup);
        name_setup = findViewById(R.id.name_input_setup);
        town_setup = findViewById(R.id.town_input_setup);
        tel_setup = findViewById(R.id.telephone_input_setup);
        bloodgrp_radio = findViewById(R.id.bloodgroup);
        sex_radio = findViewById(R.id.radiogrp_sex);
        bloodgroup= getString(R.string.bloodgroup_default);
        sex = getString(R.string.sex_default);

        user = FirebaseAuth.getInstance().getCurrentUser();
        getTel_Email(user);
        fireMethods();

        validator = new Validator(this);
        validator.setValidationListener(this);

        finish_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((bloodgrp_check_int == 0)||(sex_check_int == 0)){

                    Toast.makeText(ProfileSignUp.this, R.string.profile_signup_validation_toast, Toast.LENGTH_SHORT).show();
                }else{

                    validator.validate();
                }

            }
        });
    }

    private void commit_user () {
            setup_user(user.getUid(),
                    name_setup.getText().toString().trim(),
                    town_setup.getText().toString().trim(),
                    tel_setup.getText().toString().trim(),
                    bloodgroup,
                    sex);

    }

        private void commit_donor(){
                setup_donor(user.getUid(),
                        name_setup.getText().toString().trim(),
                        town_setup.getText().toString().trim(),
                        tel_setup.getText().toString().trim(),
                        bloodgroup);
        }



    public void fireMethods(){

        terms_checkbox_setup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    donor_switch.setVisibility(View.VISIBLE);
                    finish_setup.setVisibility(View.VISIBLE);
                }else{
                    donor_switch.setVisibility(View.INVISIBLE);
                    finish_setup.setVisibility(View.INVISIBLE);
                }
            }
        });

        bloodgrp_radio.setOnCheckedChangeListener(new NestedRadioGroupManager.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroupManager groupManager, int checkedId) {
                bloodgrp_check_int =groupManager.getCheckedId();
                NestedRadioButton temp_radiobtn = findViewById(checkedId);
                bloodgroup = temp_radiobtn.getText().toString();

            }
        });

        sex_radio.setOnCheckedChangeListener(new NestedRadioGroupManager.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NestedRadioGroupManager groupManager, int checkedId) {
                sex_check_int = groupManager.getCheckedId();
                NestedRadioButton temp_radiobtn = findViewById(checkedId);
                sex = temp_radiobtn.getText().toString();

            }
        });


    }

    public void getTel_Email(FirebaseUser user){

        if (!(user.getPhoneNumber()== null)) {
            tel_setup.setText(user.getPhoneNumber());
        }else if(!(user.getEmail().matches(""))){
            tel_setup.setText("");
        }

    }

    public void setup_user(String uid, String pseudo, String town, String tel, String bloodgrp, String sex){

        final DatabaseReference UsersRef = mRootRef.child("Users");

        Map<String, Object> userNode = new HashMap<>();
        userNode.put(uid, new UserDetails(pseudo,town,tel, bloodgrp, sex));
        subcribeFCMTopic(bloodgrp);

        UsersRef.updateChildren(userNode);



    }

    public void setup_donor(String uid, String pseudo, String town, String tel, String bloodgrp){

        final DatabaseReference DonorsRef = mRootRef.child("DonorList");

        Map<String, Object> donorNode = new HashMap<>();
        donorNode.put(uid, new DonorList(pseudo,town,bloodgrp, tel));

        DonorsRef.updateChildren(donorNode);



    }


    public void subcribeFCMTopic(String bloodGroup){

        String censored_bloodgroup;
        switch (bloodGroup) {

            case "O+":
                censored_bloodgroup = "O_plus";
                break;

            case "O-":
                censored_bloodgroup = "O_minus";
                break;

            case "A+":
                censored_bloodgroup = "A_plus";
                break;

            case "A-":
                censored_bloodgroup = "A_minus";
                break;

            case "B+":
                censored_bloodgroup = "B_plus";
                break;

            case "B-":
                censored_bloodgroup = "B_minus";
                break;

            case "AB+":
                censored_bloodgroup = "AB_plus";
                break;

            case "AB-":
                censored_bloodgroup = "AB_minus";
                break;

             default:
                 censored_bloodgroup = "Uncategorized";
        }

        FirebaseMessaging.getInstance().subscribeToTopic(censored_bloodgroup)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(ProfileSignUp.this, R.string.profile_signup_subscribe_error_toast, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ProfileSignUp.this, R.string.profile_signup_subscribe_success_toast, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onValidationSucceeded() {
            //Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
        if (donor_switch.isChecked()){
            commit_user();
            commit_donor();
        }else{
            commit_user();
        }

        //commit to server
        //edit Shared Preferences Value

        SharedPreferences.Editor sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        sharedPreferencesEditor.putBoolean("SharedPref_OnBoarding", true);
        sharedPreferencesEditor.apply();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }



}
