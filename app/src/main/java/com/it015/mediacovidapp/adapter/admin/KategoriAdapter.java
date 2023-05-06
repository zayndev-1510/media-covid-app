package com.it015.mediacovidapp.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.holder.admin.ItemkategoriHolder;
import com.it015.mediacovidapp.model.admin.KategoriModel;

import java.util.List;

public class KategoriAdapter extends RecyclerView.Adapter<ItemkategoriHolder> {
    Context context;
    List<KategoriModel> dataset;
    LayoutInflater layoutInflater;

    public KategoriAdapter(Context context, List<KategoriModel> dataset) {
        this.context = context;
        this.dataset = dataset;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ItemkategoriHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=layoutInflater.inflate(R.layout.item_kategori,parent,false);
        return new ItemkategoriHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemkategoriHolder holder, int position) {
        KategoriModel kategoriModel=dataset.get(position);
        holder.autoCompleteTextView.setText(kategoriModel.getNama_kategori());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
