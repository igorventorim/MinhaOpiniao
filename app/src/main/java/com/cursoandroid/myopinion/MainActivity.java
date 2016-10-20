package com.cursoandroid.myopinion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cursoandroid.myopinion.adapter.EstabelecimentoAdapter;
import com.cursoandroid.myopinion.adapter.RecyclerViewOnClickListenerHack;
import com.cursoandroid.myopinion.domain.Estabelecimento;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.github.clans.fab.FloatingActionButton;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener,RecyclerViewOnClickListenerHack {

    private final int VOICE_RECOGNITION_REQUEST_CODE = 1;

    FloatingActionButton addEstabelecimento,verEstabelecimento;

    private Drawer navigationDrawerLeft;
    private AccountHeader headerNavigationLeft;

    Intent adicionaLocal,estabelecimento;
    private MaterialSearchBar searchBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ViewPager viewPager;
    List<Estabelecimento> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Estabelecimento n = new Estabelecimento();
        n.setName("BAR DO SEU ZÉ");
        n.setImg(BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.barteste, 200, 200));
        mList.add(n);

        Estabelecimento m = new Estabelecimento();
        m.setName("BAR DO SEU ZÉ");
        m.setImg(BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.barteste, 200, 200));
        mList.add(m);

        Estabelecimento k = new Estabelecimento();
        k.setName("BAR DO LUIZ");
        k.setImg(BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.barteste, 200, 200));
        mList.add(k);

        Estabelecimento l = new Estabelecimento();
        l.setName("BAR DO LUIZ");
        l.setImg(BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.barteste, 200, 200));
        mList.add(l);

        Estabelecimento j = new Estabelecimento();
        j.setName("BAR");
        j.setImg(BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.barteste, 200, 200));
        mList.add(j);

        recyclerView = (RecyclerView) findViewById(R.id.rv_estabelecimentos);
        addEstabelecimento = (FloatingActionButton) findViewById(R.id.adicionar_loja);
        initSearchBar();
        adicionaLocal = new Intent(this,AdicionaLocalAcitivity.class);
        estabelecimento = new Intent(this,EstabelecimentoActivity.class);
        addEstabelecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(adicionaLocal);
            }
        });

        searchBar.setOnSearchActionListener(this);

        initNavBar();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(getApplicationContext(),mList);
        adapter.setmRecyclerViewOnClickListenerHack(this);
        recyclerView.setAdapter(adapter);

        final IProfile profile = new ProfileDrawerItem().withName("Igor Ventorim").withEmail("igor.ventorim@gmail.com").withIcon(ContextCompat.getDrawable(this,R.drawable.avatar)).withIdentifier(100);

        //HeaderNavigation
        headerNavigationLeft = new AccountHeaderBuilder()
                .withActivity(this)
