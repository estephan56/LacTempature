package com.example.lactemperature;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DAOBdd {
    static final int VERSION_BDD =1;
    private static final String NOM_BDD = "lacTemperature.db";
    //table Lac
    static final String TABLE_LAC = "tlac";
    static final String COL_IDLAC = "_id";
    static final int NUM_COL_IDLAC = 0;
    static final String COL_NOM = "Nom";
    static final int NUM_COL_NOM = 1;
    static final String COL_COORDONNEESLAT = "CoordonneesLat";
    static final int NUM_COL_COORDONNEESLAT = 2;
    static final String COL_COORDONNEESLONG = "CoordonneesLong";
    static final int NUM_COL_COORDONNEESLONG = 3;
    //table relevé
    static final String TABLE_RELEVE = "treleve";
    static final String COL_IDRELEVE = "_id";
    static final int NUM_COL_IDRELEVE = 0;
    static final String COL_JOUR = "Jour";
    static final int NUM_COL_JOUR = 1;
    static final String COL_MOIS = "MOIS";
    static final int NUM_COL_MOIS = 2;
    static final String COL_TEMPERATURE6H = "Temperature6h";
    static final int NUM_COL_TEMPERATURE6H = 3;
    static final String COL_TEMPERATURE12H = "Temperature12h";
    static final int NUM_COL_TEMPERATURE12H = 4;
    static final String COL_TEMPERATURE18H = "Temperature18h";
    static final int NUM_COL_TEMPERATURE18H = 5;
    static final String COL_TEMPERATURE24H = "Temperature24h";
    static final int NUM_COL_TEMPERATURE24H = 6;
    private CreateBDD tableCourante;
    private Context context;
    private SQLiteDatabase db;
    //le constructeur
    public DAOBdd(Context context){
        this.context = context;
        tableCourante = new CreateBDD(context, NOM_BDD, null, VERSION_BDD);
    }
    //si la bdd n’existe pas, l’objet SQLiteOpenHelper exécute la méthode onCreate
    // si la version de la base a changé, la méthode onUpgrade sera lancée
    // dans les 2 cas l’appel à getWritableDatabase ou getReadableDatabase renverra la base
            // de données en cache, nouvellement ouverte, nouvellement créée ou mise à jour
    //les méthodes d'instance
    public DAOBdd open(){
        db = tableCourante.getWritableDatabase();
        return this;
    }
    public DAOBdd close(){
        db.close();
        return null;
    }
    public long insererReleve (Releve unReleve){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne où on veut mettre la valeur)
        values.put(COL_JOUR, unReleve.getJour());
        values.put(COL_MOIS, unReleve.getMois());
        values.put(COL_TEMPERATURE6H, unReleve.getTemperature6h());
        values.put(COL_TEMPERATURE12H, unReleve.getTemperature12h());
        values.put(COL_TEMPERATURE18H, unReleve.getTemperature18h());
        values.put(COL_TEMPERATURE24H, unReleve.getTemperature24h());
        //on insère l'objet dans la BDD via le ContentValues
        return db.insert(TABLE_RELEVE, null, values);
    }
    private Releve cursorToClient(Cursor c){ //Cette méthode permet de convertir un cursor en un relevé
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;
        //Sinon
        c.moveToFirst(); //on se place sur le premier élément
        Releve unReleve = new Releve(null,null,null,null, null, null); //On créé un relevé
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        unReleve.setJour(c.getString(NUM_COL_JOUR));
        unReleve.setMois(c.getString(NUM_COL_MOIS));
        unReleve.setTemperature6h(c.getString(NUM_COL_TEMPERATURE6H));
        unReleve.setTemperature12h(c.getString(NUM_COL_TEMPERATURE12H));
        unReleve.setTemperature18h(c.getString(NUM_COL_TEMPERATURE18H));
        unReleve.setTemperature24h(c.getString(NUM_COL_TEMPERATURE24H));
        c.close(); //On ferme le cursor
        return unReleve; //On retourne le relevé
    }
    public Releve getReleveWithJour(String jour){
        //Récupère dans un Cursor les valeurs correspondant à un relevé grâce au jour
        Cursor c = db.query(TABLE_RELEVE, new String[]
                        {COL_IDRELEVE, COL_JOUR, COL_MOIS, COL_TEMPERATURE6H, COL_TEMPERATURE12H, COL_TEMPERATURE18H, COL_TEMPERATURE24H}, COL_JOUR + " = \"" + jour +"\"", null, null, null, null);
        return cursorToClient(c);
    }
    public Cursor getDataClient(){
        return db.rawQuery("SELECT * FROM treleve", null);
    }

    //pour le Lac
    public long insererLac (Lac unLac){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne où on veut mettre la valeur)
        values.put(COL_NOM, unLac.getNom());
        values.put(COL_COORDONNEESLAT, unLac.getCoordonneesLat());
        values.put(COL_COORDONNEESLONG, unLac.getCoordonneesLong());
        //on insère l'objet dans la BDD via le ContentValues
        return db.insert(TABLE_LAC, null, values);
    }
    private Lac cursorToReleve(Cursor c){ //Cette méthode permet de convertir un cursor en un lac
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;
        //Sinon
        c.moveToFirst(); //on se place sur le premier élément
        Lac unLac = new Lac(null,null,null); //On créé un lac
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        unLac.setNom(c.getString(NUM_COL_NOM));
        unLac.setCoordonneesLat(c.getString(NUM_COL_COORDONNEESLAT));
        unLac.setCoordonneesLong(c.getString(NUM_COL_COORDONNEESLONG));
        c.close(); //On ferme le cursor
        return unLac; //On retourne le lac
    }
    public Lac getLacWithNumCpt(String nom){
        //Récupère dans un Cursor les valeurs correspondant à un Lac grâce au nom du lac.
        Cursor c = db.query(TABLE_LAC, new String[] {COL_IDLAC,COL_NOM,
                        COL_COORDONNEESLAT, COL_COORDONNEESLONG}, COL_NOM + " = \"" + nom +"\"", null, null,
                null, null);
        return cursorToReleve(c);
    }
    public Cursor getDataLac(){
        return db.rawQuery("SELECT * FROM tlac", null);
    }
}