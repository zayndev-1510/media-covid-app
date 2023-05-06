package com.it015.mediacovidapp.adapter.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.activity.admin.DetailVideo;
import com.it015.mediacovidapp.activity.admin.Video;
import com.it015.mediacovidapp.holder.admin.EventData;
import com.it015.mediacovidapp.holder.admin.VideoHolder;
import com.it015.mediacovidapp.model.admin.VideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoAdapter extends RecyclerView.Adapter<VideoHolder>  {

    Context context;
    List<VideoModel> list_video;
    LayoutInflater layoutInflater;
    SimpleExoPlayer simpleExoPlayer;


    public EventData eventData;
    boolean checked=false;
    List<String> selected_list=new ArrayList<>();
    public VideoAdapter(Context context, List<VideoModel> list_video,EventData eventData) {
        this.context = context;
        this.list_video = list_video;
        this.layoutInflater=LayoutInflater.from(context);
        this.eventData = eventData;
    }
    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=layoutInflater.inflate(R.layout.item_data_video_admin,parent,false);
        return new VideoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        VideoModel videoModel=list_video.get(position);
        holder.label_judul.setText(videoModel.getJudul());
        String link=context.getResources().getString(R.string.ipgeneral)+"video";
        Uri uri=Uri.parse(link+videoModel.getUri());
        simpleExoPlayer = new SimpleExoPlayer.Builder(context.getApplicationContext()).build();
        holder.playerView.setPlayer(simpleExoPlayer);
        holder.item_jml_view.setText(String.valueOf(videoModel.getView()));
        holder.item_jml_love.setText(String.valueOf(videoModel.getLove()));
        holder.item_jml_komen.setText(String.valueOf(videoModel.getKomen()));
        MediaItem mediaItem = MediaItem.fromUri(uri);
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.prepare();

        if(videoModel.isSelected()){
            holder.image_delete.setVisibility(View.VISIBLE);
        }
        else{
            holder.image_delete.setVisibility(View.GONE);
        }

        holder.layout.setOnLongClickListener(v->{
            if(videoModel.isSelected()){
                holder.image_delete.setVisibility(View.GONE);
                videoModel.setSelected(false);
                if(getSelectList().size()==0){
                    eventData.onGetDataAction(false,0);
                }
            }else{
                holder.image_delete.setVisibility(View.VISIBLE);
                videoModel.setSelected(true);
                int x=getSelectList().size();
                eventData.onGetDataAction(true,x);
            }
            return true;
        });




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

    public List<VideoModel> getSelectList(){
        List<VideoModel> list_new=new ArrayList<>();
        for(VideoModel videoModel:list_video){
            if(videoModel.isSelected()){
                list_new.add(videoModel);
            }
        }
        return list_new;
    }


    @Override
    public int getItemCount() {
        return list_video.size();
    }
}
