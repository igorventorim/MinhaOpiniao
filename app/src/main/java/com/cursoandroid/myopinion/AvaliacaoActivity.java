package com.cursoandroid.myopinion;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.myopinion.database.EstabelecimentoDAO;
import com.cursoandroid.myopinion.domain.Estabelecimento;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvaliacaoActivity extends AppCompatActivity {

    private final String ESTABELECIMENTO = "estabelecimento";
    private MaterialSpinner atendimento,conforto,qualidade,custo,retornar,indicarAmigo;
    private ImageButton backAvaliacao;
    private Button avaliar;
    private CircleImageView imgEstabelecimento;
    private TextView tvNameStore;
    private EstabelecimentoDAO estabelecimentoDAO;

    private List<String> itens = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);
        estabelecimentoDAO = new EstabelecimentoDAO(this);
        final Intent i = getIntent();
        final Estabelecimento e = (Estabelecimento) i.getSerializableExtra(ESTABELECIMENTO);
        avaliar = (Button) findViewById(R.id.bt_avaliar);
        imgEstabelecimento = (CircleImageView) findViewById(R.id.profile_image);
        tvNameStore = (TextView) findViewById(R.id.name_store);

        imgEstabelecimento.setImageBitmap(e.getFotoBitmap());
        tvNameStore.setText(e.getNome());

        initSpinners();

        backAvaliacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });

        avaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estabelecimentoDAO.read((int) e.getId());
//                Toast.makeText(getApplicationContext(),estabelecimentoDAO.getEstabelecimento().getNome()+":"+estabelecimentoDAO.getEstabelecimento().getRating(),Toast.LENGTH_SHORT).show();
                estabelecimentoDAO.getEstabelecimento().calculaNota(atendimento.getSelectedIndex(),conforto.getSelectedIndex(),qualidade.getSelectedIndex(),custo.getSelectedIndex(),retornar.getSelectedIndex(),indicarAmigo.getSelectedIndex());
//                Toast.makeText(getApplicationContext(),estabelecimentoDAO.getEstabelecimento().getNome()+":"+estabelecimentoDAO.getEstabelecimento().getRating(),Toast.LENGTH_SHORT).show();
                estabelecimentoDAO.update();
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

//        atendimento.setText("");
//        conforto.setText("");
//        qualidade.setText("");
//        custo.setText("");
//        retornar.setText("");
//        indicarAmigo.setText("");
    }



}
