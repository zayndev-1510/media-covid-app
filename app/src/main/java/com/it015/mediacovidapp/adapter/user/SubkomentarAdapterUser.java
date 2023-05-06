package com.it015.mediacovidapp.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.activity.KomentarUser;
import com.it015.mediacovidapp.holder.user.SubKomentar;
import com.it015.mediacovidapp.model.user.SubKomentarModel;

import java.util.List;

public class SubkomentarAdapterUser extends RecyclerView.Adapter<SubKomentar> {
    Context context;
    List<SubKomentarModel> list_sub;
    LayoutInflater layoutInflater;

    public SubkomentarAdapterUser(Context context, List<SubKomentarModel> list_sub) {
        this.context = context;
        this.list_sub = list_sub;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SubKomentar onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=layoutInflater.inflate(R.layout.sub_koment_item,parent,false);
        return new SubKomentar(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubKomentar holder, int position) {
        SubKomentarModel subKomentarModel=list_sub.get(position);
        holder.nama_lengkap_komentar.setText(subKomentarModel.getNama_lengkap());
        holder.ket_komentar.setText(subKomentarModel.getKomentar());
        holder.waktu_komentar.setText(subKomentarModel.getWaktu_format());

        holder.sub_reply.setOnClickListener(v->{
            if (context instanceof KomentarUser) {
                ((KomentarUser) context).loadKeyboardSub(subKomentarModel.getParent_id(),subKomentarModel.getId_video());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_sub.size();
    }
}
