package com.mattyws.udacity.cookner.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.ui.fragments.IngredientListFragment;
import com.mattyws.udacity.cookner.ui.fragments.StepsPagerFragment;

public class CookingViewActivity extends AppCompatActivity {

    public static final String RECIPE_ID = "recipe.id";
    private static final long DEFAULT_RECIPE_ID = -1;
    private long mRecipeId = DEFAULT_RECIPE_ID;

    private IngredientListFragment mIngredientListFragment;
    private StepsPagerFragment mStepsPagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_view);

        Intent intent = getIntent();
        if(!intent.hasExtra(RECIPE_ID)) finish();
        mRecipeId = intent.getLongExtra(RECIPE_ID, DEFAULT_RECIPE_ID);
        mIngredientListFragment = new IngredientListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ingredient_list_holder, mIngredientListFragment)
                .commit();
        mStepsPagerFragment = new StepsPagerFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.step_content_holder, mStepsPagerFragment)
                .commit();
    }

//    @Override
//    public void onAttachFragment(Fragment fragment) {
//        super.onAttachFragment(fragment);
//        if (fragment == mIngredientListFragment){
//            mIngredientListFragment.fetchAndPopulateRecipeIngredient(mRecipeId);
//        } else if(fragment == mStepsPagerFragment){
//            mStepsPagerFragment.fetchAndPopulateRecipePictures(mRecipeId);
//        }
//    }
}
