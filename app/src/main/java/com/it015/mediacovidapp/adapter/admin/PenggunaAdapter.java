package com.it015.mediacovidapp.adapter.admin;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.holder.admin.DashboardVideoHolder;
import com.it015.mediacovidapp.holder.admin.PenggunaHolder;
import com.it015.mediacovidapp.model.admin.PenggunaModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PenggunaAdapter extends RecyclerView.Adapter<PenggunaHolder> {
    List<PenggunaModel> list_pengguna;
    Context context;
    LayoutInflater layoutInflater;

    public PenggunaAdapter(List<PenggunaModel> list_pengguna, Context context) {
        this.list_pengguna = list_pengguna;
        this.context = context;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PenggunaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=layoutInflater.inflate(R.layout.item_pengguna,parent,false);
       return new PenggunaHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PenggunaHolder holder, int position) {
        PenggunaModel penggunaModel=list_pengguna.get(position);
        holder.nama_lengkap.setText(penggunaModel.getNama_lengkap());
        holder.alamat.setText(penggunaModel.getAlamat());
        String url="";
        String foto_profil=penggunaModel.getFoto_profil();
        if(foto_profil.equals("default.png")){
            url=context.getString(R.string.ipgeneral)+"akun/default.png";
        }else{
            url=context.getString(R.string.ipgeneral)+"akun/"+penggunaModel.getId_pengguna()+"/"+foto_profil;
        }
        Uri uri=Uri.parse(url);
        Picasso.get().load(uri).into(holder.image_pengguna);
    }

    @Override
    public int getItemCount() {
        return list_pengguna.size();
    }
}
