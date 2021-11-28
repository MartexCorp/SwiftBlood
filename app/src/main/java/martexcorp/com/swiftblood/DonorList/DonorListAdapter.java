package martexcorp.com.swiftblood.DonorList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import martexcorp.com.swiftblood.BankItem.BankItem;
import martexcorp.com.swiftblood.BloodBankTable.TableLayoutActivity;
import martexcorp.com.swiftblood.R;

public class DonorListAdapter extends RecyclerView.Adapter<DonorListAdapter.MyViewHolder> {

    public List<Object> donorItemListrecycler;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView pseudo;
        public TextView town;
        public TextView bloodGrp;
        public FloatingActionButton contact;

        public MyViewHolder(View view) {
            super(view);
            pseudo = view.findViewById(R.id.donor_pseudo);
            town = view.findViewById(R.id.donor_town);
            bloodGrp = view.findViewById(R.id.donor_group);
            contact = view.findViewById(R.id.call_donor);

        }
    }


    public DonorListAdapter(Context context, List<Object> DonorListItemAdapter) {
        this.donorItemListrecycler = DonorListItemAdapter;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donor_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final DonorList donorList = (DonorList) donorItemListrecycler.get(position);
        holder.pseudo.setText(donorList.getPseudo());
        holder.town.setText(donorList.getTown());
        holder.bloodGrp.setText(donorList.getBloodGrp());


        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+donorList.getTel()));
                v.getContext().startActivity(intent);
            }
        }) ;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    public int getItemCount() {
        return donorItemListrecycler.size();
    }
}
