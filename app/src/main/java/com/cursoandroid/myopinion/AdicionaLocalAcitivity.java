package com.cursoandroid.myopinion;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.myopinion.database.EstabelecimentoDAO;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class AdicionaLocalAcitivity extends AppCompatActivity {

    private static final int RESULT_CAMERA = 1;
    private static final int RESULT_GALERIA = 2;

    private File imgEstabelecimento;
    private ImageButton btVoltar;
    private Button btCadastrar;
    private MaterialSpinner spinAreaComercial, spinEstado;
    private LinearLayout layoutInserirLocalizacao;
    private RadioButton rbGps, rbInserir;
    private RadioButton rbProp, rbFunc, rbClie;
    private TextView tvInserir, tvGps, imgArmazenada;
    private EditText etNomeEstabelecimento, etCidade, etBairro;
    private ImageButton ibGaleria, ibCamera;
    private CheckBox cbNotificacao;
    private Bitmap foto;
    GPSController gpsController;
    private EstabelecimentoDAO estabelecimentoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        estabelecimentoDAO = new EstabelecimentoDAO(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_local_acitivity);
        layoutInserirLocalizacao = (LinearLayout) findViewById(R.id.form_inserir_localizacao);
        rbInserir = (RadioButton) findViewById(R.id.inserir_localizacao);
        rbGps = (RadioButton) findViewById(R.id.gps);
        btVoltar = (ImageButton) findViewById(R.id.back_add_local);
        imgArmazenada = (TextView) findViewById(R.id.tv_imagem_armazenada);
        spinAreaComercial = (MaterialSpinner) findViewById(R.id.spin_area_comercial);
        rbProp = (RadioButton) findViewById(R.id.radio_proprietario);
        rbClie = (RadioButton) findViewById(R.id.radio_cliente);
        rbFunc = (RadioButton) findViewById(R.id.radio_funcionario);
        spinEstado = (MaterialSpinner) findViewById(R.id.spin_estado);
        tvInserir = (TextView) findViewById(R.id.tv_dados_inserir);
        tvGps = (TextView) findViewById(R.id.tv_dados_gps);
        etNomeEstabelecimento = (EditText) findViewById(R.id.nome_estabelecimento);
        etCidade = (EditText) findViewById(R.id.et_cidade);
        etBairro = (EditText) findViewById(R.id.et_bairro);
        ibGaleria = (ImageButton) findViewById(R.id.ib_galeria);
        ibCamera = (ImageButton) findViewById(R.id.ib_camera);
        cbNotificacao = (CheckBox) findViewById(R.id.cb_notificacoes);
        btCadastrar = (Button) findViewById(R.id.bt_add_estabelecimento);
        gpsController = new GPSController(getApplicationContext());
        spinEstado.setItems(getResources().getStringArray(R.array.list_estados));
        spinAreaComercial.setItems(getResources().getStringArray(R.array.list_areas));
        spinEstado.setText("");
        spinAreaComercial.setText("");

        rbGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutInserirLocalizacao.setVisibility(View.GONE);
                tvInserir.setVisibility(View.GONE);
                tvGps.setVisibility(View.VISIBLE);
                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,);
                boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!statusOfGPS) {
                    rbInserir.setChecked(true);
                    tvInserir.setVisibility(View.VISIBLE);
                    tvGps.setVisibility(View.GONE);
                    layoutInserirLocalizacao.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Por favor, ligue o GPS para utilizar este recurso!", Toast.LENGTH_LONG).show();
                } else {


//                    Toast.makeText(getApplicationContext(), "Cidade: "+gpsController.getCidade(getApplicationContext()), Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), "Estado: "+gpsController.getEstado(getApplicationContext()), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Latitute: "+String.valueOf(gpsController.getLatitude()), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Longitude: "+String.valueOf(gpsController.getLongitude()), Toast.LENGTH_LONG).show();


                }

            }
        });

        rbInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutInserirLocalizacao.setVisibility(View.VISIBLE);
                tvInserir.setVisibility(View.VISIBLE);
                tvGps.setVisibility(View.GONE);
            }
        });

        ibCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File picsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES.toString());
                imgEstabelecimento = new File(picsDir,"foto.jpg");
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgEstabelecimento));
                startActivityForResult(i,RESULT_CAMERA);
            }
        });

        ibGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(i,getString(R.string.select_local_img)),RESULT_GALERIA);
            }
        });


        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cadastraEstabelecimento();
            }
        });

    }


    public void cadastraEstabelecimento()
    {
        if(foto == null)
            return;
        final String nome = etNomeEstabelecimento.getText().toString();
        final String tipo = getResources().getStringArray(R.array.list_areas)[spinAreaComercial.getSelectedIndex()];
        final String estado = getResources().getStringArray(R.array.list_estados)[spinEstado.getSelectedIndex()];
        final String cidade = etCidade.getText().toString();
        final String bairro = etBairro.getText().toString();
        boolean notificacoes = cbNotificacao.isChecked();
//        final Bitmap img = foto;
        String responsavel = "";
        if(rbFunc.isChecked())
        {
            responsavel = "Funcionario";
        }else if(rbProp.isChecked())
        {
            responsavel = "Proprietario";
        }else if(rbClie.isChecked()){
            responsavel = "Cliente";
        }else
            return;

        estabelecimentoDAO.put(nome,tipo,estado,cidade,bairro,notificacoes,foto,responsavel);
        Toast.makeText(getApplicationContext(),"Estabelecimento cadastrado com sucesso!",Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();

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
    protected void onResume() {
        super.onResume();
        gpsController.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gpsController.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case RESULT_CAMERA:
                if(resultCode == RESULT_OK) {
                    Bitmap mImageBitmap = BitmapFactory.decodeFile(String.valueOf(imgEstabelecimento));
//                    foto = Bitmap.createScaledBitmap(mImageBitmap, (int)(mImageBitmap.getWidth()*0.3),(int)(mImageBitmap.getHeight()*0.3), true);
                    foto = mImageBitmap;
                    imgArmazenada.setVisibility(View.VISIBLE);
                }
                break;
            case RESULT_GALERIA:
                if(resultCode == RESULT_OK)
                {
                    Uri selectedImageUri = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImageUri,filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    foto = BitmapFactory.decodeFile(picturePath);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    foto.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    foto = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                    setresult.putExtra("BMP",bytes);

                    imgArmazenada.setVisibility(View.VISIBLE);
                }
        }
    }


}
