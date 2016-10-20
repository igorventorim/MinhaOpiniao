package com.cursoandroid.myopinion;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cursoandroid.myopinion.domain.Estabelecimento;
import com.github.ornolfr.ratingview.RatingView;

import cn.refactor.library.ShapeImageView;
import de.hdodenhof.circleimageview.CircleImageView;

public class EstabelecimentoActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 5;
    private final String ESTABELECIMENTO = "estabelecimento";

    private ImageButton btVoltar;
    private TextView tvCidadeEstado,tvBairro,tvAreaComercial,tvTituloEstabelecimento;
    private ShapeImageView storeImage;
    private Button avaliarEstabelecimento;
    private Intent intent,init;
    private ViewPager viewPager;
    private PagerAdapter mPagerAdapter;
    private CircleImageView imgPerfil;
    private RatingView rvEstabelecimentoAvaliacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento);

        imgPerfil = (CircleImageView) findViewById(R.id.image_perfil);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        tvCidadeEstado = (TextView) findViewById(R.id.tv_cidade_estado);
        tvAreaComercial = (TextView) findViewById(R.id.tv_area_comercial);
        tvBairro = (TextView) findViewById(R.id.tv_bairro);
        tvTituloEstabelecimento = (TextView) findViewById(R.id.tv_titulo_estabelecimento);
        rvEstabelecimentoAvaliacao = (RatingView) findViewById(R.id.rv_estabelecimento_avaliacao);

        imgPerfil.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.avatar, 200, 200));
        btVoltar = (ImageButton) findViewById(R.id.back_estabelecimento);
        storeImage = (ShapeImageView) findViewById(R.id.store_image);
        avaliarEstabelecimento = (Button) findViewById(R.id.avaliar_estabelecimento);

        intent = new Intent(this,AvaliacaoActivity.class);
        avaliarEstabelecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });


//        init= getIntent();
//        Bundle b = init.getExtras();
//        Estabelecimento est = (Estabelecimento) b.getSerializable(ESTABELECIMENTO);
//        Estabelecimento est = (Estabelecimento) init.getSerializableExtra(ESTABELECIMENTO);
//        tvTituloEstabelecimento.setText(est.getName());
//        rvEstabelecimentoAvaliacao.setRating(Float.parseFloat(est.getRating()));

//        storeImage.setImageBitmap(est.getImg());



        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}




