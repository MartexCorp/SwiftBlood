package martexcorp.com.swiftblood.BankItem;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import martexcorp.com.swiftblood.R;
import martexcorp.com.swiftblood.BloodBankTable.TableLayoutActivity;

public class BankItemAdapter extends RecyclerView.Adapter<BankItemAdapter.MyViewHolder> {

    public List<Object> bankItemListrecycler;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView town;
        public TextView lastUpdated;
        public FloatingActionButton contact;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.bank_name);
            town = view.findViewById(R.id.bank_region);
            lastUpdated = view.findViewById(R.id.lastUpdate);
            contact = view.findViewById(R.id.call_bank);

        }
    }


    public BankItemAdapter(Context context, List<Object> bankItemAdapter) {
        this.bankItemListrecycler = bankItemAdapter;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_database, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final BankItem bank = (BankItem) bankItemListrecycler.get(position);
        holder.name.setText(bank.getBank_name());
        holder.town.setText(bank.getBank_region());
        holder.lastUpdated.setText(bank.getLastUpdated());


        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+bank.getBank_number()));
                v.getContext().startActivity(intent);
            }
        }) ;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), TableLayoutActivity.class);
                i.putExtra("key",bank.getBankKey());
                i.putExtra("hospital",bank.getBank_name());
                i.putExtra("lastUpdated",bank.getLastUpdated());
                v.getContext().startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return bankItemListrecycler.size();
    }
}
