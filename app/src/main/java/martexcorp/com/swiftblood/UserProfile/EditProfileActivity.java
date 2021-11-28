package martexcorp.com.swiftblood.UserProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import martexcorp.com.swiftblood.EntryPoint.ProfileSignUp;
import martexcorp.com.swiftblood.EntryPoint.UserDetails;
import martexcorp.com.swiftblood.R;

public class EditProfileActivity extends AppCompatActivity implements Validator.ValidationListener{

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    protected static String bloodgroup, sex;
    private int bloodgrp_check_int, sex_check_int;
    private Button edit_profile;
    private Validator validator;
    FirebaseUser user;

    private NestedLinearRadioGroup bloodgrp_radio, sex_radio;

    @NotEmpty
    private EditText name_setup,town_setup,tel_setup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle(R.string.edit_profile_activity_title);

        edit_profile = findViewById(R.id.finish_edit);
        name_setup = findViewById(R.id.name_input_edit);
        town_setup = findViewById(R.id.town_input_edit);
        tel_setup = findViewById(R.id.telephone_input_edit);
        bloodgrp_radio = findViewById(R.id.bloodgroup_edit);
        sex_radio = findViewById(R.id.radiogrp_sex_edit);
        bloodgroup= "Unspecified";
        sex = "Decline to Answer";
        user = FirebaseAuth.getInstance().getCurrentUser();

        validator = new Validator(this);
        validator.setValidationListener(this);
        fireMethods();

    }

    private void commit_user () {
        edit_user(user.getUid(),
                name_setup.getText().toString().trim(),
                town_setup.getText().toString().trim(),
                tel_setup.getText().toString().trim(),
                bloodgroup,
                sex);

    }


    public void edit_user(String uid, String pseudo, String town, String tel, String bloodgrp, String sex){

        final DatabaseReference UsersRef = mRootRef.child("Users");

        Map<String, Object> userNode = new HashMap<>();
        userNode.put(uid, new UserDetails(pseudo,town,tel, bloodgrp, sex));
        subcribeFCMTopic(bloodgrp);
        UsersRef.updateChildren(userNode);



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
                            Toast.makeText(EditProfileActivity.this, R.string.subscribe_fcm_error_toast, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(EditProfileActivity.this, R.string.subscribe_fcm_success_toast, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void fireMethods(){
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

    public void finish_profile_edit(View view){
        if((bloodgrp_check_int == 0)||(sex_check_int == 0)){

            Toast.makeText(EditProfileActivity.this, R.string.choose_group_gender_toast, Toast.LENGTH_SHORT).show();
        }else{

            validator.validate();
        }
    }

    @Override
    public void onValidationSucceeded() {

        commit_user();
        Toast.makeText(this, R.string.profile_edited_toast, Toast.LENGTH_SHORT).show();
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
