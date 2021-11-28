package martexcorp.com.swiftblood.BloodBankTable;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import martexcorp.com.swiftblood.R;

public class TableLayoutAdapter extends RecyclerView.Adapter<TableLayoutAdapter.MyViewHolder> {

    public List<Object> tableRowListrecycler;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView bloodGrp;
        public TextView whole;
        public TextView rbc;
        public TextView plasma;
        public TextView platelets;
        public TextView summation;

        public MyViewHolder(View view) {
            super(view);
            bloodGrp = view.findViewById(R.id.blood_grp_txt);
            whole = view.findViewById(R.id.whole_blood_txt);
            rbc = view.findViewById(R.id.rbc_txt);
            plasma = view.findViewById(R.id.plasma_txt);
            platelets = view.findViewById(R.id.platelets_txt);
            summation = view.findViewById(R.id.summation_txt);

        }
    }

    public TableLayoutAdapter(Context context, List<Object> tableRowAdapter) {
        this.tableRowListrecycler = tableRowAdapter;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_table_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final TableRowItem tableRowItem = (TableRowItem) tableRowListrecycler.get(position);
        holder.bloodGrp.setText(tableRowItem.getBlood_group());
        holder.whole.setText(String.valueOf(tableRowItem.getWhole_blood()));
        holder.rbc.setText(String.valueOf(tableRowItem.getRbc()));
        holder.plasma.setText(String.valueOf(tableRowItem.getPlasma()));
        holder.platelets.setText(String.valueOf(tableRowItem.getPlatelets()));
        holder.summation.setText(String.valueOf(tableRowItem.getSummation()));

    }


    @Override
    public int getItemCount() {
        return tableRowListrecycler.size();
    }
}
