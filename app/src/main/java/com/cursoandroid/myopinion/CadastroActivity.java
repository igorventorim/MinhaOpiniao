package com.cursoandroid.myopinion;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class CadastroActivity extends AppCompatActivity {


    private ImageButton backCadastro;
    private EditText etDataNasc,etCEP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        backCadastro = (ImageButton) findViewById(R.id.back_cadastro);

        backCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etDataNasc = (EditText) findViewById(R.id.nascimento);
        etCEP = (EditText) findViewById(R.id.cep);

        etCEP.addTextChangedListener(Mask.insert("#####-###",etCEP));
        etDataNasc.addTextChangedListener(Mask.insert("##/##/####",etDataNasc));

    }
}
