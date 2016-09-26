package com.cursoandroid.myopinion;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private EditText email;
    private Button btEnviar;
    private ImageButton backRecupSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        email = (EditText) findViewById(R.id.etEmail_recup_senha);
        btEnviar = (Button) findViewById(R.id.btEnviar_recup_senha);
        backRecupSenha = (ImageButton) findViewById(R.id.back_recup_senha);

        backRecupSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
