package com.it015.mediacovidapp.adapter.admin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.activity.admin.DetailVideo;
import com.it015.mediacovidapp.holder.admin.DashboardVideoHolder;
import com.it015.mediacovidapp.holder.admin.VideoHolder;
import com.it015.mediacovidapp.model.admin.VideoModel;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardVideoHolder> {
    List<VideoModel> list_video;
    Context context;
    LayoutInflater layoutInflater;
    SimpleExoPlayer simpleExoPlayer;

    public DashboardAdapter(List<VideoModel> list_video, Context context) {
        this.list_video = list_video;
        this.context = context;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DashboardVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=layoutInflater.inflate(R.layout.item_data_video_admin_dashboard,parent,false);
        return new DashboardVideoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardVideoHolder holder, int position) {
        VideoModel videoModel=list_video.get(position);
        holder.label_judul.setText(videoModel.getJudul());
        String link="http://192.168.43.174:8000/video/";
        Uri uri=Uri.parse(link+videoModel.getUri());
        simpleExoPlayer = new SimpleExoPlayer.Builder(context.getApplicationContext()).build();
        holder.playerView.setPlayer(simpleExoPlayer);
        holder.item_jml_view.setText(String.valueOf(videoModel.getView()));
        holder.item_jml_love.setText(String.valueOf(videoModel.getLove()));
        holder.item_jml_komen.setText(String.valueOf(videoModel.getKomen()));
        MediaItem mediaItem = MediaItem.fromUri(uri);
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.prepare();
        holder.playerView.setOnClickListener(v->{
            Intent intent=new Intent(context.getApplicationContext(), DetailVideo.class);
            intent.putExtra("id_video",videoModel.getId());
            intent.putExtra("uri",videoModel.getUri());
            intent.putExtra("suka",videoModel.getLove());
            intent.putExtra("komen",videoModel.getKomen());
            intent.putExtra("lihat",videoModel.getView());
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return list_video.size();
    }
}
