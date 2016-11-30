package com.cursoandroid.myopinion;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wsTasks tasks = new wsTasks(getApplicationContext());
                tasks.execTaskForgotPassword(email.getText().toString());
                Log.d("email", String.valueOf(email.getText()));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.recup_senha), Toast.LENGTH_LONG).show();
                email.setText("");
            }
        });

    }
}
