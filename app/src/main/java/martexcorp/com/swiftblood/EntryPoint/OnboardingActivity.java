package martexcorp.com.swiftblood.EntryPoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.crashlytics.android.BuildConfig;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;


import io.fabric.sdk.android.Fabric;
import martexcorp.com.swiftblood.R;

public class OnboardingActivity extends AppIntro2{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar

        crashanalytics_init();

        onBoardingMethod();

    }

    public void onBoardingMethod(){

        addSlide(AppIntroFragment.newInstance(getString(R.string.onboarding_request_title),getString(R.string.onboarding_request_body),R.drawable.onboarding_request_ai,Color.parseColor("#9999cc")));

        addSlide(AppIntroFragment.newInstance(getString(R.string.onboarding_bank_title),getString(R.string.onboarding_bank_data),R.drawable.onboarding_blood_bank_ai,Color.parseColor("#666699")));

        addSlide(AppIntroFragment.newInstance(getString(R.string.onboarding_list_title),getString(R.string.onboarding_list_body),R.drawable.onboarding_free_donor_list_ai,Color.parseColor("#99cccc")));

        addSlide(AppIntroFragment.newInstance(getString(R.string.onboarding_assistance_title),getString(R.string.onboarding_assistance_body),R.drawable.onboarding_learn_more_ai,Color.parseColor("#009999")));





        showSkipButton(false);
        setProgressButtonEnabled(true);

    }


    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.

        SharedPreferences.Editor sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        sharedPreferencesEditor.putBoolean("SharedPref_OnBoarding", true);
        sharedPreferencesEditor.apply();

        // Your implementation
        Intent i = new Intent(this, ProfileSignUp.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    

    public void crashanalytics_init(){
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        Fabric.with(this, crashlyticsKit);
    }

    public void romove_titile_bar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
    }
}