package com.cursoandroid.myopinion;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cursoandroid.myopinion.domain.UsuarioDAO;

import java.util.ArrayList;

public class CadastroActivity extends AppCompatActivity {


    private ImageButton backCadastro;
    private Button btCadastrar;
    private EditText etDataNasc,etCEP,etNome,etEmail,etSenha,etConfirmaSenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Intent dados = getIntent();


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
        etCEP = (EditText) findViewById(R.id.cep);
        etNome = (EditText) findViewById(R.id.nome);
        etEmail = (EditText) findViewById(R.id.email);
        etConfirmaSenha = (EditText) findViewById(R.id.conf_senha);
        etSenha = (EditText) findViewById(R.id.senha) ;
        etEmail.setText(dados.getStringExtra("email"));
        etSenha.setText(dados.getStringExtra("senha"));
//        etConfirmaSenha.setText(dados.getStringExtra("senha")); // PREENCHER AUTOMATICAMENTE CAMPO CONFIRMA SENHA???
        btCadastrar = (Button) findViewById(R.id.bt_cadastrar);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(verificaDados()) {
                   UsuarioDAO user = new UsuarioDAO(getApplicationContext());
                   user.open("write");
                   user.put(etNome.getText().toString(), etEmail.getText().toString(), etCEP.getText().toString(), etDataNasc.getText().toString(), etSenha.getText().toString());
                   user.close();
                   Toast.makeText(getApplicationContext(), getResources().getString(R.string.usuario_cadastrado), Toast.LENGTH_LONG).show();
                   finish();
               }

            }
        });

    }

    private boolean verificaDados()
    {
        if(!(etEmail.getText().toString().contains("@")))
        {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.email_invalido), Toast.LENGTH_LONG).show();
            return false;
        }

        if(!(etSenha.getText().toString().equals(etConfirmaSenha.getText().toString())))
        {
            etSenha.setText("");
            etConfirmaSenha.setText("");
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.senhas_diferentes), Toast.LENGTH_LONG).show();
            return false;
        }

        if(etSenha.getText().toString().length() < 4)
        {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.senha_pequena), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

}
