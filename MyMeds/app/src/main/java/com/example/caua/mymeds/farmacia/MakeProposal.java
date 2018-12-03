package com.example.caua.mymeds.farmacia;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caua.mymeds.ClientDetails;
import com.example.caua.mymeds.LoginActivity;
import com.example.caua.mymeds.R;
import com.example.caua.mymeds.constants.Constantes;
import com.example.caua.mymeds.forms.FormReceita;
import com.example.caua.mymeds.receita.MainActivity;
import com.example.caua.mymeds.receita.Prescription;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MakeProposal extends AppCompatActivity {
    TextView etReceita;
    EditText etObs;
    EditText etValor;
    EditText etEntrega;
    Button btnProposta;
    ImageButton btnDetalhes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_proposal);

        etReceita = (TextView) findViewById(R.id.etReceita);
        etReceita.setText("Receita: " + getIntent().getStringExtra("descricao"));

        etObs = (EditText) findViewById(R.id.etObs);
        etValor = (EditText) findViewById(R.id.etValor);
        etEntrega = (EditText) findViewById(R.id.etEntrega);

        btnProposta = (Button) findViewById(R.id.btnProposta);
        btnProposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float valor = Float.parseFloat(etValor.getText().toString());
                Float entrega = Float.parseFloat(etEntrega.getText().toString());
                String obs = etObs.getText().toString();
                Boolean teste = true;
                StringBuilder vazio = new StringBuilder();

                if (valor < 0) {
                    vazio.insert(0, " Valor da receita");
                    teste = false;
                }
                if (entrega < 0) {
                    vazio.insert(0, " Valor da entrega");
                    teste = false;
                }
                if (obs.isEmpty()) {
                    vazio.insert(0, " Observação");
                    teste = false;
                }

                if (teste) {
                    try {
                        String id = getIntent().getStringExtra("id");
                        String userId = getIntent().getStringExtra("user_id");

                        JSONObject data = new JSONObject();
                        data.put("user_id", userId);
                        data.put("receita_id", id);
                        data.put("valor", valor);
                        data.put("entrega", entrega);
                        data.put("obs", obs);
                        JSONObject json = post(data);

                        if(json != null) {
                            Toast.makeText(getApplicationContext(), "A proposta foi feita", Toast.LENGTH_SHORT);
                            Intent it = new Intent(getApplicationContext(), MainPharmacyActivity.class);
                            startActivity(it);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Não foi possível fazer a proposta", Toast.LENGTH_SHORT);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Os campo(s)" + vazio.toString() + " são obrigatório(s)", Toast.LENGTH_SHORT);
                }
            }
        });

        btnDetalhes = (ImageButton) findViewById(R.id.btnDetalhes);
        btnDetalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), ClientDetails.class);
                it.putExtra("id", getIntent().getStringExtra("user_id"));
                startActivity(it);
            }
        });
    }

    public JSONObject post(final JSONObject data) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Constantes constantes = new Constantes();

            final RequestBody body = RequestBody.create(MediaType.parse("application/json"), data.toString());
            final Request request = new Request.Builder()
                    .url(constantes.getEND_POINT() + "/proposta")
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .build();

            final OkHttpClient client = new OkHttpClient();
            final Response response = client.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            JSONObject teste = new JSONObject(json.get("result").toString());
            return teste;
        }
        catch (Exception e) {
            Log.e("MAKEPROPOSAL", "Error", e);
        }
        JSONObject json = new JSONObject();
        return json;
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
                it = new Intent(MakeProposal.this, LoginActivity.class);
                startActivity(it);
                return true;
            case R.id.inicio:
                it = new Intent(MakeProposal.this, MainActivity.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
