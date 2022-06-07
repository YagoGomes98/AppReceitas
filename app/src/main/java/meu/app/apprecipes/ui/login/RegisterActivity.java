package meu.app.apprecipes.ui.login;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;


import meu.app.apprecipes.R;
import meu.app.apprecipes.adapters.AdapterBanco;
import meu.app.apprecipes.models.Usuario;


public class RegisterActivity extends ToolbarActivity {

    private AdapterBanco databaseAdapter;

    private TextInputLayout mUsername;
    private TextInputLayout mFullname;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseAdapter = AdapterBanco.getInstance(this);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Criar conta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsername = findViewById(R.id.reg_display_name);
        mFullname = findViewById(R.id.login_fullname);
        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCreateAccountPressed(View view) {
        String username = mUsername.getEditText().getText().toString();
        String fullname = mFullname.getEditText().getText().toString();
        String email = mEmail.getEditText().getText().toString();
        String password = mPassword.getEditText().getText().toString();

        registerUser(new Usuario(username, fullname, email, password));
    }

    private void registerUser(Usuario user) {
        databaseAdapter.addNewUser(user);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
