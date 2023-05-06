package com.it015.mediacovidapp.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.adapter.admin.DashboardAdapter;
import com.it015.mediacovidapp.adapter.admin.VideoAdapter;
import com.it015.mediacovidapp.model.admin.VideoModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity {
    List<VideoModel> list_video;
    DashboardAdapter dashboardAdapter;
    RecyclerView recyclerView;
    TextView btn_view_all;
    LinearLayout card_video,card_pengguna;
    LinearLayoutManager linearLayoutManager;
    BottomNavigationView bottomNavigationView;
    String URL_DATA_VIDEO="http://192.168.43.174:8000/api/app/data-video";
    String URL_PROFIL_PENGGUNA="http://192.168.43.174:8000/api/app/profil-admin";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_admin);

        URL_DATA_VIDEO=getResources().getString(R.string.ipadress)+"app/data-video";
        URL_PROFIL_PENGGUNA=getResources().getString(R.string.ipadress)+"app/profil-admin";

       sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView=findViewById(R.id.rcy_video_dashboard);
        btn_view_all=findViewById(R.id.btn_view_all);
        card_video=findViewById(R.id.card_video);
        card_pengguna=findViewById(R.id.card_pengguna);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.profil){
                    startActivity(new Intent(Dashboard.this,ProfilAdmin.class));
                    return true;
                }

                return false;
            }
        });
        loadVideo();


        String id_pengguna=sharedPreferences.getString("id_pengguna","");
        loadProfil(id_pengguna);

        btn_view_all.setOnClickListener(v->{
            Intent intent=new Intent(Dashboard.this,Video.class);
            startActivity(intent);
            finish();
        });

        card_video.setOnClickListener(v->{
            Intent intent=new Intent(Dashboard.this,Video.class);
            startActivity(intent);
            finish();
        });

        card_pengguna.setOnClickListener(v->{
            Intent intent=new Intent(Dashboard.this,Pengguna.class);
            startActivity(intent);
            finish();
        });
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
                                String url=getResources().getString(R.string.ipgeneral)+"akun/"+jsonObject.getString("foto_profil");
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("nama_lengkap",jsonObject.getString("nama_lengkap"));
                                editor.apply();;
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
    private void loadVideo() {
        list_video=new ArrayList<VideoModel>();
        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.GET,URL_DATA_VIDEO,
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
                            videoModel.setView(jsonObject.getInt("lihat"));
                            list_video.add(videoModel);
                        }
                        dashboardAdapter =new DashboardAdapter(list_video,this);
                        recyclerView.setAdapter(dashboardAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(),"Error "+error.getMessage(),Toast.LENGTH_LONG).show();
                }
        );
        requestQueue.add(stringRequest);

    }
}
