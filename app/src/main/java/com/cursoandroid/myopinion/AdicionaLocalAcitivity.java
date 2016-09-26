package com.cursoandroid.myopinion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import info.hoang8f.android.segmented.SegmentedGroup;

public class AdicionaLocalAcitivity extends AppCompatActivity {


    private ImageButton btVoltar;
    private MaterialSpinner spinAreaComercial,spinEstado;
    private LinearLayout layoutInserirLocalizacao;
    private Button rbGps,rbInserir;
    private TextView tvInserir, tvGps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_local_acitivity);
        layoutInserirLocalizacao = (LinearLayout) findViewById(R.id.form_inserir_localizacao);

        rbInserir = (Button) findViewById(R.id.inserir_localizacao);
        rbGps= (Button) findViewById(R.id.gps);
        btVoltar = (ImageButton) findViewById(R.id.back_add_local);
        spinAreaComercial = (MaterialSpinner) findViewById(R.id.spin_area_comercial);
        spinEstado = (MaterialSpinner) findViewById(R.id.spin_estado);
        tvInserir = (TextView) findViewById(R.id.tv_dados_inserir);
        tvGps = (TextView) findViewById(R.id.tv_dados_gps);

        spinEstado.setItems(getResources().getStringArray(R.array.list_estados));
        spinAreaComercial.setItems(getResources().getStringArray(R.array.list_areas));
        spinEstado.setText("");
        spinAreaComercial.setText("");

        layoutInserirLocalizacao.setVisibility(View.GONE);
        tvInserir.setVisibility(View.GONE);
        tvGps.setVisibility(View.GONE);

        rbGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutInserirLocalizacao.setVisibility(View.GONE);
                tvInserir.setVisibility(View.GONE);
                tvGps.setVisibility(View.VISIBLE);
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


        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
