package meu.app.apprecipes.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import meu.app.apprecipes.models.Receita;
import meu.app.apprecipes.ui.login.fragmentos.ViewIngredientsFragment;
import meu.app.apprecipes.ui.login.fragmentos.ViewDirectionsFragment;

public class AdapterViewPager extends FragmentStateAdapter {

    private static final int TAB_COUNT = 2;
    private final Receita recipe;
    private String[] tabTitles = {"Ingredients", "Directions"};

    public AdapterViewPager(FragmentManager fm, Receita recipe) {
        super(fm);
        this.recipe = recipe;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = ViewIngredientsFragment.newInstance(recipe.getIngredients());
                break;
            case 1:
                frag = ViewIngredientsFragment.newInstance(recipe.getAuxiliar());
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}