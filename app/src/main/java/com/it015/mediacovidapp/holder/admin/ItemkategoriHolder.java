package com.it015.mediacovidapp.holder.admin;

import android.view.View;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.it015.mediacovidapp.R;

public class ItemkategoriHolder extends RecyclerView.ViewHolder {
    public AutoCompleteTextView autoCompleteTextView;
    public ItemkategoriHolder(@NonNull View itemView) {
        super(itemView);
        autoCompleteTextView=itemView.findViewById(R.id.kategori_video);
    }


}
