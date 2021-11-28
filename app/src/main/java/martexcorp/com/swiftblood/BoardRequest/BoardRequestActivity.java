package martexcorp.com.swiftblood.BoardRequest;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vlad1m1r.lemniscate.BernoullisProgressView;

import java.util.ArrayList;
import java.util.List;

import martexcorp.com.swiftblood.MainActivity;
import martexcorp.com.swiftblood.R;

//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

public class BoardRequestActivity extends AppCompatActivity {



    public RecyclerView.Adapter adapter;
    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String town;
    BernoullisProgressView pulse;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mRequestListRef;
    private RecyclerView recyclerView;
    private List<Object> mRecyclerViewItems = new ArrayList<>();

    //private ArrayList<String> pass_town_data = new ArrayList<String>();

    // public ArrayList<String> getPass_town_data() { return pass_town_data; }

//    String places_kml = "android.resource://" + getPackageName() + "/"+R.raw.places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        setTitle(R.string.boardRequestActivity_title);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BoardRequestAdapter(this, mRecyclerViewItems);
        recyclerView.setAdapter(adapter);

        pulse = findViewById(R.id.id_pulse);

        MainActivity.getConnectivityStatusString(this);

        loadRecyclerData();

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    private void loadRecyclerData() {
        mRequestListRef = mRootRef.child("RequestBoard");

        mRequestListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String id = dataSnapshot.getKey();

                    String name =  snapshot.child("name").getValue().toString();
                    String bloodGrp =  snapshot.child("bloodGrp").getValue().toString();
                    String region =  snapshot.child("region").getValue().toString();
                    String tel =  snapshot.child("tel").getValue().toString();
                    String town =  snapshot.child("address").getValue().toString();
                    String msg = snapshot.child("msg").getValue().toString();
                    Long time = Long.parseLong(snapshot.child("time").getValue().toString());

                    String timeString = TimeAgo.using(time);

                    BoardRequest request = new BoardRequest(name,bloodGrp,region,town,tel,timeString,msg);

                    mRecyclerViewItems.add(request);

                    //Toast.makeText(BoardRequestActivity.this, "Info is :"+key, Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                pulse.setVisibility(View.INVISIBLE);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BoardRequestActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();


            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void clear(){

        mRecyclerViewItems.clear();
    }


}