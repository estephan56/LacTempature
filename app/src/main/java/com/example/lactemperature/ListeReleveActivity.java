package com.example.lactemperature;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListeReleveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listereleve);

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

    }
}
