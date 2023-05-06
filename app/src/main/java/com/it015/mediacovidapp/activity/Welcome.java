package com.it015.mediacovidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.activity.admin.Registasi;

public class Welcome extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    TextView btn_masuk,btn_daftar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        lottieAnimationView=findViewById(R.id.lottie_animation);
        btn_masuk=findViewById(R.id.btn_masuk);
        btn_daftar=findViewById(R.id.btn_daftar);
        lottieAnimationView.playAnimation();

        btn_masuk.setOnClickListener(v->{
            startActivity(new Intent(Welcome.this,Login.class));
        });

        btn_daftar.setOnClickListener(v->{
            startActivity(new Intent(Welcome.this, Registasi.class));
        });
    }
}
