package com.it015.mediacovidapp.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.adapter.admin.VideoAdapter;
import com.it015.mediacovidapp.adapter.user.KategoriAdapterUser;
import com.it015.mediacovidapp.holder.admin.EventData;
import com.it015.mediacovidapp.model.admin.KategoriModel;
import com.it015.mediacovidapp.model.admin.VideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Video extends AppCompatActivity implements KategoriAdapterUser.MyCallback,EventData {
    Button btn_tambah;
    List<VideoModel> list_video;
    List<KategoriModel> list_kategori;
    RecyclerView rcy_video,recyleview_kategori;
    VideoAdapter videoAdapter;

    TextView hapus_video,jumlah_check;

    ImageView close_selected;



    String URL_DATA_VIDEO="";
    String URL_DATA_KATEGORI="";

    LinearLayout layoutheader;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearkategori;
    KategoriAdapterUser kategoriAdapterUser;
    List<VideoModel> list_insert=new ArrayList<>();


    StringBuilder stringBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_video_admin);

        URL_DATA_VIDEO=getResources().getString(R.string.ipadress)+"app/data-video-user";
        URL_DATA_KATEGORI=getResources().getString(R.string.ipadress)+"app/data-kategori";

        btn_tambah=findViewById(R.id.tambah_video);
        rcy_video=findViewById(R.id.rcy_video);
        layoutheader=findViewById(R.id.layout_header);
        hapus_video=findViewById(R.id.hapus_video);
        jumlah_check=findViewById(R.id.selected_label);
        close_selected=findViewById(R.id.close_selected);

        recyleview_kategori=findViewById(R.id.kategori_recyle_admin);
        linearkategori=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyleview_kategori.setHasFixedSize(true);
        recyleview_kategori.setLayoutManager(linearkategori);

        linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rcy_video.setLayoutManager(linearLayoutManager);

        loadVideo();
        loadKategori();

        btn_tambah.setOnClickListener(v->{
            startActivity(new Intent(this,AddVideo.class));
        });

        hapus_video.setOnClickListener(v->{
                list_insert=videoAdapter.getSelectList();
                JsonArray jsonArray=new JsonArray();
                for(int i=0;i<list_insert.size();i++){
                    
                    jsonArray.add(list_insert.get(i).getId());
                }
            deleteVideo(jsonArray);
        });

        close_selected.setOnClickListener(v->{
            layoutheader.setVisibility(View.GONE);
            btn_tambah.setVisibility(View.VISIBLE);
            hapus_video.setVisibility(View.GONE);
            loadVideo();
        });
    }

    private void deleteVideo(JsonArray jsonArray) {

        String URL_HAPUS_VIDEO=getString(R.string.ipadress)+"app/delete-video";
        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest=
                new StringRequest(Request.Method.POST,URL_HAPUS_VIDEO,
                        response -> {
                            try {

                                JSONObject jsonObject=new JSONObject(response);
                                int val=jsonObject.getInt("val");
                                if(val>0){
                                    Toast.makeText(this, "hapus video berhasil", Toast.LENGTH_SHORT).show();
                                    loadVideo();
                                    layoutheader.setVisibility(View.GONE);
                                    btn_tambah.setVisibility(View.VISIBLE);
                                    hapus_video.setVisibility(View.GONE);
                                }
                                else{
                                    Toast.makeText(this,"hapus video gagal",Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            Toast.makeText(this,"Error "+error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<>();
                        params.put("data",jsonArray.toString());
                        return params;
                    }
                };
        requestQueue.add(stringRequest);
    }

    private void loadKategori() {
        list_kategori=new ArrayList<>();
        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest=
                new StringRequest(Request.Method.GET,URL_DATA_KATEGORI,
                        response -> {
                            JSONArray jsonArray= null;
                            try {
                                jsonArray = new JSONArray(response);
                                if(jsonArray.length()>0){
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                                        KategoriModel kategoriModel=new KategoriModel();
                                        kategoriModel.setId(jsonObject.getInt("id"));
                                        kategoriModel.setNama_kategori(jsonObject.getString("nama_kategori"));
                                        list_kategori.add(kategoriModel);
                                    }
                                }



                                kategoriAdapterUser=new KategoriAdapterUser(this,list_kategori,this);
                                recyleview_kategori.setAdapter(kategoriAdapterUser);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        },
                        error -> {
                        }
                );
        requestQueue.add(stringRequest);
    }

    private void loadVideo() {
        list_video=new ArrayList<VideoModel>();
        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();

        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest=
                new StringRequest(Request.Method.POST,URL_DATA_VIDEO,
                response -> {
                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            VideoModel videoModel=new VideoModel();
                            videoModel.setId(jsonObject.getInt("id"));
                            videoModel.setUri(jsonObject.getString("video"));
                            videoModel.setJudul(jsonObject.getString("judul_video"));
                            videoModel.setAuthor(jsonObject.getString("author"));
                            videoModel.setKomen(jsonObject.getInt("komen"));
                            videoModel.setLove(jsonObject.getInt("love"));
                            videoModel.setSelected(jsonObject.getBoolean("selected"));
                            videoModel.setView(jsonObject.getInt("lihat"));
                            list_video.add(videoModel);
                        }
                        videoAdapter=new VideoAdapter(this, list_video,this);
                        rcy_video.setAdapter(videoAdapter);
                        videoAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(),"Error "+error.getMessage(),Toast.LENGTH_LONG).show();
                }
                ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("id_kategori",String.valueOf(0));
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Video.this,Dashboard.class));
    }

    @Override
    public void loadData(JSONArray jsonArray) {
        list_video=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            try {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                VideoModel videoModel=new VideoModel();
                videoModel.setId(jsonObject.getInt("id"));
                videoModel.setUri(jsonObject.getString("video"));
                videoModel.setJudul(jsonObject.getString("judul_video"));
                videoModel.setAuthor(jsonObject.getString("author"));
                videoModel.setKomen(jsonObject.getInt("komen"));
                videoModel.setLove(jsonObject.getInt("love"));
                videoModel.setView(jsonObject.getInt("lihat"));
                list_video.add(videoModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        videoAdapter=new VideoAdapter(this,list_video,this);
        rcy_video.setAdapter(videoAdapter);
        videoAdapter.notifyDataSetChanged();
    }


    @Override
    public void onGetDataAction(Boolean selected,int a) {
        if(selected){
            layoutheader.setVisibility(View.VISIBLE);
            btn_tambah.setVisibility(View.GONE);
            hapus_video.setVisibility(View.VISIBLE);
            String h=a+" video yg dipilih";
            jumlah_check.setText(h);
        }else{
            layoutheader.setVisibility(View.GONE);
            btn_tambah.setVisibility(View.VISIBLE);
            hapus_video.setVisibility(View.GONE);
        }
    }
}
