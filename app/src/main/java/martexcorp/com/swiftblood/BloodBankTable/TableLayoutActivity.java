package martexcorp.com.swiftblood.BloodBankTable;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import martexcorp.com.swiftblood.MainActivity;
import martexcorp.com.swiftblood.R;

public class TableLayoutActivity extends AppCompatActivity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mBloodListRef;
    public RecyclerView recyclerView;
    public List<Object> mRecyclerViewItems = new ArrayList<>();
    public RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_layout);

        TextView lastUpdt = findViewById(R.id.lastlyUpdated_txt);

        Intent intent = getIntent();
        String bankKey = intent.getStringExtra("key");
        String hospitalName = intent.getStringExtra("hospital");
        String lastUpdate = intent.getStringExtra("lastUpdated");

        setTitle(hospitalName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lastUpdt.setText(getString(R.string.tableLayoutActivity_lastlyUpdated)+lastUpdate);

        recyclerView = findViewById(R.id.tableRow_recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TableLayoutAdapter(this,mRecyclerViewItems);
        recyclerView.setAdapter(adapter);

        MainActivity.getConnectivityStatusString(this);

        mBloodListRef = mRootRef.child("BloodDatabase").child(bankKey);
        Query query =  mBloodListRef.limitToFirst(8);
        query.addListenerForSingleValueEvent(valueEventListener);


    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                String bloodGroup = dataSnapshot.getKey();

                DecimalFormat formatter = new DecimalFormat("00");

                    Long whole = (Long) dataSnapshot.child("whole").getValue();
                    String whole_txt = formatter.format(whole);
                    Long rbc = (Long) dataSnapshot.child("rbc").getValue();
                    String rbc_txt = formatter.format(rbc);
                    Long plasma = (Long) dataSnapshot.child("plasma").getValue();
                    String plasma_txt = formatter.format(plasma);
                    Long platelets = (Long) dataSnapshot.child("platelets").getValue();
                    String platelets_txt = formatter.format(platelets);


                    Long sum = whole + rbc + plasma + platelets;
                    String sum_txt = formatter.format(sum);

                    TableRowItem tableRowItem = new TableRowItem(bloodGroup,whole_txt,rbc_txt,plasma_txt,platelets_txt,sum_txt);
                    mRecyclerViewItems.add(tableRowItem);

            }
            adapter.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {


        }
    };

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
