package com.it015.mediacovidapp.adapter.user;

import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.it015.mediacovidapp.R;
import com.it015.mediacovidapp.holder.user.KomentarHolderUser;
import com.it015.mediacovidapp.model.user.KomentarModel;
import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.ExpandableItem;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;
import com.xwray.groupie.databinding.BindableItem;

import java.util.List;

public class TestGroupAdapter extends Item<GroupieViewHolder> {
    ExpandableGroup expandableGroup;
    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        viewHolder.itemView.findViewById(R.id.nama_lengkap_komentar);
        viewHolder.itemView.findViewById(R.id.komentar_user);
        viewHolder.itemView.findViewById(R.id.view_more).setOnClickListener(v->{
            expandableGroup.onToggleExpanded();
        });
    }

    public void setExpandableGroup(ExpandableGroup expandableGroup) {
        this.expandableGroup = expandableGroup;
    }

    @Override
    public int getLayout() {
        return R.layout.header_comment_item;
    }
}
