package com.cursoandroid.myopinion.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.myopinion.BitmapUtil;
import com.cursoandroid.myopinion.R;
import com.cursoandroid.myopinion.domain.Estabelecimento;
import com.cursoandroid.myopinion.wsTasks;
import com.github.ornolfr.ratingview.RatingView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by igor on 16/10/16.
 */

public class EstabelecimentoAdapter extends RecyclerView.Adapter<EstabelecimentoAdapter.MyViewHolder> {

    private List<Estabelecimento> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private wsTasks tasks;
    private Context c;

    public EstabelecimentoAdapter(Context c, List<Estabelecimento> list)
    {
        this.c = c;
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

        tasks = new wsTasks(holder,mList.get(position));

        if(mList.get(position).getFoto() == null){
            mList.get(position).setFoto(BitmapUtil.getBitmapAsByteArray(BitmapFactory.decodeResource(c.getResources(),R.drawable.no_picture)));
            tasks.execTaskLoadImgEstabelecimento(mList.get(position).getNome());
        }
        holder.ivEstabelecimento.setImageBitmap( BitmapFactory.decodeByteArray(mList.get(position).getFoto(),0,mList.get(position).getFoto().length)); // SETAR IMAGEM DO VETOR
        holder.tvModel.setText(mList.get(position).getNome());
        holder.tvType.setText(mList.get(position).getTipoEstabelecimento());
        holder.rvAvaliacao.setRating(mList.get(position).getRating()/*/(float)mList.get(position).getNumAvaliacoes()*/);
        holder.tvRegiao.setText(mList.get(position).getCidade());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivEstabelecimento;
        public TextView tvModel,tvType,tvRegiao;
        public RatingView rvAvaliacao;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivEstabelecimento = (ImageView) itemView.findViewById(R.id.iv_estabelecimento);
            tvModel = (TextView) itemView.findViewById(R.id.tv_model);
            tvType = (TextView) itemView.findViewById(R.id.tv_tipo_estabelecimento);
            tvRegiao = (TextView) itemView.findViewById(R.id.tv_regiao);
            rvAvaliacao = (RatingView) itemView.findViewById(R.id.rv_avaliacao);

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
