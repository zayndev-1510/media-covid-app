package com.it015.mediacovidapp.activity.admin;

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
import com.it015.mediacovidapp.widget.Loading_bar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registasi extends AppCompatActivity {
    EditText edit_username,edit_sandi,edit_ulang_sandi;
    EditText edit_nama,edit_alamat,edit_nomor_handphone;
    TextView btn_daftar;
    String URL_DAFTAR_AKUN="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrasi_page);

        URL_DAFTAR_AKUN=getResources().getString(R.string.ipadress)+"app/daftar-akun";

        btn_daftar=findViewById(R.id.btn_daftar);
        edit_nama=findViewById(R.id.nama_lengkap);
        edit_username=findViewById(R.id.username);
        edit_sandi=findViewById(R.id.sandi);
        edit_ulang_sandi=findViewById(R.id.ulang_sandi);
        edit_nomor_handphone=findViewById(R.id.nomor_handphone);
        edit_alamat=findViewById(R.id.alamat);
        btn_daftar=findViewById(R.id.btn_daftar);
        btn_daftar.setOnClickListener(v->{
            daftarAkun();
        });
    }

    private void daftarAkun() {

       SelecksiInputan();
    }

    private void SelecksiInputan() {
        Boolean cek_username=edit_username.getText().toString().isEmpty();
        Boolean cek_nama=edit_nama.getText().toString().isEmpty();
        Boolean cek_sandi=edit_sandi.getText().toString().isEmpty();
        Boolean cek_ulang_sandi=edit_ulang_sandi.getText().toString().isEmpty();
        Boolean cek_nomor_hp=edit_nomor_handphone.getText().toString().isEmpty();
        Boolean cek_alamat=edit_alamat.getText().toString().isEmpty();
        if(cek_nama && cek_alamat && cek_nomor_hp && cek_username && cek_sandi && cek_ulang_sandi){
            Toast.makeText(this,"Opps nama lengkap belum diisi !",Toast.LENGTH_LONG).show();
        }else if(cek_nama){
            Toast.makeText(this,"Opps nama lengkap belum diisi",Toast.LENGTH_LONG).show();
        }else if(cek_alamat){
            Toast.makeText(this,"Opps alamat belum diisi",Toast.LENGTH_LONG).show();
        }else if(cek_nomor_hp){
            Toast.makeText(this,"Opps nomor handphone belum diisi",Toast.LENGTH_LONG).show();
        }else if(cek_username){
            Toast.makeText(this,"Opps nama pengguna belum diisi",Toast.LENGTH_LONG).show();
        }
        else if(cek_sandi){
            Toast.makeText(this,"Opps Sandi Belum Diisi",Toast.LENGTH_LONG).show();
        }
        else if(cek_ulang_sandi){
            Toast.makeText(this,"Opps ulang sandi belum diisi",Toast.LENGTH_LONG).show();
        }
        else{
            int limit=edit_nomor_handphone.getText().length();
            if(limit>12){
                Toast.makeText(this,"Opps handphone tidak boleh lebih dari 12",Toast.LENGTH_LONG).show();
            }
            else{
                requestDaftarAkun();
            }
        }
    }

    private void requestDaftarAkun() {
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
        StringRequest stringRequest=new StringRequest(Request.Method.POST,URL_DAFTAR_AKUN,
                response -> {
                    dialog_upload.dismiss();
                    JSONObject jsonObject= null;
                    try {
                        jsonObject = new JSONObject(response);
                        int val=jsonObject.getInt("val");
                        if(val==1){
                            Intent intent=new Intent(Registasi.this,MessageRegistrasi.class);
                            intent.putExtra("id_pengguna",jsonObject.getString("id"));
                            SharedPreferences sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("id_pengguna",jsonObject.getString("id"));
                            editor.apply();
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(this,"Ada kesalahan diserver",Toast.LENGTH_LONG).show();
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
                Map<String,String> params=new HashMap<>();
                params.put("nama_lengkap",edit_nama.getText().toString());
                params.put("alamat",edit_alamat.getText().toString());
                params.put("username",edit_username.getText().toString());
                params.put("sandi",edit_sandi.getText().toString());
                params.put("nomor_telepon",edit_nomor_handphone.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


}
