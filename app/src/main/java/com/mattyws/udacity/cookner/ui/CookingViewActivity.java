package com.mattyws.udacity.cookner.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Step;
import com.mattyws.udacity.cookner.ui.fragments.IngredientListFragment;
import com.mattyws.udacity.cookner.ui.fragments.StepsPagerFragment;
import com.mattyws.udacity.cookner.viewmodel.RecipeViewModel;
import com.mattyws.udacity.cookner.viewmodel.RecipeViewModelFactory;

import java.util.List;

public class CookingViewActivity extends AppCompatActivity

{

    public static final String RECIPE_ID = "recipe.id";
    private static final long DEFAULT_RECIPE_ID = -1;
    private long mRecipeId = DEFAULT_RECIPE_ID;
    private RecipeViewModel mRecipeViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_view);

        Intent intent = getIntent();
        if(!intent.hasExtra(RECIPE_ID)) finish();
        mRecipeId = intent.getLongExtra(RECIPE_ID, DEFAULT_RECIPE_ID);

        RecipeViewModelFactory factory = new RecipeViewModelFactory(this, mRecipeId);
        mRecipeViewModel = ViewModelProviders.of(this, factory)
                .get(RecipeViewModel.class);

        IngredientListFragment mIngredientListFragment = IngredientListFragment.newInstance(mRecipeId, true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ingredient_list_holder, mIngredientListFragment)
                .commit();
        StepsPagerFragment mStepsPagerFragment = StepsPagerFragment.newInstance(mRecipeId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.step_content_holder, mStepsPagerFragment)
                .commit();
    }
}
