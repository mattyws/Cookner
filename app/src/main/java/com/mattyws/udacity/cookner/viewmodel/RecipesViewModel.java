package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.mattyws.udacity.cookner.database.CooknerRepository;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Recipe;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    CooknerRepository mRepository;
    private LiveData<List<Recipe>> mAllUserRecipes;

    public RecipesViewModel(@NonNull Application context) {
        super(context);
        mRepository = new CooknerRepository(context);
        mAllUserRecipes = mRepository.getAllUserRecipes();
    }

    public LiveData<List<Recipe>> getAllUserRecipes() {
        if(mAllUserRecipes != null) {return mAllUserRecipes;}
        else{
            mAllUserRecipes = mRepository.getAllUserRecipes();
            return mAllUserRecipes;
        }
    }

    public LiveData<Recipe> getRecipeById(long id) {
        return mRepository.getRecipeById(id);
    }
    public LiveData<List<Picture>> getRecipePictures(long recipeId){return mRepository.getRecipePictures(recipeId);}

    public void delete(Recipe recipe) {
        mRepository.delete(recipe);
    }
}
