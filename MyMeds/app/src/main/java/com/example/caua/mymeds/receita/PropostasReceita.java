package com.example.caua.mymeds.receita;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.caua.mymeds.LoginActivity;
import com.example.caua.mymeds.R;
import com.example.caua.mymeds.forms.FormReceita;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PropostasReceita extends AppCompatActivity {
    private TextView tvReceita;
    private TextView tvData;
    private ImageButton btnEditar;
    private ListView lvPropostas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propostas_receita);

        tvReceita = (TextView) findViewById(R.id.tvReceita);
        tvData = (TextView) findViewById(R.id.tvData);

        tvReceita.setText("Receita: "  + getIntent().getStringExtra("descricao"));

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        Date date = new Date();
        try {
            date = df.parse(getIntent().getStringExtra("data"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        TimeZone tzs = TimeZone.getTimeZone("UTC");
        DateFormat dfs = new SimpleDateFormat("dd/MM/yy");
        dfs.setTimeZone(tzs);
        tvData.setText("Data: " + dfs.format(date));

        btnEditar = (ImageButton) findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetalhesReceita.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                intent.putExtra("descricao", getIntent().getStringExtra("descricao"));
                intent.putExtra("obs", getIntent().getStringExtra("obs"));
                intent.putExtra("proposta", getIntent().getStringExtra("proposta"));
                intent.putExtra("receita", getIntent().getStringExtra("receita"));
                intent.putExtra("data", getIntent().getStringExtra("data"));

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it;

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sair:
                it = new Intent(PropostasReceita.this, LoginActivity.class);
                startActivity(it);
                return true;
            case R.id.inicio:
                it = new Intent(PropostasReceita.this, MainActivity.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
