package com.cursoandroid.myopinion;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.myopinion.adapter.EstabelecimentoAdapter;
import com.cursoandroid.myopinion.database.EstabelecimentoDAO;
import com.cursoandroid.myopinion.database.FavoritoDAO;
import com.cursoandroid.myopinion.domain.Estabelecimento;
import com.github.ornolfr.ratingview.RatingView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.refactor.library.ShapeImageView;
import de.hdodenhof.circleimageview.CircleImageView;

public class EstabelecimentoActivity extends AppCompatActivity {

    //    private static final int NUM_PAGES = 5;
    private static final int AVALIACAO = 1;
    private final String ESTABELECIMENTO = "estabelecimento";

    private ImageButton btVoltar, btFavorito;
    private TextView tvCidadeEstado, tvBairro, tvAreaComercial, tvTituloEstabelecimento;
    private ShapeImageView storeImage;
    private Button avaliarEstabelecimento;
    //    private Intent intent,init;
//    private ViewPager viewPager;
//    private PagerAdapter mPagerAdapter;
    private CircleImageView imgPerfil;
    private RatingView rvEstabelecimentoAvaliacao;
//    private EstabelecimentoDAO estabelecimentoDAO;
    private FavoritoDAO favoritoDAO;
    private Estabelecimento e;
    boolean favorite;
    wsTasks tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento);
        Intent i = getIntent();
//        estabelecimentoDAO = new EstabelecimentoDAO(this);
        favoritoDAO = new FavoritoDAO(this);
        e = (Estabelecimento) i.getSerializableExtra(ESTABELECIMENTO);
        imgPerfil = (CircleImageView) findViewById(R.id.image_perfil);
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(mPagerAdapter);
        btFavorito = (ImageButton) findViewById(R.id.bt_add_favorito);
        tvCidadeEstado = (TextView) findViewById(R.id.tv_cidade_estado);
        tvAreaComercial = (TextView) findViewById(R.id.tv_area_comercial);
        tvBairro = (TextView) findViewById(R.id.tv_bairro);
        tvTituloEstabelecimento = (TextView) findViewById(R.id.tv_titulo_estabelecimento);
        rvEstabelecimentoAvaliacao = (RatingView) findViewById(R.id.rv_estabelecimento_avaliacao);
        imgPerfil.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.avatar, 200, 200));
        btVoltar = (ImageButton) findViewById(R.id.back_estabelecimento);
        storeImage = (ShapeImageView) findViewById(R.id.store_image);
        avaliarEstabelecimento = (Button) findViewById(R.id.avaliar_estabelecimento);

        avaliarEstabelecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AvaliacaoActivity.class);
                i.putExtra(ESTABELECIMENTO, e);
                startActivityForResult(i, AVALIACAO);
            }
        });

        btFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavorite();
            }
        });

        setDataEstabelecimento();

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setDataEstabelecimento() {
        tvTituloEstabelecimento.setText(e.getNome());
        rvEstabelecimentoAvaliacao.setRating(e.getRating());
        if(e.getFoto() != null){ storeImage.setImageBitmap(e.getFotoBitmap()); }
        tvAreaComercial.setText(e.getTipoEstabelecimento());
        tvCidadeEstado.setText(e.getCidade() + " - " + e.getEstado());
        tvBairro.setText(e.getBairro());
    }

    private void checkFavorite() {
        if (favorite) {
            btFavorito.setImageResource(R.drawable.ic_favorite_2);
        } else {
            btFavorito.setImageResource(R.drawable.ic_favorite);
        }
    }

    private void setFavorite() {
        if (!favorite) {
            favoritoDAO.put(e.getId());
            favorite = true;
            Toast.makeText(getApplicationContext(), "Adicionado aos favoritos com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            favoritoDAO.delete(e.getId());
            favorite = false;
            Toast.makeText(getApplicationContext(), "Removido dos favoritos com sucesso!", Toast.LENGTH_SHORT).show();
            favorite = favoritoDAO.read((int)e.getId());
//            Toast.makeText(getApplicationContext(),e.getId()+"",Toast.LENGTH_SHORT).show();
        }
        checkFavorite();
    }

    @Override
    protected void onResume() {
        super.onResume();
        favorite = favoritoDAO.read((int) e.getId());
////        Toast.makeText(getApplicationContext(),favorite+"",Toast.LENGTH_SHORT).show();
        checkFavorite();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AVALIACAO && resultCode == RESULT_OK)
        {
            tasks = new wsTasks(e,rvEstabelecimentoAvaliacao);
            tasks.execTaskLoadEstabelecimento();
        }

    }

//    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
//        public ScreenSlidePagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return new ScreenSlidePageFragment();
//        }
//
//        @Override
//        public int getCount() {
//            return NUM_PAGES;
//        }
//    }



}



