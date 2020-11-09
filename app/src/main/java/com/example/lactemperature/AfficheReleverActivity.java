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

        final String[] unLac = new String[1];


        Button btnRetour = (Button) findViewById(R.id.btnRetourAfficheReleve);
        Button btnCelsiusAfficheReleve = (Button) findViewById(R.id.btnCelsiusAfficheReleve);
        Button btnFahrenheitAfficheReleve = (Button) findViewById(R.id.btnFahrenheitAfficheReleve);

        final TextView textViewAfficheTemp = (TextView) findViewById(R.id.textViewAfficheTemp);

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
                        calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog = new DatePickerDialog(AfficheReleverActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                        date.setText(day + "/" + (month + 1) + "/" + year);
                                    }
                                }, year, month, dayOfMonth);
                        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                        datePickerDialog.show();
                        break;
                    case R.id.btnCelsiusAfficheReleve:
                        textViewAfficheTemp.setText("Température (C°)");
                        break;
                    case R.id.btnFahrenheitAfficheReleve:
                        textViewAfficheTemp.setText("Température (F°)");
                        break;
                }
            }
        };
        selectDate.setOnClickListener(ecouteur);
        btnRetour.setOnClickListener(ecouteur);
        btnCelsiusAfficheReleve.setOnClickListener(ecouteur);
        btnFahrenheitAfficheReleve.setOnClickListener(ecouteur);


        //gestion de la liste déroulante des lacs
        final Spinner spinnerAfficheChoixLac = (Spinner) findViewById(R.id.spinnerAfficheChoixLac);
        //Création d'une instance de la classe DAObdd
        DAOBdd daoBdd = new DAOBdd(this);
        //On ouvre la table
        daoBdd.open();
        // On récupère le nom de tous les lacs
        List lesLacs = daoBdd.getAllNomLac();
        List lesRrrrr = daoBdd.getAllReleveByJour("09", "1");
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

        String[] lesHeures = {"6h", "12h", "18h", "24h"};

        final ListView ListViewAfficherR2 = (ListView) findViewById(R.id.ListViewAfficherR2);
        // https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lesHeures);
        ListViewAfficherR2.setAdapter(itemsAdapter);




        Toast.makeText(AfficheReleverActivity.this, lesRrrrr.toString(), Toast.LENGTH_SHORT).show();

        final ListView ListViewAfficherR = (ListView) findViewById(R.id.ListViewAfficherR);
        ArrayAdapter<String> itemsAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lesRrrrr);
        ListViewAfficherR.setAdapter(itemsAdapter2);






    }
}
