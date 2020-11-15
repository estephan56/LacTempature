package com.example.lactemperature;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AfficheReleverActivity extends Activity {
    // déclarations des attributs utiles au calendrier
    Button selectDate;
    TextView date;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichereleve);

        selectDate = findViewById(R.id.btnDate);
        date = findViewById(R.id.editTextDate2);

        // Gestion des boutons retour, celsius et farhneiheit
        Button btnRetour = (Button) findViewById(R.id.btnRetourAfficheReleve);
        Button btnCelsiusAfficheReleve = (Button) findViewById(R.id.btnCelsiusAfficheReleve);
        Button btnFahrenheitAfficheReleve = (Button) findViewById(R.id.btnFahrenheitAfficheReleve);

        // Gestion des attributs final
        final String[] unLac = new String[1];
        final TextView textViewAfficheTemp = (TextView) findViewById(R.id.textViewAfficheTemp);
        final ListView ListViewAfficherR = (ListView) findViewById(R.id.ListViewAfficherR);
        final List[] lesRelevesCel = {new ArrayList()};

        // Création d'une instance de la classe daoBdd2
        final DAOBdd daoBdd2 = new DAOBdd(this);
        daoBdd2.open();

        //on va créer un écouteur pour un groupe de boutons
        View.OnClickListener ecouteur = new View.OnClickListener() {
            //on implémente la méthode onclick
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnRetourAfficheReleve:
                        finish();
                        break;
                    case R.id.btnDate:
                        // Gestion du calendrier, en choisissant une date, elle est retournée dans le TextView date
                        calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog = new DatePickerDialog(AfficheReleverActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                        String affiche;
                                        // Permet d'afficher un 0 devant le jour/mois si celui-ci est inférieur à 9
                                        if (month < 9 && day < 10) {
                                            affiche = "0" + day + "/" + "0" + (month + 1) + "/" + year;
                                        } else if (month < 9) {
                                            affiche = day + "/" + "0"+(month + 1) + "/" + year;
                                        } else if (day < 10) {
                                            affiche = "0"+day + "/" + (month + 1) + "/" + year;
                                        } else {
                                            affiche = day + "/" + (month + 1) + "/" + year;
                                        }
                                        date.setText(affiche);

                                    }
                                }, year, month, dayOfMonth);
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // On ne doit pas pouvoir afficher un relevé dans le futur
                        datePickerDialog.show();
                        break;
                    case R.id.btnCelsiusAfficheReleve:
                        // Récupère la date du textView
                        String date1;
                        date1 = date.getText().toString();

                        // On récupère la température des relevés en fonction de la date du jour
                        lesRelevesCel[0] = daoBdd2.getAllReleveByJour(date1.substring(0, 2), date1.substring(3,5), daoBdd2.getIdByNomLac(unLac[0]));

                        // On affiche combien il y a de relevés pour ce jour
                        int compte=0;
                        for (Object obj : lesRelevesCel[0]){
                            if (obj.toString().length() > 0) {
                                compte++;
                            }
                        }
                        Toast.makeText(AfficheReleverActivity.this, "Il y a "+compte+" relevés pour ce jour.", Toast.LENGTH_SHORT).show();

                        // On affiche ces températures dans la ListView
                        ArrayAdapter<String> itemsAdapter2 = new ArrayAdapter<String>(AfficheReleverActivity.this, android.R.layout.simple_list_item_1, lesRelevesCel[0]);
                        ListViewAfficherR.setAdapter(itemsAdapter2);
                        textViewAfficheTemp.setText("Température (C°)");
                        break;
                    case R.id.btnFahrenheitAfficheReleve:
                        // On déclare une List lesDegF qui contient nos températures depuis lesRelevesCel
                        List lesDegF = new ArrayList();
                        for (Object obj : lesRelevesCel[0]) {
                            lesDegF.add(obj.toString());
                        }

                        // Pour chaque température dans la liste, si celle-ci n'est pas vide, on la convertie en fahreinheit
                        for (int i = 0; i < lesDegF.size(); i++) {
                            if (lesDegF.get(i).toString().length() > 0) {
                                lesDegF.set(i, (Integer.parseInt(lesDegF.get(i).toString()) * 9 / 5) + 32);
                            }
                            //Toast.makeText(AfficheReleverActivity.this, lesDegF.get(i).toString(), Toast.LENGTH_SHORT).show();
                        }

                        // Prévient l'utilisateur d'afficher en celsius en premier
                        if (lesDegF.size() == 0) {
                            Toast.makeText(AfficheReleverActivity.this, "Veuillez d'abord afficher en Celsius", Toast.LENGTH_SHORT).show();
                        }

                        // On affiche ces températures dans la ListView
                        ArrayAdapter<String> itemsAdapterF = new ArrayAdapter<String>(AfficheReleverActivity.this, android.R.layout.simple_list_item_1, lesDegF);
                        ListViewAfficherR.setAdapter(itemsAdapterF);
                        textViewAfficheTemp.setText("Température (F°)");
                        break;
                }
            }
        };
        selectDate.setOnClickListener(ecouteur);
        btnRetour.setOnClickListener(ecouteur);
        btnCelsiusAfficheReleve.setOnClickListener(ecouteur);
        btnFahrenheitAfficheReleve.setOnClickListener(ecouteur);


        //Gestion de la liste déroulante des lacs
        final Spinner spinnerAfficheChoixLac = (Spinner) findViewById(R.id.spinnerAfficheChoixLac);
        //Création d'une instance de la classe DAObdd
        DAOBdd daoBdd = new DAOBdd(this);
        //On ouvre la table
        daoBdd.open();
        //Liste des lacs
        List lesLacs = daoBdd.getAllNomLac();
        //List lesRelevesCel = daoBdd.getAllReleveByJour("09", "1");
        daoBdd.close();

        ArrayAdapter<String> dataAdapterLac = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lesLacs);
        dataAdapterLac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAfficheChoixLac.setAdapter(dataAdapterLac);
        spinnerAfficheChoixLac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unLac[0] = String.valueOf(spinnerAfficheChoixLac.getSelectedItem());
                //Toast.makeText(AfficheReleverActivity.this, "Vous avez choisi le lac : " + unLac[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Gestion de la liste déroulante des heures
        String[] lesHeures = {"6h", "12h", "18h", "24h"};

        final ListView ListViewAfficherR2 = (ListView) findViewById(R.id.ListViewAfficherR2);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lesHeures);
        ListViewAfficherR2.setAdapter(itemsAdapter);

        // La date du jour est présélectionnée au lancement
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

        // TEST TOAST BDD
        //Toast.makeText(AfficheReleverActivity.this, lesRelevesCel.toString(), Toast.LENGTH_SHORT).show();

        //final ListView ListViewAfficherR = (ListView) findViewById(R.id.ListViewAfficherR);
        //ArrayAdapter<String> itemsAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lesRelevesCel);
        //ListViewAfficherR.setAdapter(itemsAdapter2);


    }
}
