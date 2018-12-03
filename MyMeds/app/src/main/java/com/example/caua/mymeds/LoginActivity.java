package com.example.caua.mymeds;


import android.content.Intent;

import android.graphics.PorterDuff;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caua.mymeds.constants.Constantes;
import com.example.caua.mymeds.farmacia.MainPharmacyActivity;
import com.example.caua.mymeds.forms.FormCustomer;
import com.example.caua.mymeds.receita.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText editLogin;
    private EditText editPassword;
    private TextView textRegister;
    private Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editLogin = (EditText) findViewById(R.id.editLogin);
        editLogin.getBackground().setColorFilter(000000, PorterDuff.Mode.SRC_IN);
        editPassword = (EditText) findViewById(R.id.editPassword);
        textRegister = (TextView) findViewById(R.id.textRegister);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    String login = editLogin.getText().toString();
                    String password = editPassword.getText().toString();

                    json.put("email", login);
                    json.put("senha", password);

                    if (login.isEmpty() || password.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Preencha os campos login e/ou senha para efetuar o login", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        JSONObject res = post(json);
                        if (res.getString("login").equals("true")) {
                            JSONObject user = new JSONObject(res.get("user").toString());
                            Toast.makeText(getApplicationContext(), "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                            int tipo = Integer.parseInt(user.get("tipo").toString());
                                if (tipo == 1) {
                                    Intent it = new Intent(getApplicationContext(), MainActivity.class);

                                    String id = user.get("id").toString();
                                    it.putExtra("id", id);
                                    it.putExtra("tipo", tipo);

                                    startActivity(it);
                                    finish();
                                }
                                else {
                                    Intent it = new Intent(getApplicationContext(), MainPharmacyActivity.class);

                                    String id = user.get("id").toString();
                                    it.putExtra("id", id);
                                    it.putExtra("tipo", tipo);

                                    startActivity(it);
                                    finish();
                                }
                        } else {
                            Toast.makeText(getApplicationContext(), "Email ou senha está incorreto", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), FormCustomer.class);
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
                    .url(constantes.getEND_POINT() + "/login")
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
            Log.e("Your tag", "Error", e);
        }
        JSONObject json = new JSONObject();
        return json;
    }
}
