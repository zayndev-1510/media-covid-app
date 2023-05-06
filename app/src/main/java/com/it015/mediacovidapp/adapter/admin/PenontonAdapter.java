package com.it015.mediacovidapp.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.holder.admin.PenontonHolder;
import com.it015.mediacovidapp.model.admin.PenontonModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PenontonAdapter extends RecyclerView.Adapter<PenontonHolder> {

    Context context;
    List<PenontonModel> list;
    LayoutInflater layoutInflater;

    public PenontonAdapter(Context context, List<PenontonModel> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PenontonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=layoutInflater.inflate(R.layout.item_data_penonton,parent,false);
        return new PenontonHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PenontonHolder holder, int position) {
            PenontonModel penontonModel=list.get(position);
            String foto_profil=penontonModel.getFoto_profil();
            String url="";
            if(foto_profil.equals("default.png")){
                url= context.getString(R.string.ipgeneral)+"akun/default.png";
            }else{
                url=context.getString(R.string.ipgeneral)+"akun/"+penontonModel.getId_pengguna()+"/"+foto_profil;
            }
            Picasso.get().load(url).into(holder.imageView);
            holder.txtnama.setText(penontonModel.getNama_lengkap());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