//                .withHeaderBackground(R.drawable.idea)
                .withHeaderBackground(new ColorDrawable(Color.parseColor("#00bfff")))
                .withTranslucentStatusBar(true)
                .withSelectionListEnabled(false)
                .withHeightDp(150)
                .addProfiles(profile)
                .build();

        //NAVIGATION DRAWER
        navigationDrawerLeft = new DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggleAnimated(true)
                .withDisplayBelowStatusBar(false)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(0)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        return false;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener(){

                    @Override
                    public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                        return false;
                    }
                })
                .withAccountHeader(headerNavigationLeft)
                .build();
        navigationDrawerLeft.addItems(
                new PrimaryDrawerItem().withName("Minha Conta").withIcon(ContextCompat.getDrawable(this,R.drawable.ic_account_circle_black_24dp)).withIdentifier(1).withSelectable(false),
                new PrimaryDrawerItem().withName("Adicionar Estabelecimento").withIcon(ContextCompat.getDrawable(this,R.drawable.ic_playlist_add_black_24dp)).withIdentifier(2).withSelectable(false),
                new PrimaryDrawerItem().withName("Meus Estabelecimentos").withIcon(ContextCompat.getDrawable(this,R.drawable.ic_star_rate_black_18dp)).withIdentifier(3).withSelectable(false),
                new PrimaryDrawerItem().withName("Fale Conosco").withIcon(ContextCompat.getDrawable(this,R.drawable.ic_question_answer_black_24dp)).withIdentifier(4).withSelectable(false),
                new PrimaryDrawerItem().withName("Sair").withIcon(ContextCompat.getDrawable(this,R.drawable.ic_logout)).withIdentifier(5).withSelectable(false),
//                new DividerDrawerItem(),
                new SectionDrawerItem().withName("Sobre o Aplicativo"),
                new SecondaryDrawerItem().withName("Dúvidas Frequentes").withIcon(ContextCompat.getDrawable(this,R.drawable.ic_ask_question)).withIdentifier(6).withSelectable(false),
                new SecondaryDrawerItem().withName("Curta no Facebook").withIcon(ContextCompat.getDrawable(this,R.drawable.ic_curtir)).withIdentifier(7).withSelectable(false),
                new SecondaryDrawerItem().withName("Avalie na Google Play").withIcon(ContextCompat.getDrawable(this,R.drawable.ic_google)).withIdentifier(8).withSelectable(false),
                new SecondaryDrawerItem().withName("Termos de Uso").withIcon(ContextCompat.getDrawable(this,R.drawable.ic_termo)).withIdentifier(9).withSelectable(false)

        );

    }

    private void initNavBar()
    {

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_indicar,null),Color.WHITE).title(getBaseContext().getString(R.string.recomendados))
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_localizacao,null),Color.WHITE).title(getBaseContext().getString(R.string.proximos))
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_new_releases_white_24dp,null),Color.WHITE).title(getBaseContext().getString(R.string.novidades))
                        .badgeTitle("with")
                        .build()
        );

        models.add(
                new NavigationTabBar.Model.Builder(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_favorite_black_24dp,null),Color.WHITE).title(getBaseContext().getString(R.string.favoritos))
                        .badgeTitle("with")
                        .build()
        );
        navigationTabBar.setModels(models);

//        navigationTabBar.setViewPager(viewPager, 2);
        navigationTabBar.setInactiveColor(Color.WHITE);
        navigationTabBar.setActiveColor(Color.BLACK);
        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.TOP);
        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        navigationTabBar.setTypeface("fonts/custom_font.ttf");
        navigationTabBar.setIsBadged(true);
        navigationTabBar.setIsTitled(true);
        navigationTabBar.setIsTinted(true);
        navigationTabBar.setIsBadgeUseTypeface(true);
        navigationTabBar.setBadgeBgColor(Color.WHITE);
        navigationTabBar.setBadgeTitleColor(Color.RED);
        navigationTabBar.setIsSwiped(true);
        navigationTabBar.setBgColor(Color.rgb(0,191,255));
        navigationTabBar.setBadgeSize(12);
        navigationTabBar.setTitleSize(15);
        navigationTabBar.setIconSizeFraction((float)0.5);
        navigationTabBar.setModelIndex(0);
    }

    private void initSearchBar()
    {
        searchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        searchBar.setTextColor(R.color.colorPrimary);
        searchBar.setHint(getApplicationContext().getText(R.string.busca_local));
        searchBar.setTextHintColor(R.color.colorPrimary);
        searchBar.setNavButtonEnabled(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onSearchStateChanged(boolean b) {

    }

    @Override
    public void onSearchConfirmed(CharSequence charSequence) {
    }

    @Override
    public void onButtonClicked(int i) {
        switch (i){
            case MaterialSearchBar.BUTTON_NAVIGATION:
//                drawer.openDrawer(Gravity.LEFT);
                searchBar.disableSearch();
                navigationDrawerLeft.openDrawer();
                break;
            case MaterialSearchBar.BUTTON_SPEECH:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
//                Toast.makeText(this,"TESTANDO",Toast.LENGTH_SHORT).show();
//                openVoiceRecognizer();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "voice recog result: " + resultCode, Toast.LENGTH_LONG).show();
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            // handleResults
            if (matches != null) {
                searchBar.enableSearch();
                searchBar.setText(matches.get(0));
//                Toast.makeText(this,matches.get(0),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClickListener(View view, int position) {

//        estabelecimento.putExtra("estabelecimento",mList.get(position));
//        Bundle b = new Bundle();
//        b.putSerializable("estabelecimento",mList.get(position));
//        estabelecimento.putExtras(b);
        startActivity(estabelecimento);
    }
}
