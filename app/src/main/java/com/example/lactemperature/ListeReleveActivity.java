package com.example.lactemperature;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListeReleveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listereleve);

        final String[] unMois = new String[1];
        final String[] unLac = new String[1];
        final ListView ListViewListeR1 = (ListView) findViewById(R.id.ListViewListeR1);
        final List[] relevesList = {new ArrayList()};

        final Spinner spinnerListeChoixMois = (Spinner) findViewById(R.id.spinnerListeChoixMois);
        final TextView textViewListeMoyenne = (TextView) findViewById(R.id.textViewListeMoyenne);

        //Création d'une instance de la classe DAObdd
        final DAOBdd relBdd = new DAOBdd(this);
        //On ouvre la table
        relBdd.open();

        Button btnRetour = (Button) findViewById(R.id.btnRetourListeReleve);
        Button btnCelsius = (Button) findViewById(R.id.btnCelsiusListeReleve);
        Button btnFahrenheit = (Button) findViewById(R.id.btnFahrenheitListeReleve);
        //on va créer un écouteur pour un groupe de boutons
        View.OnClickListener ecouteur = new View.OnClickListener() {
            //on implémente la méthode onclick
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnRetourListeReleve:
                        finish();
                        break;
                    case R.id.btnCelsiusListeReleve:
                        long idMois = spinnerListeChoixMois.getSelectedItemId()+1;
                        String idMoisString;
                        // si idMois est inférieur à 10 on ajoute un 0 devant à une variable String
                        if (idMois < 10) {
                            idMoisString = "0"+idMois;
                        } else {
                            idMoisString = String.valueOf(idMois);
                        }
                        //Toast.makeText(ListeReleveActivity.this,relBdd.getReleveByMois(idMoisString, relBdd.getIdByNomLac(unLac[0]).get(0)).toString() , Toast.LENGTH_SHORT).show();

                        // On affiche ces températures dans la ListView
                        relevesList[0] = relBdd.getReleveByMois(idMoisString, relBdd.getIdByNomLac(unLac[0]).get(0));
                        int it=0;
                        for (Object obj : relevesList[0]) {
                            //Toast.makeText(ListeReleveActivity.this, obj.toString(), Toast.LENGTH_SHORT).show();
                            if (Integer.parseInt(obj.toString()) == 0) {
                                relevesList[0].set(it, "");
                            }
                            it++;
                        }

                        ArrayAdapter<String> itemsAdapter2 = new ArrayAdapter<String>(ListeReleveActivity.this, android.R.layout.simple_list_item_1, relevesList[0]);
                        ListViewListeR1.setAdapter(itemsAdapter2);
                        textViewListeMoyenne.setText("Moyenne température (C°)");
                        break;
                    case R.id.btnFahrenheitListeReleve:
                        // On déclare une List lesDegF qui contient nos températures depuis lesRrrrrr
                        List lesDegF = new ArrayList();
                        for (Object obj : relevesList[0]) {
                            lesDegF.add(obj);
                        }

                        // Pour chaque température dans la liste, si celle-ci n'est pas vide, on la convertie en fahreinheit
                        for (int i = 0; i < lesDegF.size(); i++) {
                            if (lesDegF.get(i).toString().length() > 0) {
                                lesDegF.set(i, (Integer.parseInt(lesDegF.get(i).toString()) * 9 / 5) + 32);
                            }
                        }

                        // On affiche ces températures dans la ListView
                        if (lesDegF.size() == 0) {
                            Toast.makeText(ListeReleveActivity.this, "Veuillez d'abord afficher en Celsius", Toast.LENGTH_SHORT).show();
                        }

                        ArrayAdapter<String> itemsAdapterF = new ArrayAdapter<String>(ListeReleveActivity.this, android.R.layout.simple_list_item_1, lesDegF);
                        ListViewListeR1.setAdapter(itemsAdapterF);
                        textViewListeMoyenne.setText("Moyenne température (F°)");
                }
            }
        };
        btnRetour.setOnClickListener(ecouteur);
        btnCelsius.setOnClickListener(ecouteur);
        btnFahrenheit.setOnClickListener(ecouteur);


        //gestion de la liste déroulante des lacs
        final Spinner spinnerListeChoixLac = (Spinner) findViewById(R.id.spinnerListeChoixLac);
        //Création d'une instance de la classe DAObdd
        DAOBdd daoBdd = new DAOBdd(this);
        //On ouvre la table
        daoBdd.open();
        // On récupère le nom de tous les lacs
        List lesLacs = daoBdd.getAllNomLac();
        daoBdd.close();

        ArrayAdapter<String> dataAdapterLac = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lesLacs);
        dataAdapterLac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListeChoixLac.setAdapter(dataAdapterLac);
        spinnerListeChoixLac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unLac[0] = String.valueOf(spinnerListeChoixLac.getSelectedItem());
                //Toast.makeText(ListeReleveActivity.this, "Vous avez choisi le lac : " + unLac[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //gestion de la liste déroulante des mois

        String[] lesMois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        ArrayAdapter<String> dataAdapterR2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lesMois);
        dataAdapterR2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListeChoixMois.setAdapter(dataAdapterR2);
        spinnerListeChoixMois.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unMois[0] = String.valueOf(spinnerListeChoixMois.getSelectedItem());
                //Toast.makeText(ListeReleveActivity.this, "Vous avez choisi : " + unMois[0], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] lesHeures = {"6h", "12h", "18h", "24h"};

        final ListView ListViewListeR2 = (ListView) findViewById(R.id.ListViewListeR2);
        // https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lesHeures);
        ListViewListeR2.setAdapter(itemsAdapter);





    }
}
