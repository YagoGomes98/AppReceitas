package meu.app.apprecipes.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import meu.app.apprecipes.R;
import meu.app.apprecipes.adapters.AdapterBanco;
import meu.app.apprecipes.models.Auxiliar;
import meu.app.apprecipes.models.Ingrediente;
import meu.app.apprecipes.models.Receita;
import meu.app.apprecipes.ui.login.fragmentos.NavigableFragment;
import meu.app.apprecipes.ui.login.fragmentos.RecipeDirectionsFragment;
import meu.app.apprecipes.ui.login.fragmentos.RecipeImageFragment;
import meu.app.apprecipes.ui.login.fragmentos.RecipeIngredientsFragment;
import meu.app.apprecipes.utils.Files;
import meu.app.apprecipes.utils.Resultados;

public class CriarReceitaActivity extends AppCompatActivity implements
        RecipeImageFragment.ImageListener, RecipeDirectionsFragment.DirectionsListener,
        RecipeIngredientsFragment.IngredientListener {
    private static final int REQUEST_OPEN_GALLERY = 10;
    private static final int REQUEST_TO_ACCESS_GALLERY = 11;

    private Receita currentRecipe;
    private boolean isUpdating;
    private String currentCategory;
    private AdapterBanco databaseAdapter;

    private Button nextButton;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        databaseAdapter = AdapterBanco.getInstance(this);

        Intent intent = getIntent();
        isUpdating = intent.getBooleanExtra("isUpdating", false);
        if (isUpdating)
            currentRecipe = intent.getParcelableExtra("recipe");
        else {
            currentCategory = intent.getStringExtra("category");
            currentRecipe = new Receita(currentCategory);
        }

        initializeUI();
        displayFragment(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment instanceof RecipeIngredientsFragment)
            displayFragment(0);
        else if (fragment instanceof RecipeDirectionsFragment)
            displayFragment(1);
        else
            super.onBackPressed();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_OPEN_GALLERY:
                switch (resultCode) {
                    case RESULT_OK:
                        Uri imageData = data.getData();
                        String imageSrc = Files.getRealPathFromURI(this, imageData);
                        ((RecipeImageFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container))
                                .onImageSelected(imageSrc);

                        currentRecipe.setImagePath(imageSrc);
                        break;
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_TO_ACCESS_GALLERY:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openGallery();
                else
                    Toast.makeText(this, "Permiss??o de acesso a galeria foi negado.", Toast.LENGTH_LONG)
                            .show();
                break;
        }
    }

    private void displayFragment(int position) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        String nextButtonText = "";

        switch (position) {
            case 0:
                fragment = RecipeImageFragment.newInstance(currentRecipe);
                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                nextButtonText = "NEXT";
                break;
            case 1:
                fragment = RecipeIngredientsFragment.newInstance(currentRecipe);
                ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
                nextButtonText = "NEXT";
                break;
            case 2:
                fragment = RecipeDirectionsFragment.newInstance(currentRecipe);
                ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
                nextButtonText = "Finish";
                break;
        }

        nextButton.setText(nextButtonText);

        ft.replace(R.id.frame_container, fragment, "fragment" + position);
        ft.commit();
    }

    public void onNext(View view) {
        ((NavigableFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container)).onNext();
    }

    private void initializeUI() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nextButton = findViewById(R.id.nextButton);
    }

    private void setSupportActionBar(Toolbar mToolbar) {
    }

    @Override
    public void navigateToIngredientsFragment(String name, String description) {
        currentRecipe.setName(name);
        currentRecipe.setDescription(description);
        displayFragment(1);
    }

    @Override
    public void navigateToDirectionsFragment(List<Ingrediente> ingredients) {
        currentRecipe.setIngredientes(ingredients);
        displayFragment(2);
    }

    @Override
    public void onSelectImage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_TO_ACCESS_GALLERY);
        } else {
            openGallery();
        }
    }

    @Override
    public void onStepsFinished(List<Auxiliar> directions) {
        currentRecipe.setAuxiliar(directions);
        if (isUpdating)
            databaseAdapter.updateRecipe(currentRecipe);
        else
            databaseAdapter.addNewRecipe(currentRecipe);

        Log.i("CreateRecipeActivity", "Final recipe: " + currentRecipe);
        setResult(isUpdating ? Resultados.RECIPE_EDITED : Resultados.RECIPE_ADDED);
        finish();
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, REQUEST_OPEN_GALLERY);
    }
}
