package martexcorp.com.swiftblood.BoardRequest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.angmarch.views.NiceSpinner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import martexcorp.com.swiftblood.R;


public class WriteDonorRequest extends AppCompatActivity implements Validator.ValidationListener {


    public Button post;

    @NotEmpty
    public EditText names, address_text, telephone_text, message_text;
    public NiceSpinner regionSpinner, groupSpinner;
    public CheckBox terms;
    int finalRand;
    private Validator validator;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth;
    private TextView text_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.writeDonorActivity_title);

        post = findViewById(R.id.postrequest);
        names = findViewById(R.id.name_input);
        address_text = findViewById(R.id.town_input);
        telephone_text = findViewById(R.id.telephone_input);
        regionSpinner = findViewById(R.id.region_spinner);
        groupSpinner = findViewById(R.id.derivative_spinner);
        terms = findViewById(R.id.terms_checkbox);
        message_text = findViewById(R.id.message_input);
        text_count = findViewById(R.id.txt_count);

        validator = new Validator(this);
        validator.setValidationListener(this);

        message_text.addTextChangedListener(mTextEditorWatcher);


        terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    post.setVisibility(View.VISIBLE);
                } else {
                    post.setVisibility(View.INVISIBLE);
                }
            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public void postDB() {
        final DatabaseReference requestRef = mRootRef.child("RequestBoard");
        int rand;
        //final int randId = new Random().nextInt(  9988+ 1);
        do {
            rand = new Random().nextInt(9989);
        }
        while (rand < 1122);

        final int randId = rand;
        requestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child(String.valueOf(randId)).exists()) {
                        //do rand again
                        int newRand;
                        do {
                            newRand = new Random().nextInt(randId + 1);
                        }
                        while (newRand < 1122);
                        finalRand = newRand + randId;
                    } else {
                        //do something if not exists
                        finalRand = randId;
                    }

                    Long tsLong = System.currentTimeMillis();

                    String name, bloodGrp, address, telephone, region, msg, time;
                    name = names.getText().toString().trim();
                    bloodGrp = groupSpinner.getText().toString().trim();
                    address = address_text.getText().toString().trim();
                    telephone = telephone_text.getText().toString().trim();
                    region = regionSpinner.getSelectedItem().toString().trim();
                    msg = message_text.getText().toString().trim();
                    time = tsLong.toString();


                    Map<String, Object> request = new HashMap<>();
                    request.put(String.valueOf(finalRand), new BoardRequest(name, bloodGrp, region, address, telephone, time, msg));

                    requestRef.updateChildren(request);
                }

                sendFCM(address_text.getText().toString(),names.getText().toString(),groupSpinner.getText().toString());


                //new Notification("/topics/Uncategorized",names.getText().toString()+" may need your help !!!",hospital_text.getText().toString()+" needed Urgently");



                names.setText("");
                address_text.setText("");
                telephone_text.setText("");
                message_text.setText("");
                groupSpinner.setSelectedIndex(0);
                regionSpinner.setSelectedIndex(0);

                Toast.makeText(WriteDonorRequest.this, R.string.writeDonorActivity_writeSuccessful, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        //Post to firebase

    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            text_count.setText(String.valueOf(s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    public void postBtn(View v) {
        if (names.getText().toString().trim().equals("") ||
                address_text.getText().toString().trim().equals("") ||
                telephone_text.getText().toString().trim().equals("") ||
                message_text.getText().toString().trim().equals("")) {


        } else {


        }

        validator.validate();
    }


    @Override
    public void onValidationSucceeded() {
        postDB();
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

    public void sendFCM( String address, String names, String bloodGroup){

        String title = getString(R.string.sendFCM_title);
        String body = names+getString(R.string.sendFCM_body)+bloodGroup+getString(R.string.sendFCM_at) +address+getString(R.string.sendFCM_urgently);
        String topic;
        switch (bloodGroup) {

            case "O+":
                topic = "O_plus";
                new Notification("/topics/"+topic,title,body);
                break;

            case "O-":
                topic = "O_minus";
                new Notification("/topics/"+topic,title,body);

                break;

            case "A+":
                topic = "A_plus";
                new Notification("/topics/"+topic,title,body);

                break;

            case "A-":
                topic = "A_minus";
                new Notification("/topics/"+topic,title,body);

                break;

            case "B+":
                topic = "B_plus";
                new Notification("/topics/"+topic,title,body);

                break;

            case "B-":
                topic = "B_minus";
                new Notification("/topics/"+topic,title,body);

                break;

            case "AB+":
                topic = "AB_plus";
                new Notification("/topics/"+topic,title,body);

                break;

            case "AB-":
                topic = "AB_minus";
                new Notification("/topics/"+topic,title,body);

                break;

            default:
                topic = "Uncategorized";
        }

        new Notification("/topics/Uncategorized"+topic,title,body);


    }
}


