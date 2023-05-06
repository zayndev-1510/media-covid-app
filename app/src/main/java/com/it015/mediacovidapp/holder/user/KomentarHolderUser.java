package com.it015.mediacovidapp.holder.user;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.it015.mediacovidapp.R;

public class KomentarHolderUser extends RecyclerView.ViewHolder {
    public TextView nama_lengkap_komentar,ket_komentar,waktu_komentar,view_more;
    public RecyclerView recyclerView;
    public TextView reply;

    public KomentarHolderUser(@NonNull View itemView) {
        super(itemView);
        nama_lengkap_komentar=itemView.findViewById(R.id.nama_lengkap_komentar);
        ket_komentar=itemView.findViewById(R.id.komentar_user);
        waktu_komentar=itemView.findViewById(R.id.waktu_komentar);
        view_more=itemView.findViewById(R.id.view_more);
        recyclerView=itemView.findViewById(R.id.rcy_sub_komentar);
        reply=itemView.findViewById(R.id.reply);
    }
}
