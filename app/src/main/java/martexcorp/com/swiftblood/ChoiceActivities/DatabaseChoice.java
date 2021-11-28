package martexcorp.com.swiftblood.ChoiceActivities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import martexcorp.com.swiftblood.BankItem.BankItemActivity;
import martexcorp.com.swiftblood.DonorList.DonorList;
import martexcorp.com.swiftblood.DonorList.DonorListActivity;
import martexcorp.com.swiftblood.R;

public class DatabaseChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_choice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void donorList (View view){
        Intent i = new Intent(this, DonorListActivity.class);
        startActivity(i);
    }

    public void bloodBank (View view){
        Intent i = new Intent(this, BankItemActivity.class);
        startActivity(i);
    }
}
