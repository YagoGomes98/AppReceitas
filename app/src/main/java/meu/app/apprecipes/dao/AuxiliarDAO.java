package meu.app.apprecipes.dao;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import meu.app.apprecipes.models.Auxiliar;

public class AuxiliarDAO {

    private SQLiteDatabase db;

    public AuxiliarDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void insert(Auxiliar auxiliar) {
        insert(auxiliar.getBody(), auxiliar.getIdReceita());
    }

    public void insert(String auxiliar, long idReceita) {
        ContentValues values = new ContentValues();
        values.put(Config.KEY_BODY, auxiliar);
        values.put(Config.KEY_RECIPE_ID, idReceita);
        db.insert(Config.TABLE_NAME, null, values);
    }

    public List<Auxiliar> selectAllByRecipeId(long idReceita) {
        List<Auxiliar> auxiliar = new ArrayList<>();
        try (Cursor cursor = db.query(Config.TABLE_NAME,
                new String[]{Config.KEY_ID, Config.KEY_BODY, Config.KEY_RECIPE_ID},
                Config.KEY_RECIPE_ID + " = ?", new String[]{idReceita + ""}, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    auxiliar.add(new Auxiliar(
                            cursor.getLong(0), cursor.getString(1), cursor.getLong(2)));
                } while (cursor.moveToNext());
            }
        }

        Log.i("DAO", "DirectionDAO returning: " + auxiliar + " for recipe ID: " + idReceita);
        return auxiliar;
    }

    public boolean deleteAllByRecipeId(long idReceita) {
        return db.delete(Config.TABLE_NAME, Config.KEY_RECIPE_ID + "=" + idReceita, null) > 0;
    }

    public static class Config {
        public static final String TABLE_NAME = "auxiliar";
        public static final String KEY_ID = "id";
        public static final String KEY_BODY = "corpo";
        public static final String KEY_RECIPE_ID = "idReceita";

        public static final String CREATE_TABLE_STATEMENT =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_BODY + " TEXT NOT NULL, " +
                        KEY_RECIPE_ID + " TEXT NOT NULL, " +
                        "FOREIGN KEY(" + KEY_RECIPE_ID + ") REFERENCES " + ReceitaDAO.Config.TABLE_NAME + "(" + ReceitaDAO.Config.KEY_ID + "))";
    }
}
