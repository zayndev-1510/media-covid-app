package com.it015.mediacovidapp.holder.admin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.it015.mediacovidapp.R;

public class DashboardVideoHolder extends RecyclerView.ViewHolder {
    public PlayerView playerView;
    public TextView label_judul,item_jml_view,item_jml_love,item_jml_komen;
    public DashboardVideoHolder(@NonNull View itemView) {
        super(itemView);
        playerView=itemView.findViewById(R.id.item_muat_video_dashboard);
        label_judul=itemView.findViewById(R.id.item_judul_video_dashboard);
        item_jml_love=itemView.findViewById(R.id.item_jml_suka_dashboard);
        item_jml_view=itemView.findViewById(R.id.item_jml_view_dashboard);
        item_jml_komen=itemView.findViewById(R.id.item_jml_komentar_dashboard);
    }
}
