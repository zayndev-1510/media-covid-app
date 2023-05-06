package com.it015.mediacovidapp.holder.admin;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.model.admin.VideoModel;

public class VideoHolder extends RecyclerView.ViewHolder {
    public PlayerView playerView;
    public LinearLayout layout;
    public TextView label_judul,item_jml_view,item_jml_love,item_jml_komen;
    public ImageView image_delete;
    public VideoHolder(@NonNull View itemView) {
        super(itemView);
        label_judul=itemView.findViewById(R.id.item_judul_video);
        playerView=itemView.findViewById(R.id.item_muat_video);
        item_jml_view=itemView.findViewById(R.id.item_jml_view);
        item_jml_love=itemView.findViewById(R.id.item_jml_suka);
        image_delete=itemView.findViewById(R.id.delete_video_item);
        item_jml_komen=itemView.findViewById(R.id.item_jml_komentar_admin);
        layout=itemView.findViewById(R.id.layout_recycle);
    }


}
