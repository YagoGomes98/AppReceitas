package meu.app.apprecipes.dao;
import meu.app.apprecipes.models.Usuario;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class UsuarioDAO {

    private SQLiteDatabase bd;
    public UsuarioDAO(SQLiteDatabase bd) { this.bd = bd; }

    public void inserir (Usuario usuario){
        ContentValues valores = new ContentValues();
        valores.put(Config.KEY_USERNAME, usuario.getNomeUsuario());
        valores.put(Config.KEY_FULLNAME, usuario.getNomeCompleto());
        valores.put(Config.KEY_EMAIL, usuario.getEmail());
        valores.put(Config.KEY_PASSWORD, usuario.getSenha());

        inserir(valores);

    }
    private long inserir (ContentValues valores) {
        return bd.insert(Config.TABLE_NAME, null, valores);
    }

    public Usuario getUserByEmailAndPassword(String email, String senha) {
        try (Cursor cursor = bd.query(
                true,
                Config.TABLE_NAME,
                new String[]{Config.KEY_ID, Config.KEY_USERNAME, Config.KEY_FULLNAME, Config.KEY_EMAIL, Config.KEY_PASSWORD},
                Config.KEY_EMAIL + "=? AND " + Config.KEY_PASSWORD + "=?",
                new String[]{email, senha},
                null, null, null, null)) {
            return extrairUsuarioCursor(cursor);
        }
    }
    private Usuario extrairUsuarioCursor(Cursor cursor) {
        if (cursor.moveToFirst()) {
            return new Usuario(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        }
        return null;
    }
        public static class Config {
            public static final String TABLE_NAME = "users";
            public static final String KEY_ID = "id";
            public static final String KEY_USERNAME = "username";
            public static final String KEY_FULLNAME = "fullname";
            public static final String KEY_EMAIL = "email";
            public static final String KEY_PASSWORD = "password";

            public static final String CREATE_TABLE_STATEMENT =
                    "CREATE TABLE " + TABLE_NAME + " (" +
                            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_USERNAME + " TEXT NOT NULL, " +
                            KEY_FULLNAME + " TEXT NOT NULL, " +
                            KEY_EMAIL + " TEXT NOT NULL, " +
                            KEY_PASSWORD + " TEXT NOT NULL)";
        }
}
