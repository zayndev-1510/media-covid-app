package com.it015.mediacovidapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
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
import com.it015.mediacovidapp.activity.admin.Dashboard;
import com.it015.mediacovidapp.widget.Loading_bar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText username,sandi;
    TextView btn_masuk;
    String URL_LOGIN_APP="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        URL_LOGIN_APP=getString(R.string.ipadress)+"app/login-akun";
        username=findViewById(R.id.username);
        sandi=findViewById(R.id.sandi);
        btn_masuk=findViewById(R.id.btn_masuk);

        btn_masuk.setOnClickListener(v-> loginAkun());
    }
    private void loginAkun() {
        Boolean seleksi_inputan=seleksiInputan();
        if(seleksi_inputan){
            requestLogin();
        }
    }

    private void requestLogin() {
        Loading_bar loading_bar=new Loading_bar(this);
        Dialog dialog_upload=loading_bar.loadDialog("Sedang Memproses");
        dialog_upload.setCancelable(false);
        dialog_upload.setCanceledOnTouchOutside(false);
        dialog_upload.create();
        dialog_upload.show();
        Cache cahce=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cahce,network);
        requestQueue.start();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,URL_LOGIN_APP,
                response -> {
                    dialog_upload.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        int val=jsonObject.getInt("val");
                        if(val==1){
                            int lvl=jsonObject.getInt("lvl");
                            if(lvl==1){
                                Intent intent=new Intent(Login.this,Home.class);
                                Toast.makeText(this,"Login Berhasil",Toast.LENGTH_LONG).show();
                                SharedPreferences sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("id_pengguna",jsonObject.getString("id_pengguna"));

                                editor.apply();
                                startActivity(intent);
                                finish();
                            }
                            else if(lvl==2){
                                Intent intent=new Intent(Login.this, Dashboard.class);
                                Toast.makeText(this,"Login Berhasil",Toast.LENGTH_LONG).show();
                                SharedPreferences sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("id_pengguna",jsonObject.getString("id_pengguna"));
                                editor.apply();
                                startActivity(intent);
                                finish();
                            }

                        }
                        else if(val==2){
                            Toast.makeText(this,"Sandi masih salah",Toast.LENGTH_LONG).show();
                        }else if(val==0){
                            Toast.makeText(this,"Login Gagal",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(this,"Something error"+jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    dialog_upload.dismiss();
                    Toast.makeText(this,error.toString(),Toast.LENGTH_LONG).show();
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("username",username.getText().toString().trim());
                params.put("sandi",sandi.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private Boolean seleksiInputan() {
        Boolean cek_user=username.getText().toString().isEmpty();
        Boolean cek_sandi=sandi.getText().toString().isEmpty();
        if(cek_user && cek_sandi){
            Toast.makeText(this,"Opps Nama Pengguna Dan Sandi Belum Diisi",Toast.LENGTH_LONG).show();
        }
        else if(cek_user){
            Toast.makeText(this,"Opps Nama Pengguna Belum Diisi",Toast.LENGTH_LONG).show();
        }
        else if(cek_sandi){
            Toast.makeText(this,"Opps Sandi Belum Diisi",Toast.LENGTH_LONG).show();
        }
        else{
            return true;
        }
        return false;
    }
}
