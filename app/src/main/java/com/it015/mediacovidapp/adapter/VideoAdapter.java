package com.it015.mediacovidapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
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
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.activity.DetailVideoUser;
import com.it015.mediacovidapp.activity.admin.DetailVideo;
import com.it015.mediacovidapp.holder.VideoHolder;
import com.it015.mediacovidapp.model.VideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {

    Context context;
    List<VideoModel> list_video;
    LayoutInflater layoutInflater;
    SimpleExoPlayer simpleExoPlayer;
    String CEK_VIDEO_LOVE="";
    String ADD_VIDEO_VIEW="";


    public VideoAdapter(Context context, List<VideoModel> list_video) {
        this.context = context;

        this.list_video = list_video;
        this.layoutInflater=LayoutInflater.from(context);
        CEK_VIDEO_LOVE=context.getResources().getString(R.string.ipadress)+"app/cek-reaksi-love";
        ADD_VIDEO_VIEW=context.getResources().getString(R.string.ipadress)+"app/feedback-video";

    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=layoutInflater.inflate(R.layout.item_video,parent,false);
        return new VideoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        VideoModel videoModel=list_video.get(position);
        String format_tgl=videoModel.getFormat()+" oleh admin";
        String jumlah_suka;
        String jumlah_nonton;
        if(videoModel.getView()==0){
            jumlah_nonton="Belum ada yang menonton";
        }else{
            jumlah_nonton=String.valueOf(videoModel.getView())+" orang yang menonton";
        }
        if(videoModel.getLove()==0){
            jumlah_suka="Belum ada yang menyukai";
        }else{
            jumlah_suka=String.valueOf(videoModel.getView())+" orang yang menyukai";
        }
        holder.jumlah_view.setText(jumlah_nonton);
        holder.jumlah_love.setText(jumlah_suka);
        holder.format_tgl.setText(format_tgl);
        holder.judul_video.setText(videoModel.getJudul());
        String link=context.getResources().getString(R.string.ipgeneral)+"video/";
        Uri uri=Uri.parse(link+videoModel.getUri());
        simpleExoPlayer = new SimpleExoPlayer.Builder(context.getApplicationContext()).build();
        holder.playerView.setPlayer(simpleExoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.seekTo(0);
        simpleExoPlayer.prepare();
        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Player.Listener.super.onPlayerStateChanged(playWhenReady, playbackState);
                if(simpleExoPlayer.getPlaybackState()==SimpleExoPlayer.STATE_BUFFERING){
                    holder.loading.setVisibility(View.VISIBLE);
                }
                else{
                    holder.loading.setVisibility(View.GONE);
                }
                if(simpleExoPlayer.getPlaybackState()==SimpleExoPlayer.STATE_ENDED){
                    simpleExoPlayer.stop();
                    holder.playerView.onPause();
                    simpleExoPlayer.release();
                }
            }
        });
        holder.playerView.setOnClickListener(v->{
            Intent intent=new Intent(context.getApplicationContext(), DetailVideoUser.class);
            intent.putExtra("link_video",link+videoModel.getUri());
            intent.putExtra("suka",videoModel.getLove());
            intent.putExtra("nonton",videoModel.getView());
            intent.putExtra("id_video",videoModel.getId());
            SharedPreferences sharedPreferences= context.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
            cekViewVideo(videoModel.getId(),sharedPreferences.getString("id_pengguna",""),intent);
        });
    }

    public VideoAdapter() {
    }

    private void cekViewVideo(int id, String id_pengguna, Intent intent) {
        Cache cache=new DiskBasedCache(context.getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest=
                new StringRequest(Request.Method.GET,CEK_VIDEO_LOVE+"/"+id+"/"+id_pengguna,
                        response -> {

                            try {
                                JSONArray jsonArray=new JSONArray(response);
                                if(jsonArray.length()==0){
                                    feedBackVideo(id,id_pengguna,intent);
                                }
                                else{
                                    context.startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            Toast.makeText(context,"Kesalahan pada "+error.toString(),Toast.LENGTH_LONG).show();
                        }
                );
        requestQueue.add(stringRequest);
    }

    private void feedBackVideo(int id, String id_pengguna, Intent intent) {
        Cache cache=new DiskBasedCache(context.getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest=
                new StringRequest(Request.Method.POST,ADD_VIDEO_VIEW,
                        response -> {

                        },
                        error -> {
                            Toast.makeText(context,"Kesalahan Pada "+error.toString(),Toast.LENGTH_LONG).show();
                        }
                ){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<>();
                        params.put("id_pengguna",id_pengguna);
                        params.put("id_video",String.valueOf(id));
                        return params;
                    }
                };
        requestQueue.add(stringRequest);
        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return list_video.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<VideoModel> list_search) {
        list_video=list_search;
        notifyDataSetChanged();
    }
}
