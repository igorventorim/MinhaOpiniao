package com.cursoandroid.myopinion;

import com.cursoandroid.myopinion.database.EstabelecimentoDAO;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cursoandroid.myopinion.adapter.EstabelecimentoAdapter;
import com.cursoandroid.myopinion.adapter.RecyclerViewOnClickListenerHack;
import com.cursoandroid.myopinion.database.UsuarioDAO;
import com.cursoandroid.myopinion.domain.Estabelecimento;
import com.cursoandroid.myopinion.domain.Usuario;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.github.clans.fab.FloatingActionButton;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener,RecyclerViewOnClickListenerHack {

    private final int CADASTRAR_LOCAL = 2;
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
    SharedPreferences sharedPref;
    private final int DEFAULT_INVALID_ID = -1;
    EstabelecimentoDAO estabelecimentoDAO;
    private UsuarioDAO usuarioDAO;

//    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent data = getIntent();
        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_logged), Context.MODE_PRIVATE);
//        final Usuario user = new Usuario(getApplicationContext());
        usuarioDAO = new UsuarioDAO(getApplicationContext());
        if(isLogged()){ usuarioDAO.read(readIdUserSharedPreferences());}
        estabelecimentoDAO = new EstabelecimentoDAO(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.rv_estabelecimentos);
        addEstabelecimento = (FloatingActionButton) findViewById(R.id.adicionar_loja);
        initSearchBar();
        adicionaLocal = new Intent(this,AdicionaLocalAcitivity.class);
        estabelecimento = new Intent(this,EstabelecimentoActivity.class);
        addEstabelecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(adicionaLocal,CADASTRAR_LOCAL);
            }
        });

        searchBar.setOnSearchActionListener(this);

        initNavBar();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mList = estabelecimentoDAO.getEstabelecimentos();
        EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(getApplicationContext(),mList);
        adapter.setmRecyclerViewOnClickListenerHack(this);
        recyclerView.setAdapter(adapter);
        IProfile profile;
        final Usuario user = usuarioDAO.getUsuario();
        if(user.getId() != 0)
        {
            profile = new ProfileDrawerItem().withName(usuarioDAO.getUsuario().getNome()).withEmail(user.getEmail()).withIcon(user.getFotoBitmap()).withIdentifier(100);
        }
        else{profile = new ProfileDrawerItem().withName(Profile.getCurrentProfile().getName()).withEmail("").withIcon(ContextCompat.getDrawable(this,R.drawable.avatar)).withIdentifier(100);}

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
                        if(drawerItem != null)
                        {
                            Intent intent = null;
                            if(drawerItem.getIdentifier() == 5)
                            {
                                intent = new Intent(MainActivity.this,LoginActivity.class);
                                writeUserSharedPreferences();
                                startActivity(intent);
                                LoginManager.getInstance().logOut();
                                finish();
                            }else if(drawerItem.getIdentifier() == 2)
                            {
                                startActivityForResult(adicionaLocal,CADASTRAR_LOCAL);
                                navigationDrawerLeft.closeDrawer();
                            }


                        }

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
                new PrimaryDrawerItem().withName(getString(R.string.minha_conta)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_account_circle_black_24dp)).withIdentifier(1).withSelectable(false),
                new PrimaryDrawerItem().withName(getString(R.string.add_estabelecimento)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_playlist_add_black_24dp)).withIdentifier(2).withSelectable(false),
                new PrimaryDrawerItem().withName(getString(R.string.meus_estabelecimentos)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_star_rate_black_18dp)).withIdentifier(3).withSelectable(false),
                new PrimaryDrawerItem().withName(getString(R.string.fale_conosco)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_question_answer_black_24dp)).withIdentifier(4).withSelectable(false),
                new PrimaryDrawerItem().withName(getString(R.string.sair)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_logout)).withIdentifier(5).withSelectable(false),
//                new DividerDrawerItem(),
                new SectionDrawerItem().withName(getString(R.string.sobre_app)),
                new SecondaryDrawerItem().withName(getString(R.string.duvida_freq)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_ask_question)).withIdentifier(6).withSelectable(false),
                new SecondaryDrawerItem().withName(getString(R.string.curta_facebook)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_curtir)).withIdentifier(7).withSelectable(false),
                new SecondaryDrawerItem().withName(getString(R.string.avalie_google)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_google)).withIdentifier(8).withSelectable(false),
                new SecondaryDrawerItem().withName(getString(R.string.termos_uso)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_termo)).withIdentifier(9).withSelectable(false)

        );

    }



    @Override
    protected void onResume() {
        super.onResume();
//        estabelecimentoDAO.read((int)estabelecimentoDAO.getEstabelecimento().getId());
//        mList = estabelecimentoDAO.getEstabelecimentos();
        mList = estabelecimentoDAO.getEstabelecimentos();
//        Toast.makeText(getApplicationContext(),"Size:"+estabelecimentoDAO.getEstabelecimentos().size(),Toast.LENGTH_LONG).show();
        EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(getApplicationContext(),estabelecimentoDAO.getEstabelecimentos());
        adapter.setmRecyclerViewOnClickListenerHack(this);
        recyclerView.setAdapter(adapter);
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
//        navigationTabBar.setTypeface("fonts/custom_font.ttf");
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

        if(requestCode == CADASTRAR_LOCAL && resultCode == RESULT_OK)
        {
//            mList = estabelecimentoDAO.getEstabelecimentos();
//            Toast.makeText(getApplicationContext(),"Size:"+estabelecimentoDAO.getEstabelecimentos().size(),Toast.LENGTH_LONG).show();
//            EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(getApplicationContext(),estabelecimentoDAO.getEstabelecimentos());
//            adapter.setmRecyclerViewOnClickListenerHack(this);
//            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClickListener(View view, int position) {

//        estabelecimento.putExtra("estabelecimento",mList.get(position));
//        Bundle b = new Bundle();
//        b.putSerializable("estabelecimento",mList.get(position));
//        estabelecimento.putExtras(b);
        estabelecimento.putExtra("estabelecimento",mList.get(position));
//        Toast.makeText(getApplicationContext(),""+mList.get(position+1),Toast.LENGTH_LONG).show();
        startActivity(estabelecimento);
    }

    /**
     * Salvando estado de logado do usuário
     */
    private void writeUserSharedPreferences()
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.saved_logged),false);
        editor.putInt(getString(R.string.user_id),DEFAULT_INVALID_ID);
        editor.commit();
    }

    /**
     * Recuperar identificador de usuário que está logado
     * @return id do usuário
     */
    private int readIdUserSharedPreferences()
    {
        return sharedPref.getInt(getString(R.string.user_id),DEFAULT_INVALID_ID);
    }

    private boolean isLogged()
    {
        return sharedPref.getBoolean(getString(R.string.saved_logged),false);
    }

}
