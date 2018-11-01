package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mattyws.udacity.cookner.database.AppDatabase;
import com.mattyws.udacity.cookner.database.RecipeRepository;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.database.entities.Step;

import java.util.List;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository mRepository;
    private LiveData<Recipe> recipeLiveData;
    private LiveData<List<Ingredient>> recipeIngredients;
    private LiveData<List<Step>> recipeSteps;
    private LiveData<List<Picture>> recipePictures;

    public RecipeViewModel(Application application, long recipeId) {
        mRepository = new RecipeRepository(application);
        AppDatabase db = AppDatabase.getInstance(application);
        recipeLiveData = mRepository.getRecipeById(recipeId);
        recipeIngredients = mRepository.getRecipeIngredients(recipeId);
        recipePictures = mRepository.getRecipePictures(recipeId);
        recipeSteps = mRepository.getRecipeSteps(recipeId);
    }

    public void insertNewIngredient(Ingredient ingredient){mRepository.insert(ingredient);}
    public void insertNewStep(Step step){mRepository.insert(step);}
    public void insertNewPicture(Picture picture){mRepository.insert(picture);}


    public LiveData<Recipe> getRecipeLiveData() {
        return recipeLiveData;
    }
    public LiveData<List<Ingredient>> getRecipeIngredientsLiveData() {
        return recipeIngredients;
    }
    public LiveData<List<Step>> getRecipeStepsLiveData() {
        return recipeSteps;
    }

    public LiveData<List<Picture>> getRecipePictures() {
        return recipePictures;
    }
}
