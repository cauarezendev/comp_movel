package com.example.caua.mymeds.receita;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.caua.mymeds.LoginActivity;
import com.example.caua.mymeds.R;
import com.example.caua.mymeds.constants.Constantes;
import com.example.caua.mymeds.farmacia.MakeProposal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PropostasReceita extends AppCompatActivity {
    private TextView tvReceita;
    private TextView tvData;
    private ImageButton btnEditar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propostas_receita);

        final List<Proposes> proposesList = getAllProposes();

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


        listView = (ListView) findViewById(R.id.propostasView);
        listView.setTextFilterEnabled(true);
        ArrayAdapter<Proposes> adapter = new ArrayAdapter<Proposes>(this, android.R.layout.simple_list_item_1, proposesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PropostasReceitaEscolhida.class);
                Proposes pres = proposesList.get(position);
                intent.putExtra("receita_id", pres.getIdReceita());
                intent.putExtra("id", pres.getIdProposta());
                intent.putExtra("usuario", pres.getIdUsuario());
                intent.putExtra("entrega", pres.getEntrega());
                intent.putExtra("valor", pres.getValor());
                intent.putExtra("obs", pres.getObs());
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


    public List getAllProposes() {
        List<Proposes> proposes = new ArrayList<Proposes>();
        try {
            Constantes constantes = new Constantes();
            Log.e("URL", constantes.getEND_POINT() + "/proposta/" + getIntent().getStringExtra("id"));
            Request request = new Request.Builder()
                    .url(constantes.getEND_POINT() + "/proposta/" + getIntent().getStringExtra("id"))
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();

            JSONObject result = new JSONObject(response.body().string());

            JSONArray json = new JSONArray(result.get("result").toString());

            if (json != null) {
                for (int i = 0; i < json.length(); i++) {
                    JSONObject jAux = json.getJSONObject(i);
                    Proposes aux = new Proposes(
                            jAux.get("receita").toString(),
                            jAux.get("id").toString(),
                            jAux.get("usuario").toString(),
                            jAux.get("valor").toString(),
                            jAux.get("entrega").toString(),
                            jAux.get("obs").toString());
                    proposes.add(aux);
                }
            }

            return proposes;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return proposes;
    }
}
