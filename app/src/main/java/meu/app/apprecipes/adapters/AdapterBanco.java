package meu.app.apprecipes.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;


import meu.app.apprecipes.dao.ReceitaDAO;
import meu.app.apprecipes.dao.Banco;
import meu.app.apprecipes.dao.UsuarioDAO;
import meu.app.apprecipes.models.Receita;
import meu.app.apprecipes.models.Usuario;
import meu.app.apprecipes.utils.PreferenciaUsuario;


public class AdapterBanco {

    /**
     * The singleton instance.
     */
    private static AdapterBanco instance;
    private static final String DATABASE_NAME = "recipesDatabase";
    private static final int DATABASE_VERSION = 1;

    private Banco dbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    private UsuarioDAO userDAO;
    private ReceitaDAO recipeDAO;

    public static AdapterBanco getInstance(Context context) {
        if (instance == null) {
            synchronized (AdapterBanco.class) {
                if (instance == null)
                    instance = new AdapterBanco(context).open();
            }
        }

        return instance;
    }

    private AdapterBanco(Context context) {
        mContext = context;
        dbHelper = new Banco(context, DATABASE_NAME, DATABASE_VERSION);
    }

    private AdapterBanco open() {
        db = dbHelper.getWritableDatabase();
        userDAO = new UsuarioDAO(db);
        recipeDAO = new ReceitaDAO(db);
        return this;
    }

    public boolean signIn(String email, String password) {
        Usuario currentUser = userDAO.getUserByEmailAndPassword(email, password);
        PreferenciaUsuario.saveCurrentUser(mContext, currentUser);
        return currentUser != null;
    }

    public void addNewUser(Usuario user) {
        userDAO.inserir(user);
    }

    public long addNewRecipe(Receita recipe) {
        return recipeDAO.insert(recipe);
    }

    public void updateRecipe(Receita recipe) {
        recipeDAO.update(recipe);
    }

    public void deleteRecipe(long recipeId) {
        recipeDAO.deleteById(recipeId);
    }

    public List<Receita> getAllRecipesByCategory(String category) {
        return recipeDAO.selectAllByCategory(category);
    }
}
