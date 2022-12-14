package com.example.technicalseminar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technicalseminar.R;
import com.example.technicalseminar.model.BlockModel;
import com.example.technicalseminar.viewholder.RecyclerViewHolder;

import java.util.Date;
import java.util.List;

public class BlockAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<BlockModel> blocks;
    private Context mContext;
    private int lastPosition = -1;

    public BlockAdapter(@Nullable List<BlockModel> blocks, @NonNull Context mContext) {
        this.blocks = blocks;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.txtIndex.setText(String.format(mContext.getString(R.string.title_block_number),blocks.get(position).getIndex()));
        holder.txtPreviousHash.setText(blocks.get(position).getPreviousHash()!=null?blocks.get(position).getPreviousHash():"Null");
        holder.txtTimestamp.setText(String.valueOf(new Date(blocks.get(position).getTimestamp())));
        holder.txtData.setText(blocks.get(position).getData());
        holder.txtHash.setText(blocks.get(position).getHash());
        setAnimation(holder.itemView,position);
    }

    private void setAnimation(View itemView, int position) {
        if(position>lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext,android.R.anim.fade_in);
            itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_block_data;
    }

    @Override
    public int getItemCount() {
        return blocks == null?0:blocks.size();
    }
}
