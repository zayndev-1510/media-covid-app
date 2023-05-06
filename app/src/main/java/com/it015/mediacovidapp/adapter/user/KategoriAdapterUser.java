package com.it015.mediacovidapp.adapter.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.it015.mediacovidapp.holder.user.KategoriHolderUser;
import com.it015.mediacovidapp.model.VideoModel;
import com.it015.mediacovidapp.model.admin.KategoriModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KategoriAdapterUser extends RecyclerView.Adapter<KategoriHolderUser> {
    Context context;
    List<KategoriModel> list;
    List<VideoModel> list_video;
    LayoutInflater layoutInflater;
    private static int lastClickedPosition = 0;
    MyCallback myCallback;
    String URL_DATA_VIDEO ="";

    public KategoriAdapterUser(Context context, List<KategoriModel> list, MyCallback myCallback) {
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.myCallback = myCallback;
        this.URL_DATA_VIDEO=context.getResources().getString(R.string.ipadress)+"app/data-video-user";
    }

    @NonNull
    @Override
    public KategoriHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_kategeri_user, parent, false);
        return new KategoriHolderUser(v);
    }

    @SuppressLint("NotifyDataSetChanged")

    @Override
    public void onBindViewHolder(@NonNull KategoriHolderUser holder, @SuppressLint("RecyclerView") int position) {
        KategoriModel kategoriModel = list.get(position);
        holder.txtcaption.setText(kategoriModel.getNama_kategori());

        holder.txtcaption.setOnClickListener(v -> {
            lastClickedPosition = position;
            dataByKategori(kategoriModel.getId(), position);
            notifyDataSetChanged();
        });
        if (lastClickedPosition == position) {
            holder.txtcaption.setBackgroundResource(R.color.purple_500);
            holder.txtcaption.setTextColor(Color.parseColor("#FFFFFF"));

        } else {
            holder.txtcaption.setBackgroundResource(R.color.white);
            holder.txtcaption.setTextColor(Color.parseColor("#000000"));
        }
    }

    private void dataByKategori(int id_kategori, int position) {
        list_video = new ArrayList<>();
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        @SuppressLint("NotifyDataSetChanged")
        StringRequest stringRequest =
                new StringRequest(Request.Method.POST, URL_DATA_VIDEO,
                        response -> {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(response);
                                myCallback.loadData(jsonArray);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        },
                        error -> {
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_kategori", String.valueOf(id_kategori));
                        return params;
                    }
                };
        requestQueue.add(stringRequest);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface MyCallback {
        void loadData(JSONArray jsonArray);
    }
}
