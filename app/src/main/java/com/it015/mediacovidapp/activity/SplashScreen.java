package com.it015.mediacovidapp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
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
import com.it015.mediacovidapp.activity.admin.Dashboard;
import com.it015.mediacovidapp.widget.Loading_bar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    TextView app_name;
    String URL_CEK_AKUN="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screnn);

        URL_CEK_AKUN=getResources().getString(R.string.ipadress)+"app/cek-akun";
        lottieAnimationView=findViewById(R.id.lottie_animation);
        app_name=findViewById(R.id.judul_app);

        app_name.animate().translationY(-700).setDuration(2700).setStartDelay(0);
        lottieAnimationView.playAnimation();

        SharedPreferences sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               if(sharedPreferences.contains("id_pengguna")){
                   String id_pengguna=sharedPreferences.getString("id_pengguna","");
                   requestLogin(id_pengguna);
               }
               else{
                   startActivity(new Intent(SplashScreen.this,Welcome.class));
               }
            }
        },5000);

    }
    private void requestLogin(String id_pengguna) {
        Cache cahce=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cahce,network);
        requestQueue.start();

        StringRequest stringRequest=new StringRequest(Request.Method.POST,URL_CEK_AKUN,
                response -> {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String data=jsonObject.getString("data");
                        JSONObject jsonew=new JSONObject(data);
                        int lvl=jsonew.getInt("lvl");
                        if(lvl==1){
                            startActivity(new Intent(SplashScreen.this,Home.class));
                            finish();
                        }else if(lvl==2){
                            startActivity(new Intent(SplashScreen.this,Dashboard.class));
                            finish();
                        }
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
                Map<String,String> params=new HashMap<String,String>();
                params.put("id_pengguna",id_pengguna);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
