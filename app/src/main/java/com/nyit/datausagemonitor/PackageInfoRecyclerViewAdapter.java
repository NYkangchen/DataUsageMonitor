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
import android.widget.Toast;

import java.util.List;

public class PackageInfoRecyclerViewAdapter extends RecyclerView.Adapter<PackageViewHolder> {
    private List<PackageInfoItem> mList;
    private Context mContext;

    public PackageInfoRecyclerViewAdapter(List<PackageInfoItem> mList, Context context) {
        this.mList = mList;
        mContext = context;
    }

    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PackageViewHolder(inflater.inflate(R.layout.package_name_recycle_view_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(PackageViewHolder holder, int position) {
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

        holder.mNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                final PackageInfoDatabaseHelper db = PackageInfoDatabaseHelper.getInstance(mContext);
                List<String> selectedNames = db.getListNamesFromSelectedPackageInfoItems();

                if (selectedNames.contains(thisName)){

                    Toast.makeText(mContext, "You already selected this app", Toast.LENGTH_SHORT).show();

                }
                else {
                    builder.setTitle("Do you want to monitor data usage for " + thisName);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.insertSelectedPackages(packageInfoItem.getmName(), packageInfoItem.getmId(), packageInfoItem.getmListName());

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


                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
