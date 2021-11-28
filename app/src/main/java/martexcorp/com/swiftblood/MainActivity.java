package martexcorp.com.swiftblood;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Locale;

import martexcorp.com.swiftblood.ChoiceActivities.BoardChoice;
import martexcorp.com.swiftblood.ChoiceActivities.DatabaseChoice;
import martexcorp.com.swiftblood.ChoiceActivities.UserManagementChoice;
import martexcorp.com.swiftblood.EntryPoint.LogIn;
import martexcorp.com.swiftblood.EntryPoint.OnboardingActivity;
import martexcorp.com.swiftblood.EntryPoint.ProfileSignUp;
import martexcorp.com.swiftblood.Extras.DonateActivity;


public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_INVITE = 101;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserList,userUid;
    private static final String TAG = "TAG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        // Check if we need to display our OnboardingFragment
        if (!sharedPreferences.getBoolean("SharedPref_OnBoarding",false)){
            Intent i = new Intent(this, OnboardingActivity.class);
            startActivity(i);
            finish(); 
        }else{
            forceUserRegister(user.getUid());


        }

    }

    public void board(View view){
        Intent i = new Intent(this, BoardChoice.class);
        startActivity(i);
    }
    public void database (View view){
        Intent i = new Intent(this, DatabaseChoice.class);
        startActivity(i);
    }
    public void subscribe (View view){
        Intent i = new Intent(this, UserManagementChoice.class);
        startActivity(i);
    }

    public void forceUserRegister(String uid){

            mUserList = mRootRef.child("Users");
            userUid = mUserList.child(uid);
            userUid.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        //Not Had UID in database
                        Intent i = new Intent(getApplicationContext(), ProfileSignUp.class);
                        startActivity(i);
                        finish();
                    }else{
                        setContentView(R.layout.activity_main);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.signout:

                setContentView(R.layout.activity_log_in);
                getSupportActionBar().hide();
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(MainActivity.this, LogIn.class);
                        startActivity(i);
                        finish();
                    }
                });
                return true;

            case R.id.rate_us:
                launchMarket();
                return true;

            case R.id.language:
                languageSwitch();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void donate(){
        Intent i = new Intent(this, DonateActivity.class);
        startActivity(i);

    }

    private void languageSwitch(){
        Intent intent = new Intent( android.provider.Settings.ACTION_LOCALE_SETTINGS );
        startActivity(intent);

    }

    public static void getConnectivityStatusString(Context context) {
        String status = null;
        ConnectivityManager cm = (ConnectivityManager)           context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
            status = context.getString(R.string.mainActivity_connection);
            Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages

            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }


}
