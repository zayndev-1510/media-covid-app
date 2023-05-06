package com.it015.mediacovidapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.it015.mediacovidapp.activity.admin.DetailVideo;
import com.it015.mediacovidapp.adapter.user.KomentarAdapter;
import com.it015.mediacovidapp.model.data.KomentarData;
import com.it015.mediacovidapp.model.user.KomentarModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KomentarUser extends AppCompatActivity {
    List<KomentarModel> list_komentar;
    RecyclerView rcy_header;
    LinearLayoutManager linearLayoutManager;
    KomentarAdapter komentarAdapter;
    TextView jumlah_komentar;
    EditText editText;
    TextView btn_kirim;
    int id_komentar=0;
    int id_video=0;
    int aksi=0;
    boolean aksi_kirim=false;
    Bundle bundle;

    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;
    String URL_LOAD_KOMENTAR="";
    String URL_KIRIM_KOMENTAR="";
    String URL_KIRIM_REPLY_KOMENTAR="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kolom_komentar_user);

        bundle=getIntent().getExtras();
        aksi=bundle.getInt("aksi");
        id_video=bundle.getInt("id_video");

        URL_LOAD_KOMENTAR=getResources().getString(R.string.ipadress)+"app/data-komentar-user";
        URL_KIRIM_KOMENTAR=getResources().getString(R.string.ipadress)+"app/kirim-komentar";
        URL_KIRIM_REPLY_KOMENTAR=getResources().getString(R.string.ipadress)+"app/kirim-komentar-reply";


        jumlah_komentar=findViewById(R.id.jumlah_komentar_user);
        swipeRefreshLayout=findViewById(R.id.refresh_data);
        btn_kirim=findViewById(R.id.btn_kirim);
        sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);
        editText=findViewById(R.id.edit_komentar_user);
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcy_header=findViewById(R.id.rcyheader);
        rcy_header.setHasFixedSize(true);
        rcy_header.setLayoutManager(linearLayoutManager);
        btn_kirim.setOnClickListener(v->{
            kirimKomentar();
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadKomentar();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void kirimKomentar() {
        if(aksi_kirim){
            kirimSubKomentar();
        }else{
            kirimKomentarHeader();
        }

        loadKomentar();

    }

    private void kirimSubKomentar() {
        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest=
                new StringRequest(Request.Method.POST,URL_KIRIM_REPLY_KOMENTAR,
                        response -> {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getInt("val")>0){

                                    editText.getText().clear();
                                }else{
                                    Toast.makeText(this,"Komentar gagal dikirim",Toast.LENGTH_LONG).show();
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
                        Map<String,String> params=new HashMap<>();
                        params.put("id_video",String.valueOf(id_video));
                        params.put("komentar",editText.getText().toString().trim());
                        params.put("parent_id",String.valueOf(id_komentar));
                        params.put("nama_komentar",sharedPreferences.getString("nama_lengkap",""));
                        params.put("id_pengguna",sharedPreferences.getString("id_pengguna",""));
                        return params;
                    }
                };
        requestQueue.add(stringRequest);
    }

    private void kirimKomentarHeader() {
        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest=
                new StringRequest(Request.Method.POST,URL_KIRIM_KOMENTAR,
                        response -> {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getInt("val")>0){
                                    loadKomentar();
                                    editText.getText().clear();
                                }else{
                                    Toast.makeText(this,"Komentar gagal dikirim",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        },
                        error -> {
                            Toast.makeText(this,"Ada kesahalan",Toast.LENGTH_LONG).show();
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<>();
                        params.put("id_video",String.valueOf(id_video));
                        params.put("komentar",editText.getText().toString().trim());
                        params.put("nama_komentar",sharedPreferences.getString("nama_lengkap",""));
                        params.put("id_pengguna",sharedPreferences.getString("id_pengguna",""));
                        return params;
                    }
                };
        requestQueue.add(stringRequest);
    }


    private void loadKomentar() {
        list_komentar=new ArrayList<>();
        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest=
                new StringRequest(Request.Method.POST,URL_LOAD_KOMENTAR,
                        response -> {
                            try {
                                JSONArray jsonArray=new JSONArray(response);
                                String jml_komentar=String.valueOf(jsonArray.length())+" Komentar";
                                jumlah_komentar.setText(jml_komentar);
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    KomentarModel komentarModel=new KomentarModel();
                                    komentarModel.setId_komentar(jsonObject.getInt("id"));
                                    komentarModel.setParent_id(jsonObject.getInt("parent_id"));
                                    komentarModel.setId_video(jsonObject.getInt("id_video"));
                                    komentarModel.setKomentar(jsonObject.getString("komentar"));
                                    komentarModel.setNama_lengkap(jsonObject.getString("nama_komentar"));
                                    komentarModel.setId_pengguna(jsonObject.getString("id_pengguna"));
                                    komentarModel.setTgl(jsonObject.getString("tgl"));
                                    komentarModel.setWaktu(jsonObject.getString("waktu"));
                                    komentarModel.setView_more(jsonObject.getInt("reply"));
                                    komentarModel.setWaktu_format(jsonObject.getString("tgl_format"));
                                    list_komentar.add(komentarModel);
                                }
                                    komentarAdapter=new KomentarAdapter(this,list_komentar);
                                    rcy_header.setAdapter(komentarAdapter);
                                    komentarAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                               Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
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

    public void loadKeyboard(int id, int id_videos){
        editText.requestFocus();  InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        id_komentar=id;
        aksi_kirim=true;
    }

    public void loadKeyboardSub(int id, int id_videos){
        editText.requestFocus();  InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        id_komentar=id;
        aksi_kirim=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadKomentar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(aksi==1){
            startActivity(new Intent(KomentarUser.this,DetailVideoUser.class));
            finish();
        }
        else{
            Intent intent=new Intent(KomentarUser.this,DetailVideo.class);
            intent.putExtra("id_video",bundle.getInt("id_video"));
            intent.putExtra("suka",bundle.getInt("suka"));
            intent.putExtra("lihat",bundle.getInt("lihat"));
            intent.putExtra("komen",bundle.getInt("komen"));
            intent.putExtra("uri",bundle.getString("uri"));
            startActivity(intent);
            finish();
        }

    }
}
