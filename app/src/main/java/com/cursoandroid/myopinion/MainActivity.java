package com.cursoandroid.myopinion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addEstabelecimento,verEstabelecimento;
    Intent adicionaLocal,estabelecimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEstabelecimento = (FloatingActionButton) findViewById(R.id.adicionar_loja);
        verEstabelecimento = (FloatingActionButton) findViewById(R.id.visualizar_estabelecimento);

        adicionaLocal = new Intent(this,AdicionaLocalAcitivity.class);
        estabelecimento = new Intent(this,EstabelecimentoActivity.class);

        addEstabelecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(adicionaLocal);
            }
        });

        verEstabelecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(estabelecimento);
            }
        });
    }
}
