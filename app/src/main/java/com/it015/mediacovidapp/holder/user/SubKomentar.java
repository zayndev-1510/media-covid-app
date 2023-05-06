package com.it015.mediacovidapp.holder.user;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.it015.mediacovidapp.R;

public class SubKomentar extends RecyclerView.ViewHolder {
    public TextView nama_lengkap_komentar,ket_komentar,waktu_komentar,sub_reply;
    public SubKomentar(@NonNull View itemView) {
        super(itemView);
        nama_lengkap_komentar=itemView.findViewById(R.id.nama_lengkap_komentar_sub);
        waktu_komentar=itemView.findViewById(R.id.waktu_komentar_sub);
        ket_komentar=itemView.findViewById(R.id.komentar_user_sub);
        sub_reply=itemView.findViewById(R.id.reply_sub);
    }
}
