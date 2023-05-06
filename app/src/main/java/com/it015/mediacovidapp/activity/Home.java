package com.it015.mediacovidapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.adapter.VideoAdapter;
import com.it015.mediacovidapp.adapter.user.KategoriAdapterUser;
import com.it015.mediacovidapp.model.VideoModel;
import com.it015.mediacovidapp.model.admin.KategoriModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity implements KategoriAdapterUser.MyCallback {
    List<VideoModel> list_video=new ArrayList<>();
    List<KategoriModel> list_kategori;

    VideoAdapter videoAdapter;
    KategoriAdapterUser kategoriAdapterUser;

    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearkategori;

    RecyclerView rcy_video;
    RecyclerView rcy_kategori;
    BottomNavigationView bottomNavigationView;

    String URL_DATA_VIDEO="";
    String URL_DATA_KATEGORI="";
    String URL_PROFIL_PENGGUNA="";
    int ID_KATEGORI=0;

    SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);

        URL_DATA_VIDEO=getResources().getString(R.string.ipadress)+"app/data-video-user";
        URL_DATA_KATEGORI=getResources().getString(R.string.ipadress)+"app/data-kategori";
        URL_PROFIL_PENGGUNA=getResources().getString(R.string.ipadress)+"app/profil-pengguna";

        rcy_video=findViewById(R.id.rcy_video_pengguna);
        rcy_kategori=findViewById(R.id.kategori_rcy);

        rcy_video.setHasFixedSize(true);
        rcy_kategori.setHasFixedSize(true);

        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcy_video.setLayoutManager(linearLayoutManager);

        linearkategori=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rcy_kategori.setLayoutManager(linearkategori);
        bottomNavigationView=findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.profil) {
                    startActivity(new Intent(Home.this, ProfilUser.class));
                    finish();
                }
                return false;
            }
        });
        String id_pengguna=sharedPreferences.getString("id_pengguna","");

        loadVideo();
        loadKategori();
        if(!sharedPreferences.contains("nama_lengkap")){
            loadProfil(id_pengguna);
        }
    }
    private void loadProfil(String id_pengguna) {
        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest=
                new StringRequest(Request.Method.POST,URL_PROFIL_PENGGUNA,
                        response -> {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("nama_lengkap",jsonObject.getString("nama_lengkap"));
                                editor.apply();
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
                        params.put("id_pengguna",id_pengguna);
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
                        rcy_kategori.setAdapter(kategoriAdapterUser);
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

        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest=
                new StringRequest(Request.Method.POST,URL_DATA_VIDEO,
                response -> {
                    JSONArray jsonArray= null;
                    try {
                        jsonArray = new JSONArray(response);
                        if(jsonArray.length()>0){
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                VideoModel videoModel=new VideoModel();
                                videoModel.setId(jsonObject.getInt("id"));
                                videoModel.setUri(jsonObject.getString("video"));
                                videoModel.setJudul(jsonObject.getString("judul_video"));
                                videoModel.setAuthor(jsonObject.getString("author"));
                                videoModel.setKomen(jsonObject.getInt("komen"));
                                videoModel.setLove(jsonObject.getInt("love"));
                                videoModel.setView(jsonObject.getInt("lihat"));
                                videoModel.setFormat(jsonObject.getString("tgl_format"));
                                videoModel.setId_kategori(jsonObject.getInt("id_kategori"));
                                list_video.add(videoModel);
                            }
                        }

                        videoAdapter=new VideoAdapter(this,list_video);
                        rcy_video.setAdapter(videoAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                }
                ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("id_kategori",String.valueOf(ID_KATEGORI));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @SuppressLint("NotifyDataSetChanged")
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
              videoModel.setFormat(jsonObject.getString("tgl_format"));
              videoModel.setId_kategori(jsonObject.getInt("id_kategori"));
              list_video.add(videoModel);
          } catch (JSONException e) {
              e.printStackTrace();
          }
      }
      videoAdapter=new VideoAdapter(this,list_video);
      rcy_video.setAdapter(videoAdapter);
      videoAdapter.notifyDataSetChanged();
    }
}
