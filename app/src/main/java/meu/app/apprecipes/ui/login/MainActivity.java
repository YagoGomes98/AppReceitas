package meu.app.apprecipes.ui.login;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import R;
import adapters.DatabaseAdapter;

import adapters.MainPagerAdapter;
import meu.app.apprecipes.R;
import meu.app.apprecipes.adapters.AdapterBanco;
import meu.app.apprecipes.adapters.AdapterPaginaPrincipal;
import meu.app.apprecipes.models.Receita;
import meu.app.apprecipes.ui.login.fragmentos.CategorizedFragment;
import meu.app.apprecipes.utils.PreferenciaUsuario;
import meu.app.apprecipes.utils.Resultados;
import models.Recipe;
import ui.fragments.CategorizedFragment;
import utils.ActivityTransition;
import utils.ResultCodes;
import utils.UserPreferences;

public class MainActivity extends ToolbarActivity implements CategorizedFragment.CategorizedFragmentListener  {

    private static final int REQUEST_ADD_RECIPE = 1;
    private static final int REQUEST_VIEW_RECIPE = 2;
    private AdapterBanco databaseAdapter;
    private AdapterPaginaPrincipal mAdapter;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView first;
    private ImageView second;
    private ViewSwitcher mViewSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        databaseAdapter = AdapterBanco.getInstance(this);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Receitas");

        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tablayout);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        mViewSwitcher = findViewById(R.id.switcher);
        mTabLayout.bringToFront();
        mAdapter = new AdapterPaginaPrincipal(getSupportFragmentManager());


        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                @DrawableRes int image = -1;
                switch (position) {
                    case 0:
                        image = R.drawable.american;
                        break;
                    case 1:
                        image = R.drawable.vegan;
                        break;
                    case 2:
                        image = R.drawable.asian;
                        break;
                    case 3:
                        image = R.drawable.mediterranean;
                        break;
                    case 4:
                        image = R.drawable.european;
                        break;
                }

                if (first.getVisibility() == View.VISIBLE) {
                    second.setImageResource(image);
                    mViewSwitcher.showNext();
                } else {
                    first.setImageResource(image);
                    mViewSwitcher.showPrevious();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_recipe:
                Intent intent = new Intent(this, CriarReceitaActivity.class);
                intent.putExtra("category", getCurrentlyDisplayedCategory());
                startActivityForResult(intent, REQUEST_ADD_RECIPE);
                break;
            case R.id.sign_out:
                PreferenciaUsuario.clear(this);
                navigateToLogin();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD_RECIPE:
                switch (resultCode) {
                    case Resultados.RECIPE_ADDED:
                        Snackbar.make(getWindow().getDecorView(), "Receita adicionada.", Snackbar.LENGTH_LONG)
                                .show();
                        break;
                    case Resultados.RECIPE_EDITED:
                        Snackbar.make(getWindow().getDecorView(), "Receita modificada.", Snackbar.LENGTH_LONG)
                                .show();
                        break;
                }
                break;
            case REQUEST_VIEW_RECIPE:
                switch (resultCode) {
                    case Resultados.RECIPE_SHOULD_BE_EDITED:
                        Receita recipe = data.getParcelableExtra("recipe");
                        Intent intent = new Intent(this, CriarReceitaActivity.class);
                        intent.putExtra("recipe", recipe);
                        intent.putExtra("category", recipe.getCategory());
                        intent.putExtra("isUpdating", true);
                        startActivityForResult(intent, REQUEST_ADD_RECIPE);
                        break;
                    case Resultados.RECIPE_SHOULD_BE_DELETED:
                        long recipeId = data.getLongExtra("recipeId", -1);
                        if (recipeId != -1) {
                            onDeleteRecipe(recipeId);
                            mViewPager.getAdapter().notifyDataSetChanged();
                        }
                        break;
                }
                break;
        }
    }

    private String getCurrentlyDisplayedCategory() {
        return mAdapter.getPageTitle(mViewPager.getCurrentItem()).toString();
    }

    private void navigateToLogin() {
        Intent startIntent = new Intent(this, LoginActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public void onShowRecipe(Receita recipe, Pair<View, String>[] pairs) {
        Intent intent = new Intent(this, VerReceitaActivity.class);
        intent.putExtra("recipe", recipe);

        ActivityTransition.startActivityForResultWithSharedElement(
                this, intent, pairs[0].first, pairs[0].second, REQUEST_VIEW_RECIPE);
    }

    @Override
    public void onEditRecipe(Receita recipe) {
        Intent intent = new Intent(this, CriarReceitaActivity.class);
        intent.putExtra("recipe", recipe);
        intent.putExtra("category", getCurrentlyDisplayedCategory());
        intent.putExtra("isUpdating", true);
        startActivityForResult(intent, REQUEST_ADD_RECIPE);
        registerForActivityResult(REQUEST_ADD_RECIPE, intent);
    }

    @Override
    public void onDeleteRecipe(long recipeId) {
        databaseAdapter.deleteRecipe(recipeId);
        Snackbar.make(getWindow().getDecorView(), "Receita deletada.", Snackbar.LENGTH_LONG).show();
    }
}
