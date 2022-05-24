package meu.app.apprecipes.dao;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import meu.app.apprecipes.models.Auxiliar;

public class Banco extends SQLiteOpenHelper {

    private static final String TAG = Banco.class.getSimpleName();

    public Banco(Context context, String databaseName, int databaseVersion) {
        super (context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UsuarioDAO.Config.CREATE_TABLE_STATEMENT);
        db.execSQL(ReceitaDAO.Config.CREATE_TABLE_STATEMENT);
        db.execSQL(AuxiliarDAO.Config.CREATE_TABLE_STATEMENT);
        db.execSQL(IngredienteDAO.Config.CREATE_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion +
                " to " + newVersion + ", which will destroy all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + UsuarioDAO.Config.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AuxiliarDAO.Config.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReceitaDAO.Config.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IngredienteDAO.Config.TABLE_NAME);
        onCreate(db);
    }
}
