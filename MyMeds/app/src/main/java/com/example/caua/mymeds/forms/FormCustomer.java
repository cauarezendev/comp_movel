package com.example.caua.mymeds.forms;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caua.mymeds.LoginActivity;
import com.example.caua.mymeds.R;
import com.example.caua.mymeds.constants.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FormCustomer extends AppCompatActivity {
    private EditText editNome;
    private EditText editRua;
    private EditText editCep;
    private EditText editBairro;
    private EditText editNumero;
    private EditText editSenha;
    private EditText editEmail;
    private EditText editConfirma;
    private Button cadastrar;
    private Switch chavinha;
    private TextView textMudaTipo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_customer);

        editNome = (EditText) findViewById(R.id.editName);
        editRua = (EditText) findViewById(R.id.editRua);
        editCep = (EditText) findViewById(R.id.editCep);
        editBairro = (EditText) findViewById(R.id.editBairro);
        editNumero = (EditText) findViewById(R.id.editNumero);
        editSenha = (EditText) findViewById(R.id.editSenha);
        editConfirma = (EditText) findViewById(R.id.editConfirmarSenha);
        editEmail = (EditText) findViewById(R.id.editEmail);
        cadastrar = (Button) findViewById(R.id.btnCadastrar);
        chavinha = (Switch) findViewById(R.id.chavinha);
        textMudaTipo = (TextView) findViewById(R.id.textMudaTipo);

        chavinha.setTextOn("");
        chavinha.setTextOff("");
        chavinha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textMudaTipo.setText("");
                    textMudaTipo.append("Farmácia");
                }
                else {
                    textMudaTipo.setText("");
                    textMudaTipo.append("Cliente");
                }
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String telefone = tm.getLine1Number();

                    String nome = editNome.getText().toString();
                    String rua = editRua.getText().toString();
                    String cep = editCep.getText().toString();
                    String bairro = editBairro.getText().toString();
                    String numero = editNumero.getText().toString();
                    String senha = editSenha.getText().toString();
                    String confirma = editConfirma.getText().toString();
                    String email = editEmail.getText().toString();
                    Boolean teste = true;

                    StringBuilder vazio = new StringBuilder();

                    if (nome.isEmpty()) {
                        vazio.insert(0," Nome/Razão Social");
                        teste = false;
                    }
                    if (rua.isEmpty()) {
                        vazio.insert(0," Rua");
                        teste = false;
                    }
                    if (cep.isEmpty()) {
                        vazio.insert(0," CEP");
                        teste = false;
                    }
                    if (bairro.isEmpty()) {
                        vazio.insert(0," Bairro");
                        teste = false;
                    }
                    if (numero.isEmpty()) {
                        vazio.insert(0," Numero");
                        teste = false;
                    }
                    if (senha.isEmpty()) {
                        vazio.insert(0, " Senha");
                        teste = false;
                    }
                    if (confirma.isEmpty()) {
                        vazio.insert(0," Confirma Senha");
                        teste = false;
                    }
                    if (email.isEmpty()) {
                        vazio.insert(0, " E-mail");
                        teste = false;
                    }

                    if (teste) {
                        if (senha.equals(confirma)) {
                            JSONObject json = new JSONObject();
                            json.put("razao_social", nome);
                            json.put("endereco_bairro", bairro);
                            json.put("endereco_cep", cep);
                            json.put("endereco_num", numero);
                            json.put("endereco_rua", rua);
                            json.put("email", email);
                            json.put("telefone", telefone);
                            json.put("senha", senha);

                            if (textMudaTipo.getText().toString().equals("Cliente")) {
                                json.put("tipo", 1);
                            }
                            else {
                                json.put("tipo", 2);
                            }
                            if (post(json)) {
                                Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(it);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Não foi possível realizar o cadastro", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "As senhas não são iguais", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Os campo(s):" + vazio.toString() + ", são obrigatório(s).", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (SecurityException e) {
                    Toast.makeText(getApplicationContext(), "Não foi possível encontrar o número de telefone", Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Boolean post(final JSONObject data) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Constantes constantes = new Constantes();

            final RequestBody body = RequestBody.create(MediaType.parse("application/json"), data.toString());
            final Request request = new Request.Builder()
                    .url(constantes.getEND_POINT() + "/user")
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .build();

            final OkHttpClient client = new OkHttpClient();
            final Response response = client.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            JSONObject teste = new JSONObject(json.get("result").toString());


            if (teste.get("razao_social").toString().equals(data.get("razao_social").toString())) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            Log.e("Your tag", "Error", e);
        }

        return true;
    }
}

