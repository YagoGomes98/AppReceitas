package meu.app.apprecipes.dao;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import meu.app.apprecipes.models.Ingrediente;
import java.util.ArrayList;
import java.util.List;

public class IngredienteDAO {

    private SQLiteDatabase db;

    public IngredienteDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public void insert(Ingrediente ingrediente) {
        insert(ingrediente.getName(), ingrediente.getRecipeId());
    }

    public void insert(String ingrediente, long idReceita) {
        ContentValues values = new ContentValues();
        values.put(Config.KEY_NAME, ingrediente);
        values.put(Config.KEY_RECIPE_ID, idReceita);
        db.insert(Config.TABLE_NAME, null, values);
    }

    public List<Ingrediente> selectAllByRecipeId(long idReceita) {
        List<Ingrediente> ingrediente = new ArrayList<>();
        try (Cursor cursor = db.query(Config.TABLE_NAME,
                new String[]{Config.KEY_ID, Config.KEY_NAME, Config.KEY_RECIPE_ID},
                Config.KEY_RECIPE_ID + " = ?", new String[]{idReceita + ""}, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    ingrediente.add(new Ingrediente(
                            cursor.getLong(0), cursor.getString(1), cursor.getLong(2)));
                } while (cursor.moveToNext());
            }
        }

        Log.i("DAO", "IngredientDAO returning: " + ingrediente + " for recipe ID: " + idReceita);
        return ingrediente;
    }

    public boolean deleteAllByRecipeId(long idReceita) {
        return db.delete(Config.TABLE_NAME, Config.KEY_RECIPE_ID + "=" + idReceita, null) > 0;
    }

    public static class Config {
        public static final String TABLE_NAME = "ingredientes";
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "nome";
        public static final String KEY_RECIPE_ID = "idReceita";

        public static final String CREATE_TABLE_STATEMENT =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_NAME + " TEXT NOT NULL, " +
                        KEY_RECIPE_ID + " TEXT NOT NULL, " +
                        "FOREIGN KEY(" + KEY_RECIPE_ID + ") REFERENCES " + ReceitaDAO.Config.TABLE_NAME + "(" + ReceitaDAO.Config.KEY_ID + "))";
    }
}
