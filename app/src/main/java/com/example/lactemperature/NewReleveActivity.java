package com.example.lactemperature;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

public class NewReleveActivity extends Activity {
    Button selectDate2;
    TextView date;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newreleve);

        selectDate2 = findViewById(R.id.btnDateNewR);
        date = findViewById(R.id.editTextDate);

        final String[] uneHeure = new String[1];
        final String[] unLac = new String[1];
        //Gestion des boutons enregistrer et annuler
        Button btnValider = (Button) findViewById(R.id.btnValiderNewR);
        Button btnAnnuler = (Button) findViewById(R.id.btnAnnulerNewR);
        final DAOBdd releveBdd = new DAOBdd(this);
        releveBdd.open();
        //on va créer un écouteur pour un groupe de boutons
        View.OnClickListener ecouteur = new View.OnClickListener() {
            //on implémente la méthode onclick
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.btnAnnulerNewR:
                        finish();
                        break;
                    case R.id.btnDateNewR:
                        calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog = new DatePickerDialog(NewReleveActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                        date.setText(day + "/" + (month + 1) + "/" + year);
                                    }
                                }, year, month, dayOfMonth);
                        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                        datePickerDialog.show();
                }
            }
        };
        selectDate2.setOnClickListener(ecouteur);
        btnAnnuler.setOnClickListener(ecouteur);


        //gestion de la liste déroulante des heures
        final Spinner spinnerNewChoixHeure = (Spinner) findViewById(R.id.spinnerNewChoixHeure);
        String[] lesHeures = {"6h", "12h", "18h", "0h"};
        ArrayAdapter<String> dataAdapterR = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lesHeures);
        dataAdapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNewChoixHeure.setAdapter(dataAdapterR);
        spinnerNewChoixHeure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uneHeure[0] = String.valueOf(spinnerNewChoixHeure.getSelectedItem());
                //Toast.makeText(NewReleveActivity.this, "Vous avez choisi : " + "\nl'heure : " + uneHeure[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //gestion de la liste déroulante des lacs
        final Spinner spinnerNewChoixLac = (Spinner) findViewById(R.id.spinnerNewChoixLac);
        //Création d'une instance de la classe DAObdd
        DAOBdd daoBdd = new DAOBdd(this);
        //On ouvre la table
        daoBdd.open();
        //on récupère tous les lacs via le curseur
        Cursor c = daoBdd.getDataLac();
        //Toast.makeText(getApplicationContext(), "il y a " + String.valueOf(c.getCount()) +
        //        " lacs dans la table", Toast.LENGTH_LONG).show();

        // On récupère le nom de tous les lacs
        List lesLacs = daoBdd.getAllNomLac();
        daoBdd.close();

        ArrayAdapter<String> dataAdapterLac = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lesLacs);
        dataAdapterLac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNewChoixLac.setAdapter(dataAdapterLac);
        spinnerNewChoixLac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unLac[0] = String.valueOf(spinnerNewChoixLac.getSelectedItem());
                //Toast.makeText(NewReleveActivity.this, "Vous avez choisi le lac : " + unLac[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}
