package com.example.lactemperature;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DAOBdd {
    static final int VERSION_BDD = 5;
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
    static final String COL_MOIS = "Mois";
    static final int NUM_COL_MOIS = 2;
    static final String COL_TEMPERATURE6H = "Temperature6h";
    static final int NUM_COL_TEMPERATURE6H = 3;
    static final String COL_TEMPERATURE12H = "Temperature12h";
    static final int NUM_COL_TEMPERATURE12H = 4;
    static final String COL_TEMPERATURE18H = "Temperature18h";
    static final int NUM_COL_TEMPERATURE18H = 5;
    static final String COL_TEMPERATURE24H = "Temperature24h";
    static final int NUM_COL_TEMPERATURE24H = 6;
    static  final String COL_FK_IDLAC = "id_lac";
    static final int NUM_COL_FK_IDLAC = 7;
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
        values.put(COL_FK_IDLAC, unReleve.getId_lac());
        //on insère l'objet dans la BDD via le ContentValues
        return db.insert(TABLE_RELEVE, null, values);
    }
    private Releve cursorToReleve(Cursor c){ //Cette méthode permet de convertir un cursor en un relevé
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;
        //Sinon
        c.moveToFirst(); //on se place sur le premier élément
        Releve unReleve = new Releve(null,null,null,null, null, null, null); //On créé un relevé
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        unReleve.setJour(c.getString(NUM_COL_JOUR));
        unReleve.setMois(c.getString(NUM_COL_MOIS));
        unReleve.setTemperature6h(c.getString(NUM_COL_TEMPERATURE6H));
        unReleve.setTemperature12h(c.getString(NUM_COL_TEMPERATURE12H));
        unReleve.setTemperature18h(c.getString(NUM_COL_TEMPERATURE18H));
        unReleve.setTemperature24h(c.getString(NUM_COL_TEMPERATURE24H));
        unReleve.setId_lac(c.getString(NUM_COL_FK_IDLAC));
        c.close(); //On ferme le cursor
        return unReleve; //On retourne le relevé
    }

    // Permet d'obtenir les 4 températures d'un jour en fonction de la date et du lac
    public List<String> getAllReleveByJour(String jour, String mois, String id_lac){
        List<String> listeReleve = new ArrayList<>();
        // Ajoute 4 String vide à la liste
        for (int i=0;i < 4; i++){
            listeReleve.add("");
        }
        Cursor c = db.rawQuery("SELECT * FROM treleve WHERE Jour = "+"'"+jour+"'"+" AND id_lac = "+id_lac+" AND Mois = "+"'"+mois+"'", null);
        // Pour chaque élément, si x température n'est pas vide, on l'ajoute à la liste à la place d'un String vide
        if(c.moveToFirst()) {
            do {
                if (!c.isNull(3)) {
                    listeReleve.set(0, c.getString(3)); // Exemple: 14 à la place de "" pour Temperature6h
                } else if (!c.isNull(4)) {
                    listeReleve.set(1, c.getString(4));
                } else if (!c.isNull(5)) {
                    listeReleve.set(2, c.getString(5));
                } else if (!c.isNull(6)){
                    listeReleve.set(3, c.getString(6));
                }
            } while (c.moveToNext());
        }
        c.close();
        //db.close();
        return listeReleve;
    }

    // Permet d'obtenir une liste avec la moyenne de 4 températures pour un mois et un lac donné
    public List<Integer> getReleveByMois(String mois, String id_lac){
        // On déclare 2 listes, une avec la somme de x température et une avec le compte total de x température
        List<Integer> listeReleve = new ArrayList<>();
        List<Integer> listeCount = new ArrayList<>();
        // Ajoute 4 0 aux listes
        for (int i=0;i < 4; i++){
            listeReleve.add(0);
            listeCount.add(0);
        }
        Cursor c = db.rawQuery("SELECT * FROM treleve WHERE Mois = "+"'"+mois+"'"+" AND id_lac = "+id_lac, null);
        // Pour chaque élément, si x température n'est pas vide, on l'additionne à son emplacement dans la liste et on compte +1 dans listeCount
        if(c.moveToFirst()) {
            do {
                if (!c.isNull(3)) {
                    listeReleve.set(0, Integer.parseInt(c.getString(3))+listeReleve.get(0)); // Temperature6h est additionné à la valeur située à son emplacement dans la liste
                    listeCount.set(0, listeCount.get(0)+1); // on ajoute +1 à son emplacement
                } else if (!c.isNull(4)) {
                    listeReleve.set(1, Integer.parseInt(c.getString(4))+listeReleve.get(1));
                    listeCount.set(1, listeCount.get(1)+1);
                } else if (!c.isNull(5)) {
                    listeReleve.set(2, Integer.parseInt(c.getString(5))+listeReleve.get(2));
                    listeCount.set(2, listeCount.get(2)+1);
                } else if (!c.isNull(6)){
                    listeReleve.set(3, Integer.parseInt(c.getString(6))+listeReleve.get(3));
                    listeCount.set(3, listeCount.get(3)+1);
                }
            } while (c.moveToNext());
        }
        // Pour chaque élément dans listeCount
        for (int i=0;i < 4; i++){
            // si celui-ci est égal à 0 alors on le remplace par 1 (empêche la division par 0)
            if (listeCount.get(i) == 0) {
                listeCount.set(i, 1);
            }
            // ajoute à la liste le résultat de la somme de x température divisé par le nombre de x température
            listeReleve.set(i, listeReleve.get(i)/listeCount.get(i));
        }
        return listeReleve;
    }

    // Méthode de test pour relevés 1
