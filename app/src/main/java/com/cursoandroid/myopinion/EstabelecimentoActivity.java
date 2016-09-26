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

import cn.refactor.library.ShapeImageView;
import de.hdodenhof.circleimageview.CircleImageView;

public class EstabelecimentoActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 5;
    private ImageButton btVoltar;
    private ShapeImageView storeImage;
    private Button avaliarEstabelecimento;
    private Intent intent;
    private ViewPager viewPager;
    private PagerAdapter mPagerAdapter;
    private CircleImageView imgPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento);

        imgPerfil = (CircleImageView) findViewById(R.id.image_perfil);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);


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


        storeImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.barteste, 150, 150));



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




