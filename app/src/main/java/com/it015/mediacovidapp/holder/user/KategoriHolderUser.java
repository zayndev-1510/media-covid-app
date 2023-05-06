package com.it015.mediacovidapp.holder.user;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.it015.mediacovidapp.R;

public class KategoriHolderUser extends RecyclerView.ViewHolder {
    public TextView txtcaption;
    public KategoriHolderUser(@NonNull View itemView) {
        super(itemView);
        txtcaption=itemView.findViewById(R.id.caption_kategori);

    }
}
