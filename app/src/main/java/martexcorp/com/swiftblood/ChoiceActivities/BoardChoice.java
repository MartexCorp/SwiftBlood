package martexcorp.com.swiftblood.ChoiceActivities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import martexcorp.com.swiftblood.BoardRequest.BoardRequestActivity;
import martexcorp.com.swiftblood.R;
import martexcorp.com.swiftblood.BoardRequest.WriteDonorRequest;

public class BoardChoice extends AppCompatActivity {

    public Button post_board;
    public Button view_board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_choice);

        //post_board=findViewById(R.id.post_request);
        //view_board =findViewById(R.id.view_requests);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void view (View view){
        Intent i = new Intent(this, BoardRequestActivity.class);
        startActivity(i);
    }

    public void post (View view){
        Intent i = new Intent(this, WriteDonorRequest.class);
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
