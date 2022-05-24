package meu.app.apprecipes.dao;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import meu.app.apprecipes.models.Receita;

public class ReceitaDAO {

    private SQLiteDatabase db;

    private IngredienteDAO ingredienteDAO;
    private AuxiliarDAO auxiliarDAO;

    public ReceitaDAO(SQLiteDatabase db) {
        this.db = db;

        ingredienteDAO = new IngredienteDAO(db);
        auxiliarDAO = new AuxiliarDAO(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long insert(Receita receita) {
        if (receita.getIngredients() == null || receita.getAuxiliar() == null)
            throw new IllegalStateException("Não é possível inserir uma receita incompleta.");

        long newRecipeId = insert(receita.getName(), receita.getCategory(), receita.getDescription(), receita.getImagePath());
        Log.i("DAO", "Inserted new recipe : " + newRecipeId);
        Log.i("DAO", "New recipe has " + receita.getAuxiliar().size() + " directions.");
        receita.getIngredients()
                .forEach(ingrediente -> {
                    ingrediente.setRecipeId(newRecipeId);
                    ingredienteDAO.insert(ingrediente);
                    Log.i("DAO", "Inserted " + ingrediente);
                });
        receita.getAuxiliar()
                .forEach(auxiliar -> {
                    auxiliar.setIdReceita(newRecipeId);
                    auxiliarDAO.insert(auxiliar);
                    Log.i("DAO", "Inserted " + auxiliar);
                });

        return newRecipeId;
    }

    private long insert(String nome, String categoria, String descricao, String imagePath) {
        ContentValues values = new ContentValues();
        values.put(Config.KEY_NAME, nome);
        values.put(Config.KEY_CATEGORY, categoria);
        values.put(Config.KEY_DESCRIPTION, descricao);
        values.put(Config.KEY_IMAGE_PATH, imagePath);
        return db.insert(Config.TABLE_NAME, null, values);
    }

    public List<Receita> selectAllByCategory(String categoria) {
        List<Receita> receitas = new ArrayList<>();
        try (Cursor cursor = db.query(Config.TABLE_NAME,
                new String[]{Config.KEY_ID, Config.KEY_NAME, Config.KEY_CATEGORY, Config.KEY_DESCRIPTION, Config.KEY_IMAGE_PATH},
                Config.KEY_CATEGORY + " = ?", new String[]{categoria}, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    Receita receita = new Receita(
                            cursor.getLong(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4));
                    receita.setIngredientes(ingredienteDAO.selectAllByRecipeId(receita.getId()));
                    receita.setAuxiliar(auxiliarDAO.selectAllByRecipeId(receita.getId()));
                    receitas.add(receita);

                } while (cursor.moveToNext());
            }
        }

        Log.i("DAO", "RecipeDAO returning: " + receitas);
        return receitas;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(Receita receita) {
        ingredienteDAO.deleteAllByRecipeId(receita.getId());
        auxiliarDAO.deleteAllByRecipeId(receita.getId());
        receita.getIngredients()
                .forEach(ingrediente -> {
                    ingrediente.setRecipeId(receita.getId());
                    ingredienteDAO.insert(ingrediente);
                });
        receita.getAuxiliar()
                .forEach(auxiliar -> {
                    auxiliar.setIdReceita(receita.getId());
                    auxiliarDAO.insert(auxiliar);
                });

        ContentValues values = new ContentValues();
        values.put(Config.KEY_NAME, receita.getName());
        values.put(Config.KEY_CATEGORY, receita.getCategory());
        values.put(Config.KEY_DESCRIPTION, receita.getDescription());
        values.put(Config.KEY_IMAGE_PATH, receita.getImagePath());
        db.update(Config.TABLE_NAME, values, Config.KEY_ID + "=" + receita.getId(), null);
    }

    public boolean deleteById(long id) {
        ingredienteDAO.deleteAllByRecipeId(id);
        auxiliarDAO.deleteAllByRecipeId(id);
        return db.delete(Config.TABLE_NAME, Config.KEY_ID + "=" + id, null) > 0;
    }

    public static class Config {
        public static final String TABLE_NAME = "receitas";
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "nome";
        public static final String KEY_CATEGORY = "categoria";
        public static final String KEY_DESCRIPTION = "descricao";
        public static final String KEY_IMAGE_PATH = "imagePath";

        public static final String CREATE_TABLE_STATEMENT =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_NAME + " TEXT NOT NULL, " +
                        KEY_CATEGORY + " TEXT NOT NULL, " +
                        KEY_DESCRIPTION + " TEXT NOT NULL, " +
                        KEY_IMAGE_PATH + " TEXT NOT NULL)";
    }
}
