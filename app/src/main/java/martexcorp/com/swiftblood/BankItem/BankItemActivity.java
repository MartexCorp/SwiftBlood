package martexcorp.com.swiftblood.BankItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vlad1m1r.lemniscate.BernoullisProgressView;

import java.util.ArrayList;
import java.util.List;

import martexcorp.com.swiftblood.MainActivity;
import martexcorp.com.swiftblood.R;


public class BankItemActivity extends AppCompatActivity {

    public RecyclerView.Adapter adapter;
    BernoullisProgressView pulse;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mRequestListRef;
    private RecyclerView recyclerView;
    private List<Object> mRecyclerViewItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        setTitle(R.string.bankItemActivity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BankItemAdapter(this, mRecyclerViewItems);
        recyclerView.setAdapter(adapter);

        pulse = findViewById(R.id.id_pulse);
        mRequestListRef = mRootRef.child("BloodDatabase");

        MainActivity.getConnectivityStatusString(this);


        loadRecyclerData();

    }

    public void loadRecyclerData(){

        mRequestListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mRecyclerViewItems.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DataSnapshot infoSnapshot = dataSnapshot.child("info");
                    String key = dataSnapshot.getKey();
                    String hospital = infoSnapshot.child("name").getValue().toString();
                    String tel = infoSnapshot.child("contact").getValue().toString();
                    String region = infoSnapshot.child("location").getValue().toString();
                    Long time = Long.parseLong(infoSnapshot.child("lastUpdate").getValue().toString());
                    String timeago = TimeAgo.using(time);

                    BankItem bankItem = new BankItem(hospital,region,tel,timeago,key);
                    mRecyclerViewItems.add(bankItem);
                }

                adapter.notifyDataSetChanged();
                pulse.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}



/*
    public  void loadRecyclerData (){

        mRequestListRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clear();

                String hospital = dataSnapshot.getKey();
                String name =  snapshot.child("name").getValue().toString();
                String hospital =  snapshot.child("hospital").getValue().toString();
                String region =  snapshot.child("region").getValue().toString();
                String tel =  snapshot.child("tel").getValue().toString();
                String town =  snapshot.child("address").getValue().toString();
                Long time = Long.parseLong(snapshot.child("time").getValue().toString());
                String timeString = TimeAgo.using(time);
                BoardRequest request = new BoardRequest(name,hospital,region,town,tel,timeString);
                mRecyclerViewItems.add(request);
                adapter.notifyDataSetChanged();
                pulse.setVisibility(View.INVISIBLE);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }*/
