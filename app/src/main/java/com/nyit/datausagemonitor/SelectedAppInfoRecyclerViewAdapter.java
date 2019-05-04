package com.nyit.datausagemonitor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class SelectedAppInfoRecyclerViewAdapter extends RecyclerView.Adapter<PackageViewHolder> {
    private List<PackageInfoItem> mList;
    private Context mContext;

    public SelectedAppInfoRecyclerViewAdapter(List<PackageInfoItem> mList, Context context) {
        this.mList = mList;
        mContext = context;
    }

    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PackageViewHolder(inflater.inflate(R.layout.package_name_recycle_view_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(PackageViewHolder holder, final int position) {
        final PackageInfoItem packageInfoItem = mList.get(position);

        final String thisName = packageInfoItem.getmListName();
        holder.mNameView.setText(thisName);


        Drawable icon = null;
        try {
            icon = mContext.getPackageManager().getApplicationIcon(packageInfoItem.getmName());
            holder.mIconView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        String usage = BytesConversion.getDataSize(packageInfoItem.getmUsage());

        holder.mUsageView.setText("   "+ usage );

        holder.mNameView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Stop monitor data usage for " + thisName);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PackageInfoDatabaseHelper db = PackageInfoDatabaseHelper.getInstance(mContext);

                        db.deletSelectedPackages(packageInfoItem.getmId());
         //               notifyDataSetChanged();
                        mList = PackageInfoDatabaseHelper.getInstance(mContext).getSelectedPackageInfoItems();
                        notifyItemRemoved(position);


                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();



                return  true;

            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
