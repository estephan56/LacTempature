package com.example.lactemperature;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CreateBDD extends SQLiteOpenHelper {
    //Table Lac
    private static final String TABLE_LAC = "tlac";
    static final String COL_IDLAC = "_id_lac";
    private static final String COL_NOM = "Nom";
    private static final String COL_COORDONNEESLAT = "CoordonneesLat";
    private static final String COL_COORDONNEESLONG = "CoordonneesLong";
    private static final String CREATE_TABLELAC = "CREATE TABLE " +
            TABLE_LAC + " ("+COL_IDLAC+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COL_NOM + " TEXT NOT NULL, " + COL_COORDONNEESLAT + " TEXT NOT NULL, " + COL_COORDONNEESLONG + " TEXT NOT NULL);";

    //Table Relevé
    private static final String TABLE_RELEVE = "treleve";
    static final String COL_IDRELEVE = "_id";
    private static final String COL_JOUR = "Jour";
    private static final String COL_MOIS = "Mois";
    private static final String COL_TEMPERATURE6H = "Temperature6h";
    private static final String COL_TEMPERATURE12H = "Temperature12h";
    private static final String COL_TEMPERATURE18H = "Temperature18h";
    private static final String COL_TEMPERATURE24H = "Temperature24h";
    private static final String COL_FK_IDLAC = "id_lac";
    private static final String CREATE_TABLERELEVE = "CREATE TABLE " +
            TABLE_RELEVE + " ("+COL_IDRELEVE+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ COL_JOUR
            + " TEXT NOT NULL, " + COL_MOIS + " TEXT NOT NULL, " + COL_TEMPERATURE6H + " TEXT ," +
            COL_TEMPERATURE12H + " TEXT , " + COL_TEMPERATURE18H + " TEXT ," + COL_TEMPERATURE24H + " TEXT ," +
            COL_FK_IDLAC+ " INTEGER, " + " FOREIGN KEY ("+COL_FK_IDLAC+") REFERENCES " + TABLE_LAC + "("+COL_IDLAC+"));";



    //constructeur paramétré
    public CreateBDD(Context context, String name, SQLiteDatabase.CursorFactory
            factory, int version) {
        super(context, name, factory, version);
    }
    //méthodes d'instance permettant la gestion de l'objet
    @Override
    public void onCreate(SQLiteDatabase db) {
        //appelée lorsqu’aucune base n’existe et que la classe utilitaire doit en créer une
        //on créé la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_TABLELAC);
        db.execSQL(CREATE_TABLERELEVE);
    }
    // appelée si la version de la base a changé
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut supprimer la table et de la recréer
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LAC + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RELEVE + ";");
        onCreate(db);
    }
}
