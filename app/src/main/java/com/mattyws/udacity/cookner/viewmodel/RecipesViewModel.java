package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.mattyws.udacity.cookner.database.RecipeRepository;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.database.entities.Step;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    RecipeRepository mRepository;
    private LiveData<List<Recipe>> mAllUserRecipes;

    public RecipesViewModel(@NonNull Application application) {
        super(application);
        mRepository = new RecipeRepository(application);
        mAllUserRecipes = mRepository.getAllUserRecipes();
    }

    public LiveData<List<Recipe>> getAllUserRecipes() {
        return mAllUserRecipes;
    }

    public LiveData<Recipe> getRecipeById(long id) {
        return mRepository.getRecipeById(id);
    }

    public void delete(Recipe recipe) {
        mRepository.delete(recipe);
    }
}
