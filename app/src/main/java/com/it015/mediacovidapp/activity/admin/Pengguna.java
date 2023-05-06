package com.it015.mediacovidapp.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.gson.JsonArray;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.activity.Home;
import com.it015.mediacovidapp.activity.Login;
import com.it015.mediacovidapp.adapter.admin.PenggunaAdapter;
import com.it015.mediacovidapp.adapter.admin.VideoAdapter;
import com.it015.mediacovidapp.model.admin.PenggunaModel;
import com.it015.mediacovidapp.model.admin.VideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pengguna extends AppCompatActivity {
    List<PenggunaModel> list_pengguna;
    PenggunaAdapter penggunaAdapter;
    RecyclerView recyclerView;
    String URL_DATA_PENGGUNA="http://192.168.43.174:8000/api/app/data-pengguna";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_pengguna_admin);

        URL_DATA_PENGGUNA=getResources().getString(R.string.ipadress)+"app/data-pengguna";


        recyclerView=findViewById(R.id.rcy_pengguna);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadPengguna();
    }

    private void loadPengguna() {
        Cache cahce=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cahce,network);
        requestQueue.start();
        list_pengguna=new ArrayList<>();
        @SuppressLint("NotifyDataSetChanged")
        StringRequest stringRequest=new StringRequest(Request.Method.GET,URL_DATA_PENGGUNA,
                response -> {
                    try {
                        JSONArray jsonArray=new JSONArray(response);
                        if(jsonArray.length()>0){
                            int x=jsonArray.length();
                            for(int i=0;i<x;i++){
                                PenggunaModel penggunaModel=new PenggunaModel();
                                JSONObject jsonObject=jsonArray.getJSONObject(i);

                                penggunaModel.setId_pengguna(jsonObject.getString("id"));
                                penggunaModel.setNama_lengkap(jsonObject.getString("nama_lengkap"));
                                penggunaModel.setAlamat(jsonObject.getString("alamat"));
                                penggunaModel.setNomor_handphone(jsonObject.getString("nomor_telepon"));
                                penggunaModel.setFoto_profil(jsonObject.getString("foto_profil"));
                                penggunaModel.setUsername(jsonObject.getString("username"));
                                penggunaModel.setTglBuat(jsonObject.getString("tgl_buat"));
                                list_pengguna.add(penggunaModel);
                                android.util.Log.d("data",list_pengguna.toString());
                            }
                            penggunaAdapter=new PenggunaAdapter(list_pengguna,this);
                            recyclerView.setAdapter(penggunaAdapter);
                        }
                    } catch (JSONException e) {
                       Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(this,error.toString(),Toast.LENGTH_LONG).show();
                }
        );
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Pengguna.this,Dashboard.class));
    }
}
