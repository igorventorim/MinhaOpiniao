package com.cursoandroid.myopinion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvaliacaoActivity extends AppCompatActivity {


    private MaterialSpinner atendimento,conforto,qualidade,custo,retornar,indicarAmigo;
    private ImageButton backAvaliacao;
    private Button avaliar;
    private CircleImageView imgEstabelecimento;

    private List<String> itens = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);

        avaliar = (Button) findViewById(R.id.bt_avaliar);
        imgEstabelecimento = (CircleImageView) findViewById(R.id.profile_image);

        imgEstabelecimento.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.barteste, 200, 200));

        initSpinners();

        backAvaliacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });
    }

    /**
     * MÉTODO RESPONSÁVEL PELA INICIALIZAÇÃO DOS SPINNERS
     */
    private void initSpinners()
    {
        atendimento = (MaterialSpinner) findViewById(R.id.spinAtendimento);
        conforto = (MaterialSpinner) findViewById(R.id.spinConforto);
        qualidade = (MaterialSpinner) findViewById(R.id.spinQualidade);
        custo = (MaterialSpinner) findViewById(R.id.spinCusto);
        retornar = (MaterialSpinner) findViewById(R.id.spinRetornaria);
        indicarAmigo = (MaterialSpinner) findViewById(R.id.spinIndicarAmigo);
        backAvaliacao = (ImageButton) findViewById(R.id.back_avaliacao);

        atendimento.setItems(getResources().getStringArray(R.array.atributos_avaliacao));
        conforto.setItems(getResources().getStringArray(R.array.atributos_avaliacao));
        qualidade.setItems(getResources().getStringArray(R.array.atributos_avaliacao));
        custo.setItems(getResources().getStringArray(R.array.questao_custo));
        retornar.setItems(getResources().getStringArray(R.array.questao_objetiva));
        indicarAmigo.setItems(getResources().getStringArray(R.array.questao_objetiva));

        atendimento.setText("");
        conforto.setText("");
        qualidade.setText("");
        custo.setText("");
        retornar.setText("");
        indicarAmigo.setText("");
    }



}
