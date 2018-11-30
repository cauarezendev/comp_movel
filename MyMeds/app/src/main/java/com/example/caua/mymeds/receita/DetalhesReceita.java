package com.example.caua.mymeds.receita;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caua.mymeds.LoginActivity;
import com.example.caua.mymeds.R;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DetalhesReceita extends AppCompatActivity {
    private TextView tvDescricao;
    private TextView tvObs;
    private TextView tvData;
    private ImageView imgReceita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_receita);

        tvDescricao = (TextView) findViewById(R.id.tvDescricao);
        tvDescricao.setText("Descrição: " + getIntent().getStringExtra("descricao"));

        tvObs = (TextView) findViewById(R.id.tvObs);
        tvObs.setText("Observação: " + getIntent().getStringExtra("obs"));

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

        tvData = (TextView) findViewById(R.id.tvData);
        tvData.setText("Data: " + dfs.format(date));
        imgReceita = (ImageView) findViewById(R.id.imgReceita);
        try {
            URL url = new URL(getIntent().getStringExtra("receita"));
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            //imgReceita.setImageBitmap(bmp);
            imgReceita.setImageBitmap(bmp);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        imgReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
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
                it = new Intent(DetalhesReceita.this, LoginActivity.class);
                startActivity(it);
                return true;
            case R.id.inicio:
                it = new Intent(DetalhesReceita.this, MainActivity.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
