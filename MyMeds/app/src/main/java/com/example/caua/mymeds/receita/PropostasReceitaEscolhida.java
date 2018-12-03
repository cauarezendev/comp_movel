package com.example.caua.mymeds.receita;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.caua.mymeds.ClientDetails;
import com.example.caua.mymeds.R;

import org.w3c.dom.Text;

public class PropostasReceitaEscolhida extends AppCompatActivity {
    TextView tvObs;
    TextView tvValor;
    TextView tvEntrega;
    TextView tvTotal;
    Button btnFarmacia;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propostas_escolhida);

        tvValor = (TextView) findViewById(R.id.tvValor);
        tvValor.setText("Valor: R$ " + getIntent().getStringExtra("valor"));

        tvEntrega = (TextView) findViewById(R.id.tvEntrega);
        tvEntrega.setText("Valor da entrega: R$ " + getIntent().getStringExtra("entrega"));

        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvTotal.setText("Total: R$ " + Float.parseFloat(getIntent().getStringExtra("valor")) + Float.parseFloat(getIntent().getStringExtra("entrega")));

        tvObs = (TextView) findViewById(R.id.tvObs);
        tvObs.setText("Observações: " + getIntent().getStringExtra("obs"));

        btnFarmacia = (Button) findViewById(R.id.buttonDetalhes);
        btnFarmacia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), ClientDetails.class);
                it.putExtra("id", getIntent().getStringExtra("usuario") );
                startActivity(it);
            }
        });

    }
}
