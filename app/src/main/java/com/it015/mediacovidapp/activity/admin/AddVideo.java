package com.it015.mediacovidapp.activity.admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.google.gson.Gson;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.adapter.admin.KategoriAdapter;
import com.it015.mediacovidapp.apiconfig.admin.NetworkClient;
import com.it015.mediacovidapp.apiconfig.admin.VideoApi;
import com.it015.mediacovidapp.model.VideoModel;
import com.it015.mediacovidapp.model.admin.KategoriModel;
import com.it015.mediacovidapp.widget.Loading_bar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddVideo extends AppCompatActivity {
    TextView buka_video;
    TextView upload_video;
    AutoCompleteTextView kategori_video;
    List<String> dataset=new ArrayList<>();
    List<KategoriModel> list_kategori;
    KategoriAdapter kategoriAdapter;
    String API_DATA_KATEGORI;
    int id_kategori;
    String [] MEDIA_COLUMS={MediaStore.Video.Media._ID};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    String MEDIA_PATH;
    ImageView image_thumbnail;
    PlayerView playerView;
    String VIDEO_PATH;
    SimpleExoPlayer simpleExoPlayer;
    Uri checkvideo;
    EditText jdul_video;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_video_admin);

        API_DATA_KATEGORI=getResources().getString(R.string.ipadress)+"app/data-kategori";

        buka_video=findViewById(R.id.buka_video);
        playerView=findViewById(R.id.preview_video);
        upload_video=findViewById(R.id.upload_video);
        jdul_video=findViewById(R.id.judul_video);
        kategori_video=findViewById(R.id.kategori_video);

        loadItemKategori();
        buka_video.setOnClickListener(v->{
            askPermision();
        });

        upload_video.setOnClickListener(v->{
           if(id_kategori==0){
               Toast.makeText(this,"Silakan pilih kategori dulu",Toast.LENGTH_LONG).show();
           }else{
               uploadVideo();
           }
        });
    }

    private void loadItemKategori() {
        Cache cache=new DiskBasedCache(getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());

        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        list_kategori=new ArrayList<>();
        List<String> dataset=new ArrayList<>();
        StringRequest stringRequest =new StringRequest(Request.Method.GET,API_DATA_KATEGORI,
                response -> {
                    try {
                        JSONArray jsonArray=new JSONArray(response);

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            KategoriModel kategoriModel=new KategoriModel();
                            kategoriModel.setId(jsonObject.getInt("id"));
                            kategoriModel.setNama_kategori(jsonObject.getString("nama_kategori"));
                            list_kategori.add(kategoriModel);
                            dataset.add(jsonObject.getString("nama_kategori"));
                        }

                        ArrayAdapter <String> arrayadapter = new ArrayAdapter<String>(this,R.layout.item_kategori,dataset);
                        kategori_video.setText(arrayadapter.getItem(0));
                        kategori_video.setAdapter(arrayadapter);
                        kategori_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                id_kategori=list_kategori.get(i).getId();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this,error.getMessage(),Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(stringRequest);


    }

    private void askPermision() {
        int permision= ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permision != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }
        AlertDialog.Builder alBuilder=new AlertDialog.Builder(getApplicationContext());
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_EXTERNAL_STORAGE);
    }
    @SuppressLint("Recycle")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode ==1 && resultCode==RESULT_OK && null !=data){
                Uri selectvideo=data.getData();
                checkvideo=data.getData();
                simpleExoPlayer = new SimpleExoPlayer.Builder(getApplicationContext()).build();
                playerView.setPlayer(simpleExoPlayer);
                MediaItem mediaItem = MediaItem.fromUri(selectvideo);
                simpleExoPlayer.addMediaItem(mediaItem);
                simpleExoPlayer.prepare();
                simpleExoPlayer.play();
                VIDEO_PATH=getPathFile(selectvideo);
            }
            else{
                Toast.makeText(this,"Waduh silakan dipilih dulu video yang diupload",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(this,"Something wrong",Toast.LENGTH_LONG).show();
        }

    }
    @SuppressLint("Recycle")
    private String getPathFile(Uri selectvideo) {
        String [] projection={MediaStore.Video.Media.DATA};
        Cursor cursor=getContentResolver().query(selectvideo,projection,null,null,null);
        if(cursor !=null){
            int column_index=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }else{
            return null;
        }
    }

    private void uploadVideo() {
        String str_judul_video=jdul_video.getText().toString();
        if(checkvideo ==null &&  str_judul_video.isEmpty()){
            Toast.makeText(this,"Hallo,admin judul video dan video dimasukan dulu",Toast.LENGTH_LONG).show();
        }
        else if(checkvideo ==null){
            Toast.makeText(this,"Hallo,admin video nya belum dimasukan",Toast.LENGTH_LONG).show();
        }
        else if(str_judul_video.isEmpty()){
            Toast.makeText(this,"Hallo,admin judul video nya belum dimasukan",Toast.LENGTH_LONG).show();
        }
        else{
            Loading_bar loading_bar=new Loading_bar(this);
            Dialog dialog_upload=loading_bar.loadDialog("Sedang Memproses");
            dialog_upload.setCancelable(false);
            dialog_upload.setCanceledOnTouchOutside(false);
            dialog_upload.create();
            dialog_upload.show();
            File file=new File(VIDEO_PATH);
            RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);
            MultipartBody.Part filepart=MultipartBody.Part.createFormData("file",file.getName(),requestBody);

            RequestBody judul_video=RequestBody.create(MultipartBody.FORM,jdul_video.getText().toString().trim());
            RequestBody id_kategori_body=RequestBody.create(MultipartBody.FORM,String.valueOf(id_kategori));
            String url=getString(R.string.ipadress);
            VideoApi videoApi= NetworkClient.getRetrofit(url).create(VideoApi.class);
            Call<ResponseBody> call=videoApi.uploadVideo(filepart,judul_video,id_kategori_body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    dialog_upload.dismiss();
                    if(response.body() !=null){
                        try {
                            String data=response.body().string();
                            JSONObject jsonObject=new JSONObject(data);
                            int val=jsonObject.getInt("val");
                            android.util.Log.d("cek",String.valueOf(val));
                            if(val==1){
                                startActivity(new Intent(AddVideo.this,PesanPage.class));
                                finish();
                                simpleExoPlayer.stop();
                                playerView.onPause();
                            }
                        } catch (IOException | JSONException e) {
                            Toast.makeText(AddVideo.this,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    dialog_upload.dismiss();
                    Toast.makeText(AddVideo.this,t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddVideo.this,Dashboard.class));
        simpleExoPlayer.stop();
        playerView.onPause();
    }
}
