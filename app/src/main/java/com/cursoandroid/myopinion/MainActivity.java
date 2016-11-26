package com.cursoandroid.myopinion;

import com.cursoandroid.myopinion.database.EstabelecimentoDAO;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cursoandroid.myopinion.adapter.EstabelecimentoAdapter;
import com.cursoandroid.myopinion.adapter.RecyclerViewOnClickListenerHack;
import com.cursoandroid.myopinion.database.FavoritoDAO;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener,RecyclerViewOnClickListenerHack {

    private final int CADASTRAR_LOCAL = 2;
    private final int ID_ACCOUNT_FACEBOOK = 9999;
    private final int VOICE_RECOGNITION_REQUEST_CODE = 1;
    FloatingActionButton addEstabelecimento,verEstabelecimento;
    private Drawer navigationDrawerLeft;
    private AccountHeader headerNavigationLeft;
    private NavigationTabBar navigationTabBar;

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
    private wsTasks tasks;
    Usuario user;
    GPSController gpsController;
//    private UsuarioDAO usuarioDAO;

//    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent data = getIntent();
        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_logged), Context.MODE_PRIVATE);
        user = new Usuario();
        IProfile profile = new ProfileDrawerItem().withName("").withEmail("").withIcon(ContextCompat.getDrawable(this,R.drawable.avatar)).withIdentifier(100);
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
        gpsController = new GPSController(getApplicationContext());
        searchBar.setOnSearchActionListener(this);

        initNavBar();

        //HeaderNavigation
        headerNavigationLeft = new AccountHeaderBuilder()
                .withActivity(this)
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

                           switch((int)drawerItem.getIdentifier())
                           {
                               case 1:
                                        break;
                               case 2:
                                        startActivityForResult(adicionaLocal,CADASTRAR_LOCAL);
                                        navigationDrawerLeft.closeDrawer();
                                        break;
                               case 3:
                                        navigationDrawerLeft.closeDrawer();
                                        navigationTabBar.setModelIndex(3);
                                        break;
                               case 4:
                                       intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                       "mailto","contato@minhaopiniao.com", null));
                                       intent.putExtra(Intent.EXTRA_SUBJECT, "Contato pelo aplicativo minha opinião [ INFORME O ASSUNTO ]");
                                       intent.putExtra(Intent.EXTRA_TEXT, "Obrigado por entrar em contato, teremos o prazer em te responder o mais rápido possível.");
                                       startActivity(Intent.createChooser(intent, "Enviar email por ..."));
                                        break;
                               case 5:
                                       intent = new Intent(MainActivity.this,LoginActivity.class);
                                       writeUserSharedPreferences();
                                       startActivity(intent);
                                       LoginManager.getInstance().logOut();
                                       finish();
                                       break;
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
                new PrimaryDrawerItem().withName(getString(R.string.meus_favoritos)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_star_rate_black_18dp)).withIdentifier(3).withSelectable(false),
                new PrimaryDrawerItem().withName(getString(R.string.fale_conosco)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_question_answer_black_24dp)).withIdentifier(4).withSelectable(false),
                new PrimaryDrawerItem().withName(getString(R.string.sair)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_logout)).withIdentifier(5).withSelectable(false),
//                new DividerDrawerItem(),
                new SectionDrawerItem().withName(getString(R.string.sobre_app)),
                new SecondaryDrawerItem().withName(getString(R.string.duvida_freq)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_ask_question)).withIdentifier(6).withSelectable(false),
                new SecondaryDrawerItem().withName(getString(R.string.curta_facebook)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_curtir)).withIdentifier(7).withSelectable(false),
                new SecondaryDrawerItem().withName(getString(R.string.avalie_google)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_google)).withIdentifier(8).withSelectable(false),
                new SecondaryDrawerItem().withName(getString(R.string.termos_uso)).withIcon(ContextCompat.getDrawable(this,R.drawable.ic_termo)).withIdentifier(9).withSelectable(false)

        );


        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                switch (index)
                {
                    case 0:
                            showAllList();
                            searchBar.disableSearch();
                            break;

                    case 1:
                            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            if (statusOfGPS){showNearby(gpsController.getLatitude(),gpsController.getLongitude());}else{ Toast.makeText(getApplicationContext(), "Por favor, ligue o GPS para utilizar este recurso!", Toast.LENGTH_LONG).show();}

                            searchBar.disableSearch();
                            break;

                    case 2:
                            showNewEstabelecimentos();
                            searchBar.disableSearch();
                            break;

                    case 3:
                            showFavorites();
                            searchBar.disableSearch();
                            break;
                }
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {

            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        tasks = new wsTasks(getApplicationContext(),user,headerNavigationLeft,recyclerView,this,(ArrayList<Estabelecimento>)mList);
        if(isLogged()){ tasks.execTaskLoadUser(readIdUserSharedPreferences());}

        if(readIdUserSharedPreferences() ==  9999)
        {
            Log.d("ENTREI","SUCESSO!");
            ArrayList<IProfile> arrayList = new ArrayList<>();
            tasks.execTaskLoadImgFacebook();
            profile = new ProfileDrawerItem().withName(Profile.getCurrentProfile().getName()).withEmail("").withIcon(ContextCompat.getDrawable(this,R.drawable.avatar)).withIdentifier(100);
            arrayList.add(profile);
            headerNavigationLeft.setProfiles(arrayList);
        }


    }



    @Override
    protected void onResume() {
        super.onResume();
        gpsController.onResume();
        navigationTabBar.setModelIndex(0);
    }

    private void showAllList() { tasks.exectTaskLoadEstabelecimentos(); }

    private void showFavorites()
    {
        FavoritoDAO favoritoDAO = new FavoritoDAO(this);
        String listaFavoritos = favoritoDAO.getStringFavoritos();
        tasks.execTaskLoadFavorites(listaFavoritos);
    }

    private void showNearby(double latitude, double longitude){ tasks.execTaskLoadNearby(latitude,longitude);}

    private void showNewEstabelecimentos() { tasks.execNewEstabelecimentos(); }

    private void initNavBar()
    {

        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb);
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
        if(charSequence.length() == 0)
        {
            return;
        }else {
            ArrayList<Estabelecimento> list = new ArrayList<>();//estabelecimentoDAO.read(charSequence.toString());
            for (Estabelecimento e: mList){
                if(e.getNome().toLowerCase().contains(charSequence.toString().toLowerCase())){ list.add(e); }
            }
            EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(getApplicationContext(), list);
            adapter.setmRecyclerViewOnClickListenerHack(this);
            recyclerView.setAdapter(adapter);
            navigationTabBar.deselect();
        }
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

//        if(requestCode == CADASTRAR_LOCAL && resultCode == RESULT_OK)
//        {

//        }
    }

    @Override
    public void onClickListener(View view, int position) {
        estabelecimento.putExtra("estabelecimento",mList.get(position));
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



    @Override
    protected void onStart() {
        super.onStart();
        gpsController.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gpsController.onStop();
    }


    @Override
    protected void onPause() {
        super.onPause();
        gpsController.onPause();
    }
}