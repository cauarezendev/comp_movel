package com.example.caua.mymeds;

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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_PROPOSAL = "EXTRA_PROPOSAL";
    private static final int REQUEST_RESPONSE = 1;
    private ImageButton btnAdicionar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdicionar = (ImageButton) findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, FormReceita.class);
                startActivity(it);
            }
        });

        List<Prescription> prescriptionsList = getAllPrescription();
        listView = (ListView) findViewById(R.id.listView);
        listView.setTextFilterEnabled(true);
        final ArrayAdapter<Prescription> adapter = new ArrayAdapter<Prescription>(this, android.R.layout.simple_list_item_1, prescriptionsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FormProposal.class);
                listView.getContext().startActivity(intent);
                //intent.putExtra(EXTRA_PROPOSAL, position);
                startActivity(intent);
            }
        });


    }

    public List getAllPrescription() {
        List<Prescription> prescriptions = new ArrayList<>();
        Prescription aux = new Prescription("paracetamol0", "paracetamol0");
        prescriptions.add(aux);
        aux = new Prescription("paracetamol1", "paracetamol1");
        prescriptions.add(aux);
        aux = new Prescription("paracetamol2", "paracetamol2");
        prescriptions.add(aux);
        aux = new Prescription("paracetamol3", "paracetamol3");
        prescriptions.add(aux);

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
