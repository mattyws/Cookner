package com.mattyws.udacity.cookner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.mattyws.udacity.cookner.database.CooknerRepository;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.database.entities.Step;

import java.util.List;

public class RecipeViewModel extends ViewModel {

    private CooknerRepository mRepository;
    private LiveData<Recipe> recipeLiveData;
    private LiveData<List<Ingredient>> recipeIngredients;
    private LiveData<List<Step>> recipeSteps;
    private LiveData<List<Picture>> recipePictures;

    public RecipeViewModel(Context context, long recipeId) {
        mRepository = new CooknerRepository(context);
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

    public void delete(Step deletedStep) {mRepository.delete(deletedStep);}

    public void delete(Ingredient removedIngredient) {mRepository.delete(removedIngredient);}
}
