package com.example.lactemperature;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNewReleve = (Button) findViewById(R.id.btnNewReleve);
        Button btnListeReleve = (Button) findViewById(R.id.btnListeReleve);
        Button btnAfficheReleve = (Button) findViewById(R.id.btnAffichageReleve);

        //on va créer un écouteur pour un groupe de boutons
        View.OnClickListener ecouteur = new View.OnClickListener() {
            //on implémente la méthode onclick
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnNewReleve:
                        Intent intent = new Intent(MainActivity.this, NewReleveActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnListeReleve:
                        intent = new Intent(MainActivity.this, ListeReleveActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnAffichageReleve:
                        intent = new Intent(MainActivity.this, AfficheReleverActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
        btnNewReleve.setOnClickListener(ecouteur);
        btnListeReleve.setOnClickListener(ecouteur);
        btnAfficheReleve.setOnClickListener(ecouteur);

    }
}