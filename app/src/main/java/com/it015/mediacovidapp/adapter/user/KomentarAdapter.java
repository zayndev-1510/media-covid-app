package com.it015.mediacovidapp.adapter.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.activity.Home;
import com.it015.mediacovidapp.activity.KomentarUser;
import com.it015.mediacovidapp.holder.user.KomentarHolderUser;
import com.it015.mediacovidapp.model.data.KomentarData;
import com.it015.mediacovidapp.model.user.KomentarModel;
import com.it015.mediacovidapp.model.user.SubKomentarModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KomentarAdapter extends RecyclerView.Adapter<KomentarHolderUser> {
    Context context;
    List<KomentarModel> list_komentar;
    List<SubKomentarModel> sub_komentar;
    LayoutInflater layoutInflater;
    KomentarUser komentarUser;

    public int id_komentar;
    SubkomentarAdapterUser subkomentarAdapterUser;
    String URL_LOAD_KOMENTAR="";

    public KomentarAdapter(Context context, List<KomentarModel> list_komentar) {

        this.context = context;
        this.list_komentar = list_komentar;
        this.layoutInflater=LayoutInflater.from(context);
        this.URL_LOAD_KOMENTAR=context.getResources().getString(R.string.ipadress)+"app/data-komentar-reply";

    }

    @NonNull
    @Override
    public KomentarHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v=layoutInflater.inflate(R.layout.header_comment_item,parent,false);
       return new KomentarHolderUser(v);
    }

    public KomentarAdapter() {
    }

    @Override
    public void onBindViewHolder(@NonNull KomentarHolderUser holder, int position) {
        KomentarModel komentarModel=list_komentar.get(position);

        holder.nama_lengkap_komentar.setText(komentarModel.getNama_lengkap());
        holder.ket_komentar.setText(komentarModel.getKomentar());
        holder.waktu_komentar.setText(komentarModel.getWaktu_format());
        int view_more=komentarModel.getView_more();
        if(view_more==0){
            holder.view_more.setVisibility(View.INVISIBLE);
        }else{
            holder.view_more.setVisibility(View.VISIBLE);
        }

        holder.reply.setOnClickListener(v->{
            if (context instanceof KomentarUser) {
                ((KomentarUser) context).loadKeyboard(komentarModel.getId_komentar(),komentarModel.getId_video());
            }
            });
        holder.view_more.setOnClickListener(v->{

            int id=komentarModel.getId_komentar();

            loadReply(id,holder);
        });
    }
    private void loadReply(int id, KomentarHolderUser holder) {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        sub_komentar=new ArrayList<>();
        Cache cache=new DiskBasedCache(context.getCacheDir(),1024*1024);
        Network network=new BasicNetwork(new HurlStack());
        RequestQueue requestQueue=new RequestQueue(cache,network);
        requestQueue.start();
        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest=
                new StringRequest(Request.Method.POST,URL_LOAD_KOMENTAR,
                        response -> {
                            try {
                                JSONArray jsonArray=new JSONArray(response);
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    SubKomentarModel subKomentarModel=new SubKomentarModel();
                                    subKomentarModel.setId_komentar(jsonObject.getInt("id"));
                                    subKomentarModel.setParent_id(jsonObject.getInt("parent_id"));
                                    subKomentarModel.setId_video(jsonObject.getInt("id_video"));
                                    subKomentarModel.setKomentar(jsonObject.getString("komentar"));
                                    subKomentarModel.setNama_lengkap(jsonObject.getString("nama_komentar"));
                                    subKomentarModel.setId_pengguna(jsonObject.getString("id_pengguna"));
                                    subKomentarModel.setTgl(jsonObject.getString("tgl"));
                                    subKomentarModel.setWaktu(jsonObject.getString("waktu"));
                                    subKomentarModel.setView_more(jsonObject.getInt("reply"));
                                    subKomentarModel.setWaktu_format(jsonObject.getString("tgl_format"));
                                    sub_komentar.add(subKomentarModel);
                                }
                                    subkomentarAdapterUser =new SubkomentarAdapterUser(context,sub_komentar);
                                    holder.recyclerView.setAdapter(subkomentarAdapterUser);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        },
                        error -> {
                            Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<>();
                        params.put("id",String.valueOf(id));
                        return params;
                    }
                };
        requestQueue.add(stringRequest);

    }
    @Override
    public int getItemCount() {
        return list_komentar.size();
    }

}
