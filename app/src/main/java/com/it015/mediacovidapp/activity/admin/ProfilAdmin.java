package com.it015.mediacovidapp.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.activity.Home;
import com.it015.mediacovidapp.activity.ProfilUser;
import com.it015.mediacovidapp.activity.Welcome;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilAdmin extends AppCompatActivity {
    TextView nama_lengkap,alamat,noper,username,keluar_akun;
    ImageView imageView;
    String URL_PROFIL_PENGGUNA="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_admin);

        URL_PROFIL_PENGGUNA=getResources().getString(R.string.ipadress)+"app/profil-admin";

        SharedPreferences sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);
        username=findViewById(R.id.username_admin);
        noper=findViewById(R.id.nomor_handphone_admin);
        alamat=findViewById(R.id.alamat_admin);
        imageView=findViewById(R.id.image_admin);
        nama_lengkap=findViewById(R.id.nama_lengkap_admin);
        keluar_akun=findViewById(R.id.keluar_akun);
        keluar_akun.setOnClickListener(v->{
            SharedPreferences.Editor edit=sharedPreferences.edit();
            edit.clear();
            edit.apply();
            startActivity(new Intent(ProfilAdmin.this, Welcome.class));
        });

        String id_pengguna=sharedPreferences.getString("id_pengguna","");
        loadProfil(id_pengguna);
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

                                username.setText(jsonObject.getString("username"));
                                noper.setText(jsonObject.getString("nomor_telepon"));
                                nama_lengkap.setText(jsonObject.getString("nama_lengkap"));
                                alamat.setText(jsonObject.getString("alamat"));
                                Picasso.get().load(url).into(imageView);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfilAdmin.this, Dashboard.class));
    }
}
