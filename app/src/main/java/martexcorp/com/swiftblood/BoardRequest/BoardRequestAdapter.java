package martexcorp.com.swiftblood.BoardRequest;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import martexcorp.com.swiftblood.R;

/**
 * Created by Martex on 8/12/2018.
 */

public class BoardRequestAdapter extends RecyclerView.Adapter<BoardRequestAdapter.MyViewHolder> {

    public List<Object> requestListrecycler;
    private Context context;
    private Activity activity;



    public BoardRequestAdapter(Context context, List<Object> donorAdapter) {
        this.requestListrecycler = donorAdapter;
        this.context = context;
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap image = Bitmap.createBitmap(v.getWidth(),
                v.getHeight(),
                Bitmap.Config.RGB_565);
        //Draw the view inside the Bitmap
        v.draw(new Canvas(image));

        return image;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final BoardRequest request = (BoardRequest) requestListrecycler.get(position);
        holder.name.setText(request.getName());
        holder.timeAgo.setText(request.getTime());
        holder.region.setText(request.getRegion());
        holder.address.setText(request.getAddress());
        final String message =  "Derivative: "+request.getBloodGrp()+"\n"+
                "Telephone: "+request.getTel()+"\n"+
                "Posted: "+request.getTime()+"\n\n\n"+
                request.getMsg();
        final String share_message =    "SWIFTBLOOD DONATION !!!\n"+
                                        request.getName().toUpperCase()+
                                        " needs urgent help with a blood donation of derivative "+
                                        request.getBloodGrp().toUpperCase()+" in "+
                                        request.getAddress().toUpperCase()+"\n\n"+
                                        "Message: \n"+request.getMsg()+"\n\n"+
                                        "Follow the link for more info:\n"+
                                        "https://app.swiftblood.org";


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                MaterialDialog mDialog = new MaterialDialog.Builder(getActivity(v.getContext()))
                        .setAnimation(R.raw.donate_heart)
                        .setTitle(request.getName())
                        .setMessage(message)
                        .setCancelable(true)
                        .setPositiveButton("Call", R.drawable.ic_call_red_24dp, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+request.getTel()));
                                v.getContext().startActivity(intent);
                            }
                        })
                        .setNegativeButton("Share", R.drawable.ic_share_24dp, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                                share(share_message,v.getContext());

                            }
                        })
                        .build();

                // Show Dialog
                mDialog.show();


            }
        });


    }

    @Override
    public int getItemCount(){
        return requestListrecycler.size();
    }


    public Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

    public void share(String message, Context context){

        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.logo);
        try {
            File file = new File(context.getExternalCacheDir(), "postimage.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, "martexcorp.com.swiftblood.fileprovider", file));
            intent.putExtra(Intent.EXTRA_SUBJECT, "SWIFTBLOOD DONATION !!! ");
            intent.putExtra(Intent.EXTRA_TEXT,message);
            intent.setType("text/html");
            context.startActivity(Intent.createChooser(intent, "Share post via"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView bloodGrp;
        public TextView address;
        public TextView region;
        public TextView tel;
        public TextView timeAgo;
        public FloatingActionButton call;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.user_name);
            address = view.findViewById(R.id.address);
            region = view.findViewById(R.id.region);
            timeAgo = view.findViewById(R.id.timeago);

        }
    }

}

