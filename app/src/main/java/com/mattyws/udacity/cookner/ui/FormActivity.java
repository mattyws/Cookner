package com.mattyws.udacity.cookner.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Step;
import com.mattyws.udacity.cookner.ui.fragments.IngredientFormFragment;
import com.mattyws.udacity.cookner.ui.fragments.StepFormFragment;
import com.mattyws.udacity.cookner.viewmodel.AddIngredientViewModel;
import com.mattyws.udacity.cookner.viewmodel.AddIngredientViewModelFactory;
import com.mattyws.udacity.cookner.viewmodel.AddStepViewModel;
import com.mattyws.udacity.cookner.viewmodel.AddStepViewModelFactory;

public class FormActivity extends AppCompatActivity implements IngredientFormFragment.IngredientFormListener,
        StepFormFragment.StepFormListener{

    //TODO: Comment

    public static final String FORMULARY = "form";
    public static final String ITEM_ID = "itemid";
    public static final String RECIPE_ID = "recipe.id";
    public static final int INGREDIENT_FORM = 1;
    public static final int STEP_FORM = 2;

    public static final long DEFAULT_ID = -1;
    private static final String TAG = FormActivity.class.getCanonicalName();

    private long mItemId = DEFAULT_ID;
    private long mRecipeId = DEFAULT_ID;

    private int requestedFormulary;

    private AddIngredientViewModel mAddIngredientViewModel;
    private AddStepViewModel mStepViewModel;

    private IngredientFormFragment mIngredientFormFragment;
    private StepFormFragment mStepFormFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.drawable.ic_add_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);

        Intent intentThatStartActivity = getIntent();

        if(intentThatStartActivity.hasExtra(ITEM_ID)){
            mItemId = intentThatStartActivity.getLongExtra(ITEM_ID, DEFAULT_ID);
        }
        if(intentThatStartActivity.hasExtra(RECIPE_ID)){
            mRecipeId = intentThatStartActivity.getLongExtra(RECIPE_ID, DEFAULT_ID);
        }

        if(intentThatStartActivity.hasExtra(FORMULARY)) {
            if (intentThatStartActivity.getIntExtra(FORMULARY, INGREDIENT_FORM) == STEP_FORM) {
                requestedFormulary = STEP_FORM;
                setupStepUi();
            } else if (intentThatStartActivity.getIntExtra(FORMULARY, INGREDIENT_FORM) == INGREDIENT_FORM) {
                requestedFormulary = INGREDIENT_FORM;
                setupIngredientUi();
            }
        }

    }

    private void setupStepUi() {
        mStepFormFragment = new StepFormFragment();
        AddStepViewModelFactory factory = new AddStepViewModelFactory(getApplication(), mItemId);
        mStepViewModel = ViewModelProviders.of(this, factory)
                .get(AddStepViewModel.class);
        if(mItemId == DEFAULT_ID) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.form_fragment, mStepFormFragment)
                    .commit();
        } else {
            mStepViewModel.getStepLiveData().observe(this, new Observer<Step>() {
                @Override
                public void onChanged(@Nullable Step step) {
                    mStepViewModel.getStepLiveData().removeObserver(this);
                    mStepFormFragment.setStep(step);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.form_fragment, mStepFormFragment)
                            .commit();
                }
            });
        }
    }

    private void setupIngredientUi(){
        mIngredientFormFragment = new IngredientFormFragment();
        AddIngredientViewModelFactory factory = new AddIngredientViewModelFactory(getApplication(), mItemId);
        mAddIngredientViewModel = ViewModelProviders.of(this, factory)
                .get(AddIngredientViewModel.class);
        if(mItemId == DEFAULT_ID) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.form_fragment, mIngredientFormFragment)
                    .commit();
        } else {

            mAddIngredientViewModel.getIngredientLiveData().observe(this, new Observer<Ingredient>() {
                @Override
                public void onChanged(@Nullable Ingredient ingredient) {
                    mAddIngredientViewModel.getIngredientLiveData().removeObserver(this);
                    mIngredientFormFragment.setIngredient(ingredient);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.form_fragment, mIngredientFormFragment)
                            .commit();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_ingredient_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(R.id.menu_action_cooking_mode == id) {
            switch (requestedFormulary){
                case INGREDIENT_FORM:
                    persistIngredient();
                    break;
                case STEP_FORM:
                    persistStep();
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void persistIngredient() {
        if(checkInput()) {
            String name = mIngredientFormFragment.mDataBinding.ingredientNameEt.getText().toString();
            String quantity = mIngredientFormFragment.mDataBinding.ingredientQuantityEt.getText().toString();
            String measure = mIngredientFormFragment.mDataBinding.ingredientMeasureEt.getText().toString();
            Ingredient ingredient;
            if(mIngredientFormFragment.getIngredient() != null){
                ingredient = mIngredientFormFragment.getIngredient();
                ingredient.setName(name);
                ingredient.setQuantity(quantity);
                ingredient.setMeasure(measure);
                mAddIngredientViewModel.update(ingredient);
            } else {
                ingredient = new Ingredient(name, quantity, measure);
                ingredient.setRecipeId(mRecipeId);
                mAddIngredientViewModel.insert(ingredient);
            }
        }
        finish();
    }

    private void persistStep(){
        if(checkInput()){
            String stepDescription = mStepFormFragment.mDataBinding.stepDescriptionEt.getText().toString();
            Step step;
            if(mStepFormFragment.getStep() != null){
                step = mStepFormFragment.getStep();
                step.setDescription(stepDescription);
                mStepViewModel.update(step);
            } else {
                step = new Step(stepDescription);
                step.setRecipeId(mRecipeId);
                mStepViewModel.insert(step);
            }
        }
        finish();
    }

    private boolean checkInput() {
        return true;
    }

    @Override
    public void onIngredientFormInteraction() {

    }

    @Override
    public void onStepFormInteraction() {

    }
}
