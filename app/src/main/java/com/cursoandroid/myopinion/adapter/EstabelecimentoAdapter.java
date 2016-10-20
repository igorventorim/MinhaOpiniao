package com.cursoandroid.myopinion.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.myopinion.R;
import com.cursoandroid.myopinion.domain.Estabelecimento;

import java.util.List;

/**
 * Created by igor on 16/10/16.
 */

public class EstabelecimentoAdapter extends RecyclerView.Adapter<EstabelecimentoAdapter.MyViewHolder> {

    private List<Estabelecimento> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public EstabelecimentoAdapter(Context c, List<Estabelecimento> list)
    {
        mList = list;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setmRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r)
    {
        mRecyclerViewOnClickListenerHack = r;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.item_estabelecimento, parent,false);

        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.ivEstabelecimento.setImageBitmap(mList.get(position).getImg()); // SETAR IMAGEM DO VETOR
        holder.tvModel.setText(mList.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivEstabelecimento;
        public TextView tvModel;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivEstabelecimento = (ImageView) itemView.findViewById(R.id.iv_estabelecimento);
            tvModel = (TextView) itemView.findViewById(R.id.tv_model);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null)
            {
                mRecyclerViewOnClickListenerHack.onClickListener(v,getAdapterPosition());
            }
        }
    }


}
