package com.mattyws.udacity.cookner.ui.adapters;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Step;
import com.mattyws.udacity.cookner.ui.fragments.IngredientListFragment;
import com.mattyws.udacity.cookner.ui.fragments.StepListFragment;
import com.mattyws.udacity.cookner.viewmodel.RecipeViewModel;

import java.util.List;

public class RecipeFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = RecipeFragmentPagerAdapter.class.getCanonicalName();
    private final Context mContext;

    public RecipeFragmentPagerAdapter(Context context, FragmentManager manager){
        super(manager);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        Log.d(TAG, "getItem: " + i);
        switch (i){
            case 0:
                return IngredientListFragment.newInstance();
            case 1:
                return StepListFragment.newInstance();
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getResources().getString(R.string.ingredient_tab_name);
            case 1:
                return mContext.getResources().getString(R.string.step_tab_name);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
