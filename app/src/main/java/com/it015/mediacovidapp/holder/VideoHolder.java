package com.it015.mediacovidapp.holder;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.model.VideoModel;

import org.w3c.dom.Text;

import java.util.List;

public class VideoHolder extends RecyclerView.ViewHolder {
    public TextView jumlah_love;
    public TextView jumlah_view;
    public TextView format_tgl;
    public TextView judul_video;
    public PlayerView playerView;
    public ProgressBar loading;
    public LinearLayout linearLayout;
    public VideoHolder(@NonNull View itemView) {

        super(itemView);
        jumlah_view=itemView.findViewById(R.id.item_jumlah_view_pengguna);
        jumlah_love=itemView.findViewById(R.id.item_jumlah_like_pengguna);
        playerView=itemView.findViewById(R.id.item_muat_video_pengguna);
        format_tgl=itemView.findViewById(R.id.item_format_tgl_pengguna);
        judul_video=itemView.findViewById(R.id.item_judul_video_pengguna);
        loading=itemView.findViewById(R.id.loading_video_pengguna_home);
        linearLayout=itemView.findViewById(R.id.linear_klik);
    }


}
