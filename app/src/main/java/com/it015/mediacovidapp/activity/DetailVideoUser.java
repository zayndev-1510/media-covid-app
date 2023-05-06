package com.it015.mediacovidapp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.adapter.admin.PenontonAdapter;
import com.it015.mediacovidapp.model.admin.PenontonModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailVideoUser extends AppCompatActivity {
    TextView jumlah_suka,jumlah_nonton,jumlah_komentar,jumlah_penonton;
    PlayerView playerView;
    ExoPlayer exoPlayer;
    ProgressBar loading_video;
    ImageView image_love;
    int temp=0;

    String penonton=null;

    PenontonAdapter penontonAdapter;

    int id_video=0;

    String CEK_VIDEO_LOVE="";
    String BERI_LOVE="";
    String URL_DEETAIL_VIDEO="";
    String URL_DATA_PENONTON="";

    int suka=0;
    private int resumeWindow;
    private long resumePosition;
    ImageView image_komentar,detail_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_video_pengguna);

        CEK_VIDEO_LOVE=getResources().getString(R.string.ipadress)+"app/cek-reaksi-love";
        BERI_LOVE=getResources().getString(R.string.ipadress)+"app/feedback-love";
        URL_DEETAIL_VIDEO=getResources().getString(R.string.ipadress)+"app/detail-video-user";
        URL_DATA_PENONTON=getString(R.string.ipadress)+"app/detail-penonton";
        jumlah_suka=findViewById(R.id.detail_jumlah_love_pengguna);
        jumlah_nonton=findViewById(R.id.detail_jumlah_view_pengguna);
        jumlah_komentar=findViewById(R.id.detail_jumlah_komentar_pengguna);
        playerView=findViewById(R.id.detail_media_player_pengguna);
        loading_video=findViewById(R.id.loading_video_pengguna);
        image_love=findViewById(R.id.image_love_pengguna);
        image_komentar=findViewById(R.id.detail_image_komentar);
        detail_view=findViewById(R.id.detail_image_view);

        Uri uri=Uri.parse(getIntent().getStringExtra("link_video"));
        exoPlayer=new ExoPlayer.Builder(getApplicationContext()).setRenderersFactory(new DefaultRenderersFactory(getApplicationContext())
                        .setEnableAudioTrackPlaybackParams(true)
                        .setEnableAudioOffload(true)
                        .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
        ).build();


        boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
           exoPlayer.seekTo(resumeWindow, resumePosition);
        }
        playerView.setPlayer(exoPlayer);
         MediaItem mediaItem = MediaItem.fromUri(uri);
        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.addListener(new Player.Listener() {
            @Override

            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Player.Listener.super.onPlayerStateChanged(playWhenReady, playbackState);
                if(exoPlayer.getPlaybackState()==ExoPlayer.STATE_BUFFERING){
                    loading_video.setVisibility(View.VISIBLE);
                }
                else{
                    loading_video.setVisibility(View.GONE);
                }
                if(exoPlayer.getPlaybackState()==ExoPlayer.STATE_ENDED){
                    playerView.onPause();
                    exoPlayer.stop();
                    exoPlayer.release();
                }


            }

            @Override
            public void onPlayerError(@NonNull PlaybackException error) {
                Player.Listener.super.onPlayerError(error);
            }

            @Override
            public void onPositionDiscontinuity(@NonNull Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {
                Player.Listener.super.onPositionDiscontinuity(oldPosition, newPosition, reason);
                updateResumePosition();
            }
        });
        suka=getIntent().getIntExtra("suka",0);
        SharedPreferences sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);
        String id_pengguna=sharedPreferences.getString("id_pengguna","");
        id_video=getIntent().getIntExtra("id_video",0);

        cekStatusLove(id_pengguna,id_video);
        loadDetailVideo(id_video);

        detail_view.setOnClickListener(v->{
            openBottomSheet();
        });
        image_love.setOnClickListener(v -> {
                if(temp==0){
                    image_love.setImageResource(R.drawable.ic_baseline_favorite_24_red);
                    temp=1;
                    suka++;
                }else{
                    image_love.setImageResource(R.drawable.ic_baseline_favorite_24);
                    temp=0;
                    suka--;
                }

                FeebackLove(id_pengguna,id_video,temp);

                jumlah_suka.setText(String.valueOf(suka));

        });

        image_komentar.setOnClickListener(v->{
            playerView.onPause();
            Intent intent=new Intent(DetailVideoUser.this,KomentarUser.class);
            Bundle bundle=new Bundle();
            bundle.putInt("aksi",1);
            bundle.putInt("id_video",id_video);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        });
    }

    private void openBottomSheet() {
        Dialog dialog=new BottomSheetDialog(this);
        View view=LayoutInflater.from(this).inflate(R.layout.bottom_sheet_view,(ViewGroup) findViewById(R.id.container_bottom),false);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        loadPenonton(id_video,view);
        dialog.show();
    }

    private void loadPenonton(int id_video, View view) {
        List<PenontonModel> data_penonton=new ArrayList<>();
        RecyclerView recyclerView=view.findViewById(R.id.rcy_view);
        jumlah_penonton=view.findViewById(R.id.jumlah_penonton);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();

        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest=
                new StringRequest(Request.Method.POST,URL_DATA_PENONTON,
                        response -> {
                            try {
                                JSONArray jsonArray=new JSONArray(response);
                                if(jsonArray.length()>0){
                                    penonton=jsonArray.length()+" pengguna telah menonton video ini";
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                                        PenontonModel penontonModel =new PenontonModel();
                                        penontonModel.setFoto_profil(jsonObject.getString("foto_profil"));
                                        penontonModel.setId_pengguna(jsonObject.getString("id"));
                                        penontonModel.setNama_lengkap(jsonObject.getString("nama_lengkap"));
                                        data_penonton.add(penontonModel);
                                    }
                                }
                                else{
                                    penonton="Belum ada yang menonton";
                                }
                                jumlah_penonton.setText(penonton);
                                penontonAdapter=new PenontonAdapter(this,data_penonton);
                                recyclerView.setAdapter(penontonAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            Toast.makeText(this,error.toString(),Toast.LENGTH_LONG).show();
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<>();
                        params.put("id_video",String.valueOf(id_video));
                        return params;
                    }
                };
        requestQueue.add(stringRequest);
    }

    void loadDetailVideo(int id_video) {

        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        StringRequest stringRequest=
                new StringRequest(Request.Method.POST, URL_DEETAIL_VIDEO,
                        response -> {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                int [] reaction={jsonObject.getInt("love"),jsonObject.getInt("komen"),jsonObject.getInt("lihat")};
                                jumlah_suka.setText(String.valueOf(reaction[0]));
                                jumlah_komentar.setText(String.valueOf(reaction[1]));
                                jumlah_nonton.setText(String.valueOf(reaction[2]));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            Toast.makeText(this,"Ada kesalahan pada  "+error.toString(),Toast.LENGTH_LONG).show();
                        }
                ){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<>();
                        params.put("id_video",String.valueOf(id_video));
                        return params;
                    }
                };
        requestQueue.add(stringRequest);

    }

    private void FeebackLove(String id_pengguna, int id_video, int love) {

        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        StringRequest stringRequest=
                new StringRequest(Request.Method.POST,BERI_LOVE,
                        response -> {

                        },
                        error -> {
                            Toast.makeText(this,"Ada Kesalahan pada "+error.toString(),Toast.LENGTH_LONG).show();
                        }
                ){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<>();
                        params.put("id_pengguna",id_pengguna);
                        params.put("id_video",String.valueOf(id_video));
                        params.put("love",String.valueOf(love));
                        return params;
                    }
                };
        requestQueue.add(stringRequest);
    }

    private void cekStatusLove(String id_pengguna, int id_video) {
        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
       StringRequest stringRequest=
                new StringRequest(Request.Method.GET,CEK_VIDEO_LOVE+"/"+id_video+"/"+id_pengguna,
                response -> {
                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        if(jsonArray.length()>0){
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            int love=jsonObject.getInt("love");
                            if(love==0){
                                image_love.setImageResource(R.drawable.ic_baseline_favorite_24);
                            }
                            else{
                                image_love.setImageResource(R.drawable.ic_baseline_favorite_24_red);
                            }

                            temp=love;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this,"ada kesalahan pada "+error.toString(),Toast.LENGTH_LONG).show();
                }

        );
        requestQueue.add(stringRequest);
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    private void updateResumePosition() {
        resumeWindow = exoPlayer.getCurrentWindowIndex();
        resumePosition = exoPlayer.isCurrentWindowSeekable() ? Math.max(0, exoPlayer.getCurrentPosition())
                : C.TIME_UNSET;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerView.onPause();
        exoPlayer.stop();
        exoPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerView.onPause();
        exoPlayer.stop();
        exoPlayer.release();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DetailVideoUser.this,Home.class));
        playerView.onPause();
        exoPlayer.stop();
        exoPlayer.release();
    }

}