/*    public List<String> getReleve(String jour, String id_lac){
        List<String> listeReleve = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM treleve WHERE Jour = "+"'"+jour+"'"+" AND id_lac = "+id_lac, null);
        if(c.moveToFirst()) {
            listeReleve.add(c.getString(0)+c.getColumnName(0));
            listeReleve.add(c.getString(1)+c.getColumnName(1));
            listeReleve.add(c.getString(2)+c.getColumnName(2));
            listeReleve.add(c.getString(3)+c.getColumnName(3));
            listeReleve.add(c.getString(7)+c.getColumnName(7));
        }
        c.close();
        db.close();
        return listeReleve;
    }*/

    // Méthode de test pour relevés 2
/*    public List<String> getReleveTest(){
        List<String> listeReleve = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM treleve", null);
        if(c.moveToLast()) {

            //listeReleve.add(c.getString(0)+c.getColumnName(0));
            //listeReleve.add(c.getString(1)+c.getColumnName(1));
            listeReleve.add(c.getString(2)+c.getColumnName(2));
            //listeReleve.add(c.getString(3)+c.getColumnName(3));
            //listeReleve.add(c.getString(7)+c.getColumnName(7));
        }
        //c.close();
        //db.close();
        return listeReleve;
    }*/

    // Méthode de test pour relevés 3
/*    public List<Integer> getReleveByMoisTest(String mois, String id_lac){
        List<Integer> listeReleve = new ArrayList<>();
        for (int i=0;i < 4; i++){
            listeReleve.add(0);
        }

        Cursor c = db.rawQuery("SELECT Temperature6h, Temperature12h, Temperature18h, Temperature24h FROM treleve WHERE Mois = "+mois+" AND id_lac = "+id_lac, null);
        if(c.moveToFirst()) {
            do {
                if (!c.isNull(0)) {
                    listeReleve.set(0, Integer.parseInt(c.getString(0)));
                } else if (!c.isNull(1)) {
                    listeReleve.set(1, Integer.parseInt(c.getString(1)));
                } else if (!c.isNull(2)) {
                    listeReleve.set(2, Integer.parseInt(c.getString(2)));
                } else if (!c.isNull(3)){
                    listeReleve.set(3, Integer.parseInt(c.getString(3)));
                }
            } while (c.moveToNext());
        }
        return listeReleve;
    }*/

    public Cursor getDataReleve(){
        return db.rawQuery("SELECT * FROM treleve", null);
    }

    // inserer lac dans la bdd
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
    private Lac cursorToLac(Cursor c){ //Cette méthode permet de convertir un cursor en un lac
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

    public Cursor getDataLac(){
        return db.rawQuery("SELECT * FROM tlac", null);
    }

    // Permet de retourner une liste contenant uniquement le noms des lacs
    public List<String> getAllNomLac(){
        List<String> listeNomLacs = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT Nom FROM tlac", null);
        if(c.moveToFirst()) {
            do {
                listeNomLacs.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        //db.close();
        return listeNomLacs;
    }

    public void deleteLacs(){
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_LAC + "'");
        db.execSQL("delete from "+ TABLE_LAC);
        db.close();
    }
    public void deleteReleves(){
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_RELEVE + "'");
        db.execSQL("delete from "+ TABLE_RELEVE);
        db.close();
    }

    // Permet de retourner une liste incluant les coordonnées d'un lac depuis son nom
    public List<String> getGpsByNomLac(String lac) {
        List<String> listeNomLacs = new ArrayList<>();
        // Ajoute la latitude à la liste
        Cursor c = db.rawQuery("SELECT " + COL_COORDONNEESLAT + " FROM tlac WHERE nom = "+"'"+lac+"'", null);
        if(c.moveToFirst()) {
            listeNomLacs.add(c.getString(0));
        }
        // Ajoute la longitude à la liste
        Cursor c2 = db.rawQuery("SELECT " + COL_COORDONNEESLONG + " FROM tlac WHERE nom = "+"'"+lac+"'", null);
        if(c2.moveToFirst()) {
            listeNomLacs.add(c2.getString(0));
        }
        c.close();
        c2.close();
        //db.close();
        return listeNomLacs;
    }

    // Permet de retourner l'id d'un lac depuis son nom
    public String getIdByNomLac(String nomLac) {
        String idLac = "";
        Cursor c = db.rawQuery("SELECT * FROM tlac WHERE nom = "+"'"+nomLac+"'", null);
        if(c.moveToFirst()) {
            idLac = c.getString(0);
        }
        c.close();
        return idLac;
    }

}