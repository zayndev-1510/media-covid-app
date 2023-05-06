package com.it015.mediacovidapp.holder.admin;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.it015.mediacovidapp.R;

public class PenontonHolder extends RecyclerView.ViewHolder {
    public TextView txtnama;
    public ImageView imageView;
    public PenontonHolder(@NonNull View itemView) {
        super(itemView);
        txtnama=itemView.findViewById(R.id.row_item_nama_pengguna_view);
        imageView=itemView.findViewById(R.id.row_item_image_pengguna_view);
    }
}
