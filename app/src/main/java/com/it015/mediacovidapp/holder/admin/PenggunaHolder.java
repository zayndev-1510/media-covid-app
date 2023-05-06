package com.it015.mediacovidapp.holder.admin;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.it015.mediacovidapp.R;

public class PenggunaHolder extends RecyclerView.ViewHolder {
    public TextView nama_lengkap,alamat;
    public ImageView image_pengguna;
    public PenggunaHolder(@NonNull View itemView) {
        super(itemView);
        this.nama_lengkap=itemView.findViewById(R.id.row_item_nama_pengguna);
        this.alamat=itemView.findViewById(R.id.row_item_alamat_pengguna);
        this.image_pengguna=itemView.findViewById(R.id.row_item_image_pengguna);
    }
}
