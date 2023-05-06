package com.it015.mediacovidapp.activity.admin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.activity.DetailVideoUser;
import com.it015.mediacovidapp.activity.KomentarUser;
import com.it015.mediacovidapp.activity.SheetView;
import com.it015.mediacovidapp.adapter.admin.PenontonAdapter;
import com.it015.mediacovidapp.model.admin.PenontonModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailVideo extends AppCompatActivity {
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    TextView label_love,label_komen,label_view,jumlah_penonton;

    ImageView image_komentar,image_view;
    BottomSheetDialog bottomSheetDialog;
    String penonton=null;

    PenontonAdapter penontonAdapter;

    int id_video;

    String URL_DATA_PENONTON="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video_detail);

        URL_DATA_PENONTON=getString(R.string.ipadress)+"app/detail-penonton";

        playerView=findViewById(R.id.detail_media_player);
        label_love=findViewById(R.id.detail_jumlah_love);
        label_komen=findViewById(R.id.detail_jumlah_komen);
        label_view=findViewById(R.id.detail_jumlah_view);
        image_komentar=findViewById(R.id.detail_image_koment);
        image_view=findViewById(R.id.detail_image_view);
        loadVideo();

        image_komentar.setOnClickListener(v->{
            gotoDetail();
        });

        image_view.setOnClickListener(v->{

            openSheetDialog();
        });
    }

    private void openSheetDialog() {
              Dialog dialog=new BottomSheetDialog(this);
              View view=LayoutInflater.from(this).inflate(R.layout.bottom_sheet_view,(ViewGroup) findViewById(R.id.container_bottom),false);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(view);
                loadDataPenonton(id_video,view);
                dialog.show();
    }

    private void gotoDetail() {
        Intent intent=new Intent(DetailVideo.this, KomentarUser.class);
        Bundle bundle_new=new Bundle();
        bundle_new.putInt("id_video",getIntent().getIntExtra("id_video",0));
        bundle_new.putInt("suka",getIntent().getIntExtra("suka",0));
        bundle_new.putInt("komen",getIntent().getIntExtra("komen",0));
        bundle_new.putInt("lihat",getIntent().getIntExtra("lihat",0));
        bundle_new.putString("uri",getIntent().getStringExtra("uri"));
        intent.putExtras(bundle_new);
        startActivity(intent);
        simpleExoPlayer.stop();
        playerView.onPause();
        finish();
    }

    private void loadVideo() {
        simpleExoPlayer=new SimpleExoPlayer.Builder(getApplicationContext()).build();
        playerView.setPlayer(simpleExoPlayer);
        String link=getResources().getString(R.string.ipgeneral)+"video/";
        String path=getIntent().getStringExtra("uri");
        Uri uri=Uri.parse(link+path);
        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        MediaItem mediaItem = MediaItem.fromUri(uri);
        id_video=(getIntent().getIntExtra("id_video",0));
        label_komen.setText(String.valueOf(getIntent().getIntExtra("komen",0)));
        label_love.setText(String.valueOf(getIntent().getIntExtra("suka",0)));
        label_view.setText(String.valueOf(getIntent().getIntExtra("lihat",0)));
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.prepare();
        simpleExoPlayer.play();
    }

    private void loadDataPenonton(int id_video, View view) {
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DetailVideo.this,Video.class));
        playerView.onPause();
        simpleExoPlayer.stop();
        finish();
    }
}
