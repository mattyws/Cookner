package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mattyws.udacity.cookner.database.RecipeRepository;
import com.mattyws.udacity.cookner.database.entities.Ingredient;

import java.util.List;

public class IngredientViewModel extends ViewModel {

    private RecipeRepository mRepository;
    private LiveData<List<Ingredient>> mIngredients;

    public IngredientViewModel(Application application, long recipeId) {
        mRepository = new RecipeRepository(application);
        mIngredients = mRepository.getRecipeIngredients(recipeId);
    }

    public LiveData<List<Ingredient>> getRecipeIngredients() {
        return mIngredients;
    }

    public void insert(Ingredient ingredient){mRepository.insert(ingredient);}

    public void update(Ingredient ingredient){mRepository.update(ingredient);}

    public void delete(Ingredient ingredient){mRepository.delete(ingredient);}

}
