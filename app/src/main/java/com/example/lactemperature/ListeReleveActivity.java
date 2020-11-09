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
import android.widget.Toast;

import java.util.List;

public class ListeReleveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listereleve);

        final String[] unMois = new String[1];
        final String[] unLac = new String[1];

        Button btnRetour = (Button) findViewById(R.id.btnRetourListeReleve);
        //on va créer un écouteur pour un groupe de boutons
        View.OnClickListener ecouteur = new View.OnClickListener() {
            //on implémente la méthode onclick
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnRetourListeReleve:
                        finish();
                        break;
                }
            }
        };
        btnRetour.setOnClickListener(ecouteur);




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
        final Spinner spinnerListeChoixMois = (Spinner) findViewById(R.id.spinnerListeChoixMois);
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
