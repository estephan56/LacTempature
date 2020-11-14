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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

        // Création d'une instance de la classe releveBdd
        final DAOBdd releveBdd = new DAOBdd(this);
        releveBdd.open();

        // récupération des données saisies
        final EditText temp = findViewById(R.id.editTextNumberTemperature);

        //on va créer un écouteur pour un groupe de boutons
        View.OnClickListener ecouteur = new View.OnClickListener() {
            //on implémente la méthode onclick
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnAnnulerNewR:
                        finish();
                        break;
                    case R.id.btnValiderNewR:
                        Releve releve;
                        String date1;
                        date1 = date.getText().toString();

                        // Message si il y n'a pas de température
                        if (temp.getText().toString().length() < 1) {
                            Toast.makeText(NewReleveActivity.this, "Veuillez ajouter une température", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        // Ajout dans la bdd en fonction de l'heure, récupération de l'id du lac en fonction du nom
                        if (uneHeure[0] == "6h") {
                            releve = new Releve(date1.substring(0, 2), date1.substring(3, 5), temp.getText().toString(), null, null,null, releveBdd.getIdByNomLac(unLac[0]).get(0));
                        } else if (uneHeure[0] == "12h") {
                            releve = new Releve(date1.substring(0, 2), date1.substring(3, 5), null, temp.getText().toString(), null,null, releveBdd.getIdByNomLac(unLac[0]).get(0));
                        } else if (uneHeure[0] == "18h") {
                            releve = new Releve(date1.substring(0, 2), date1.substring(3, 5), null, null, temp.getText().toString(),null, releveBdd.getIdByNomLac(unLac[0]).get(0));
                        } else {
                            releve = new Releve(date1.substring(0, 2), date1.substring(3, 5), null, null, null,temp.getText().toString(), releveBdd.getIdByNomLac(unLac[0]).get(0));
                        }
                        releveBdd.insererReleve(releve);
                        //releveBdd.close();
                        finish();
                        // Affichage d'un résumé
                        Toast.makeText(NewReleveActivity.this, "Le relevé a bien été enregistré.", Toast.LENGTH_SHORT).show();
                        Cursor c2 = releveBdd.getDataReleve();
                        Toast.makeText(getApplicationContext(), "Il y a " + String.valueOf(c2.getCount()) + " relevés dans la table.", Toast.LENGTH_LONG).show();
                        releveBdd.close();
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
                                        String affiche;
                                        // Permet d'afficher un 0 devant le jour/mois si celui-ci est inférieur à 9
                                        if (month < 9 && day < 9) {
                                            affiche = "0" + day + "/" + "0" + (month + 1) + "/" + year;
                                        } else if (month < 9) {
                                            affiche = day + "/" + "0"+(month + 1) + "/" + year;
                                        } else if (day < 9) {
                                            affiche = "0"+day + "/" + (month + 1) + "/" + year;
                                        } else {
                                            affiche = day + "/" + (month + 1) + "/" + year;
                                        }
                                        date.setText(affiche);

                                    }
                                }, year, month, dayOfMonth);
                        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                        datePickerDialog.show();

                }
            }
        };
        selectDate2.setOnClickListener(ecouteur);
        btnAnnuler.setOnClickListener(ecouteur);
        btnValider.setOnClickListener(ecouteur);


        //gestion de la liste déroulante des heures
        final Spinner spinnerNewChoixHeure = (Spinner) findViewById(R.id.spinnerNewChoixHeure);
        String[] lesHeures = {"6h", "12h", "18h", "24h"};
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
        final DAOBdd daoBdd = new DAOBdd(this);
        //On ouvre la table
        daoBdd.open();
        //on récupère tous les lacs via le curseur
        //Cursor c = daoBdd.getDataLac();
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
                daoBdd.open();
                // Modification du TextView pour les coordonnées GPS
                final TextView textViewCoordGps = (TextView) findViewById(R.id.textViewCoordGps);
                // Les coordonnées sont enregistrées dans une liste ll en fonction du nom du lac
                List ll = daoBdd.getGpsByNomLac(unLac[0]);
                textViewCoordGps.setText("Coordonnées :\n"+ll.get(0).toString()+", "+ll.get(1).toString());
                daoBdd.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // La date du jour est présélectionnée au lancement
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

        // TEST
        //final TextView textViewCoordGps = (TextView) findViewById(R.id.textViewCoordGps);
        //List ll = releveBdd.getIdByNomLac(unLac[0]);
        //textViewCoordGps.setText(ll.get(0).toString());

    }
}
