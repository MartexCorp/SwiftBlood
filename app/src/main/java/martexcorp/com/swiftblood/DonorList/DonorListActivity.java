package martexcorp.com.swiftblood.DonorList;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vlad1m1r.lemniscate.BernoullisProgressView;

import java.util.ArrayList;
import java.util.List;

import martexcorp.com.swiftblood.BoardRequest.BoardRequest;
import martexcorp.com.swiftblood.BoardRequest.BoardRequestAdapter;
import martexcorp.com.swiftblood.MainActivity;
import martexcorp.com.swiftblood.R;

public class DonorListActivity extends AppCompatActivity {

    public RecyclerView.Adapter adapter;
    BernoullisProgressView pulse;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mDonorListRef;
    private RecyclerView recyclerView;
    private List<Object> mRecyclerViewItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        setTitle(R.string.donorListActivity_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerview);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
        }


        adapter = new DonorListAdapter(this, mRecyclerViewItems);
        recyclerView.setAdapter(adapter);
        pulse = findViewById(R.id.id_pulse);
        MainActivity.getConnectivityStatusString(this);
        loadRecyclerData();

    }

    private void loadRecyclerData() {
        mDonorListRef = mRootRef.child("DonorList");

        mDonorListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String id = dataSnapshot.getKey();

                    String pseudo =  snapshot.child("pseudo").getValue().toString();
                    String town =  snapshot.child("town").getValue().toString();
                    String bloodGrp =  snapshot.child("bloodGrp").getValue().toString();
                    String tel =  snapshot.child("tel").getValue().toString();


                    DonorList donorList = new DonorList(pseudo,town,bloodGrp,tel);

                    mRecyclerViewItems.add(donorList);
                }
                adapter.notifyDataSetChanged();
                pulse.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DonorListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

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
