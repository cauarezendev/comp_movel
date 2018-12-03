package com.example.caua.mymeds.forms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.caua.mymeds.LoginActivity;
import com.example.caua.mymeds.R;

public class FormProposal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_proposal);
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
                it = new Intent(FormProposal.this, LoginActivity.class);
                startActivity(it);
                return true;
            case R.id.receitas_titulo:
                it = new Intent(FormProposal.this, FormReceita.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
