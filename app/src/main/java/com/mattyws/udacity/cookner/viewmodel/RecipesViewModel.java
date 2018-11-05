package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.mattyws.udacity.cookner.database.CooknerRepository;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Recipe;

import java.util.List;

public class RecipesViewModel extends ViewModel {

    CooknerRepository mRepository;
    private LiveData<List<Recipe>> mAllUserRecipes;

    public RecipesViewModel(Context context, String userId) {
        mRepository = new CooknerRepository(context);
        mAllUserRecipes = mRepository.getAllUserRecipes(userId);
    }

    public LiveData<List<Recipe>> getAllUserRecipes() {
        return mAllUserRecipes;
    }

    public LiveData<Recipe> getRecipeById(long id) {
        return mRepository.getRecipeById(id);
    }
    public LiveData<List<Picture>> getRecipePictures(long recipeId){return mRepository.getRecipePictures(recipeId);}

    public void delete(Recipe recipe) {
        mRepository.delete(recipe);
    }
}
