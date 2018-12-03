package com.example.caua.mymeds;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.caua.mymeds.constants.Constantes;
import com.example.caua.mymeds.forms.FormReceita;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.Response;

public class ClientDetails extends AppCompatActivity {
    TextView tvNome;
    TextView tvEndereço;
    TextView tvTelefone;
    TextView tvNumero;
    TextView tvBairro;
    TextView tvCEP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        try {
            JSONObject json = getUser();

            tvNome = (TextView) findViewById(R.id.tvNome);
            tvNome.setText("Nome: " + json.get("razao_social").toString());

            tvEndereço = (TextView) findViewById(R.id.tvRua);
            tvEndereço.setText("Rua: " + json.get("endereco_rua").toString());

            tvNumero = (TextView) findViewById(R.id.tvNumero);
            tvNumero.setText("Número: " + json.get("endereco_num").toString());

            tvBairro = (TextView) findViewById(R.id.tvBairro);
            tvBairro.setText("Bairro: " + json.get("endereco_bairro").toString());

            tvCEP = (TextView) findViewById(R.id.tvCEP);
            tvCEP.setText("CEP: " + json.get("cep").toString());

            tvTelefone = (TextView) findViewById(R.id.tvTelefone);
            tvTelefone.setText("Phone: " + json.get("telefone".toString()));
        }
        catch (JSONException e) {
            Log.e("JSON", e.toString());
        }
    }

    public JSONObject getUser() {
        try {
            Constantes constantes = new Constantes();

            Request request = new Request.Builder()
                    .url(constantes.getEND_POINT() + "/user/" + getIntent().getStringExtra("id"))
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();

            JSONObject result = new JSONObject(response.body().string());

            JSONObject json = new JSONObject(result.get("result").toString());

            return json;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
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
                it = new Intent(ClientDetails.this, LoginActivity.class);
                startActivity(it);
                finish();
                return true;
            case R.id.receitas_titulo:
                it = new Intent(ClientDetails.this, FormReceita.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
