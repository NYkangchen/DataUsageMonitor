package com.nyit.datausagemonitor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PackageViewHolder extends RecyclerView.ViewHolder {
    TextView mNameView;
    ImageView mIconView;
    TextView mUsageView;


    public PackageViewHolder(View itemView) {
        super(itemView);
        mNameView = itemView.findViewById(R.id.packe_name_textview);
        mIconView = itemView.findViewById(R.id.icon_image_view);
        mUsageView = itemView.findViewById(R.id.show_app_data_usage_textview);
    }
}
