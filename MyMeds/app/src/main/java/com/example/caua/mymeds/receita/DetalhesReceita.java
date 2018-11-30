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
        tvDescricao.setText(getIntent().getStringExtra("descricao"));

        tvObs = (TextView) findViewById(R.id.tvObs);
        tvObs.setText(getIntent().getStringExtra("obs"));

        tvData = (TextView) findViewById(R.id.tvData);
        tvData.setText(getIntent().getStringExtra("data"));

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
