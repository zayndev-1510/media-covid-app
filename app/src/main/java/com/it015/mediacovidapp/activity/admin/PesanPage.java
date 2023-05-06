package com.it015.mediacovidapp.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.it015.mediacovidapp.R;

public class PesanPage extends AppCompatActivity {
    TextView btn_kembali;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pesan_page);
        btn_kembali=findViewById(R.id.data_video);
        btn_kembali.setOnClickListener(v->{
            startActivity(new Intent(PesanPage.this,Video.class));
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PesanPage.this,Video.class));
        finish();
    }
}
