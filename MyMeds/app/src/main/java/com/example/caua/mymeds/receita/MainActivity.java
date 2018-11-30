package com.example.caua.mymeds.receita;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.caua.mymeds.LoginActivity;
import com.example.caua.mymeds.R;
import com.example.caua.mymeds.constants.Constantes;
import com.example.caua.mymeds.forms.FormReceita;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnAdicionar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int tipo = getIntent().getIntExtra("tipo", 2);
        final List<Prescription> prescriptionsList = getAllPrescription(tipo);

        btnAdicionar = (ImageButton) findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Intent it = new Intent(MainActivity.this, FormReceita.class);
                String id = intent.getStringExtra("id");
                it.putExtra("id", id);
                startActivity(it);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        listView.setTextFilterEnabled(true);
        ArrayAdapter<Prescription> adapter = new ArrayAdapter<Prescription>(this, android.R.layout.simple_list_item_1, prescriptionsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PropostasReceita.class);

                Prescription pres = prescriptionsList.get(position);
                intent.putExtra("id", pres.getId());
                intent.putExtra("descricao", pres.getDescricao());
                intent.putExtra("obs", pres.getObs());
                intent.putExtra("proposta", pres.getProposta());
                intent.putExtra("receita", pres.getReceita());
                intent.putExtra("data", pres.getData());
                startActivity(intent);
            }
        });
    }

    public List getAllPrescription(int tipo) {
        List<Prescription>  prescriptions = new ArrayList<Prescription>();
        try {
            Constantes constantes = new Constantes();

            Request request = new Request.Builder()
                    .url(constantes.getEND_POINT() + "/receita")
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();

            JSONObject result = new JSONObject(response.body().string());

            JSONArray json = new JSONArray(result.get("result").toString());

            if (json != null) {
                if (tipo == 2) {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jAux = json.getJSONObject(i);
                        Prescription aux = new Prescription(
                                jAux.get("receita").toString(),
                                "Proposta",
                                jAux.get("data").toString(),
                                jAux.get("id").toString(),
                                jAux.get("descricao").toString(),
                                jAux.get("obs").toString());
                        prescriptions.add(aux);
                    }
                }
                else {
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jAux = json.getJSONObject(i);
                        if (jAux.get("usuario").equals(getIntent().getStringExtra("id").toString())) {
                            Prescription aux = new Prescription(
                                    jAux.get("receita").toString(),
                                    "Proposta",
                                    jAux.get("data").toString(),
                                    jAux.get("id").toString(),
                                    jAux.get("descricao").toString(),
                                    jAux.get("obs").toString());
                            prescriptions.add(aux);
                        }
                    }
                }
            }
            return prescriptions;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return prescriptions;
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
                it = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(it);
                return true;
            case R.id.nova_receita:
                it = new Intent(MainActivity.this, FormReceita.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
