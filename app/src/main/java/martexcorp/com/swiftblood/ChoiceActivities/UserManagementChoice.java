package martexcorp.com.swiftblood.ChoiceActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.thefinestartist.finestwebview.FinestWebView;

import martexcorp.com.swiftblood.R;
import martexcorp.com.swiftblood.UserProfile.UserProfileActivity;

public class UserManagementChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management_choice);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void myAccount (View view){
        Intent i = new Intent(this, UserProfileActivity.class);
        startActivity(i);
    }

    public void blog (View view){
        new FinestWebView.Builder(this)
                .webViewUseWideViewPort(true)
                .webViewAllowContentAccess(true)
                .webViewSupportZoom(true)
                .webViewJavaScriptEnabled(true)
                .webViewLoadsImagesAutomatically(true)
                .webViewBlockNetworkImage(false)
                .showUrl(false)
                .showMenuRefresh(false)
                .show("https://www.swiftblood.org/blog");
    }
}