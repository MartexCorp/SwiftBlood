package martexcorp.com.swiftblood.EntryPoint;

import android.app.NotificationChannel;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import martexcorp.com.swiftblood.MainActivity;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new LoadDashboard().execute();

    }

    private class LoadDashboard extends AsyncTask<Boolean, Boolean, Boolean> {

        private Boolean userSignedIn;

        @Override
        protected Boolean doInBackground(Boolean... booleans) {


            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();

            if (user != null) {
                userSignedIn = true;
            } else {
                userSignedIn = false;
            }

            return userSignedIn;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashScreen.this, LogIn.class);
                startActivity(intent);
                finish();


            }
        }


    }


}
