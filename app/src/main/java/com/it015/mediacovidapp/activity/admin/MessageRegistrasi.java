package com.it015.mediacovidapp.activity.admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.activity.Home;
import com.it015.mediacovidapp.apiconfig.admin.NetworkClient;
import com.it015.mediacovidapp.apiconfig.admin.VideoApi;
import com.it015.mediacovidapp.widget.Loading_bar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageRegistrasi extends AppCompatActivity {
    TextView upload_foto,lewati;
    Uri checkfoto;
    ImageView imageView;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_registrasi);
        upload_foto=findViewById(R.id.upload);
        imageView=findViewById(R.id.foto_profil);
        lewati=findViewById(R.id.lewati);

        imageView.setOnClickListener(v->{
            askPermision();
        });
        lewati.setOnClickListener(v->{
            Dialog dialog;
            Loading_bar loading_bar=new Loading_bar(this);
            dialog=loading_bar.loadDialog("Sedang Memuat");
            dialog.create();
            dialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(getApplicationContext(),Home.class);
                    startActivity(intent);
                }
            },3000);
        });
        upload_foto.setOnClickListener(v->{
            if(checkfoto ==null){
                Toast.makeText(this,"Belum ada foto yang dimasukan !",Toast.LENGTH_LONG).show();
            }else{
               uploadFoto();
            }
        });
    }

    private void uploadFoto() {
      try {
          File file=new File(getPathFile(checkfoto));
          Loading_bar loading_bar=new Loading_bar(this);
          Dialog dialog_upload=loading_bar.loadDialog("Sedang Memproses");
          dialog_upload.create();
          dialog_upload.show();
          String temp_id=getIntent().getStringExtra("id_pengguna");
          RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);
          MultipartBody.Part filepart=MultipartBody.Part.createFormData("file",file.getName(),requestBody);
          RequestBody id_pengguna=RequestBody.create(MultipartBody.FORM,temp_id);
          String url=getString(R.string.ipadress);
          VideoApi videoApi= NetworkClient.getRetrofit(url).create(VideoApi.class);
          Call<ResponseBody> call=videoApi.uploadFoto(filepart,id_pengguna);
          call.enqueue(new Callback<ResponseBody>() {
              @Override
              public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                  dialog_upload.dismiss();
                  if(response.body() !=null){
                      try {
                          String data=response.body().string();
                          JSONObject jsonObject=new JSONObject(data);
                          int val=jsonObject.getInt("val");
                          if(val==1){
                              startActivity(new Intent(MessageRegistrasi.this, Home.class));
                              finish();
                          }
                      } catch (IOException | JSONException e) {
                          Toast.makeText(getApplicationContext(),"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
                      }
                  }
              }

              @Override
              public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                  dialog_upload.dismiss();
                  Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
              }
          });


      }
      catch (Exception e){
          Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
      }

    }

    private String getPathFile(Uri selectvideo) {
        String [] projection={MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, selectvideo, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    private void askPermision() {
        int permision= ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permision != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }
        else{
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(i, "Select a file"), REQUEST_EXTERNAL_STORAGE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode ==REQUEST_EXTERNAL_STORAGE && resultCode==RESULT_OK && null !=data){
                Uri selectimage=data.getData();
                checkfoto=data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectimage);
                imageView.setImageURI(selectimage);
            }
            else{

                Toast.makeText(this,"Waduh silakan dipilih dulu video yang diupload",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(this,"Something wrong",Toast.LENGTH_LONG).show();
        }
    }
}
