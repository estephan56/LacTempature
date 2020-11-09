package com.example.lactemperature;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNewReleve = (Button) findViewById(R.id.btnNewReleve);
        Button btnListeReleve = (Button) findViewById(R.id.btnListeReleve);
        Button btnAfficheReleve = (Button) findViewById(R.id.btnAffichageReleve);

        //deleteLacs();
        //remplirLacs();


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

    public void remplirReleves() {
        DAOBdd daoBdd = new DAOBdd(this);
        Releve releve1 = new Releve("15", "10", "17", "21", "22", "19", "0");
        //on ouvre la base de données
        daoBdd.open();
        //on insère le relevé
        daoBdd.insererReleve(releve1);
        //le curseur pour afficher le nombre de relevés dans la base
        Cursor c1 = daoBdd.getDataReleve();
        Toast.makeText(getApplicationContext(), " il y a " + String.valueOf(c1.getCount()) + " relevés ", Toast.LENGTH_LONG).show();
    }

    public void remplirLacs(){
        DAOBdd daoBdd = new DAOBdd(this);
        Lac lac1 = new Lac("Lac Léman", "46.455743", "6.562420");
        Lac lac2 = new Lac("Lac Bled", "46.363068", "14.093823");
        //on ouvre la base de données
        daoBdd.open();
        //on insère lac1 puis lac2
        daoBdd.insererLac(lac1);
        daoBdd.insererLac(lac2);
        //le curseur pour afficher le nombre de lacs dans la base
        Cursor c = daoBdd.getDataLac();
        //Toast.makeText(getApplicationContext(), " il y a " + String.valueOf(c.getCount()) + " lacs ", Toast.LENGTH_LONG).show();
    }

    public void deleteLacs() {
        DAOBdd daoBdd = new DAOBdd(this);
        daoBdd.open();
        daoBdd.deleteLacs();
    }

}