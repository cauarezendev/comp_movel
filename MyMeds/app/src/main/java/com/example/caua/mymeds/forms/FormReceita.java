package com.example.caua.mymeds.forms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.caua.mymeds.LoginActivity;
import com.example.caua.mymeds.R;
import com.example.caua.mymeds.constants.Constantes;
import com.example.caua.mymeds.receita.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FormReceita extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    String mCurrentPhotoPath;
    File photoFile;

    private EditText editDescricao;
    private EditText editObs;
    private Button btnNewPicture;
    private Button btnPublicar;
    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_receita);

        btnNewPicture = (Button) findViewById(R.id.btnNewPicture);
        btnNewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView = (ImageView) findViewById(R.id.mImageView);
                getPermissions();
            }
        });

        btnPublicar = (Button) findViewById(R.id.btnPublicar);
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDescricao = (EditText) findViewById(R.id.editDescricao);
                String description = editDescricao.getText().toString();

                editObs = (EditText) findViewById(R.id.editObs);
                String obs = editObs.getText().toString();

                Boolean teste = true;
                StringBuilder vazio = new StringBuilder();

                if (description.isEmpty()) {
                    vazio.insert(0, " Descrição");
                    teste = false;
                }
                if (obs.isEmpty()) {
                    vazio.insert(0," Observação");
                    teste = false;
                }
                if (photoFile == null) {
                    vazio.insert(0, " Foto da receita");
                    teste = false;
                }

                if (teste) {
                    try {
                        JSONObject json = post(description, obs);
                        if (json.get("data").toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Não foi possível cadastrar a nova receita", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Nova receita cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(FormReceita.this, MainActivity.class);
                            startActivity(it);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Os campos " + vazio.toString() + " são obrigatórios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else
            dispatchTakePictureIntent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try {
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photoFile = File.createTempFile("PHOTOAPP", ".jpg", storageDir);
                mCurrentPhotoPath = "file:" + photoFile.getAbsolutePath();
            }
            catch(IOException ex){
                Toast.makeText(getApplicationContext(), "Erro ao tirar a foto", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Bitmap bm1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(mCurrentPhotoPath)));
                mImageView.setImageBitmap(bm1);
            }catch(FileNotFoundException fnex){
                Toast.makeText(getApplicationContext(), "Foto não encontrada!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public JSONObject post(String description, String obs) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Constantes constantes = new Constantes();

            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());

            Intent it = getIntent();
            String id = it.getStringExtra("id");

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_id", id)
                    .addFormDataPart("descricao", description)
                    .addFormDataPart("data", nowAsISO)
                    .addFormDataPart("obs", obs)
                    .addFormDataPart("receita", "image.jpg", RequestBody.create(MediaType.parse("image/jpg"), photoFile))
                    .build();

            Request request = new Request.Builder()
                    .url(constantes.getEND_POINT() + "/receita")
                    .post(requestBody)
                    .build();

            final OkHttpClient client = new OkHttpClient();
            final Response response = client.newCall(request).execute();

            JSONObject result = new JSONObject(response.body().string());
            JSONObject json = new JSONObject(result.get("result").toString());
            return json;
        }
        catch (Exception e) {
            Log.e("Your tag", "Error", e);
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
                it = new Intent(FormReceita.this, LoginActivity.class);
                startActivity(it);
                return true;
            case R.id.inicio:
                it = new Intent(FormReceita.this, MainActivity.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
