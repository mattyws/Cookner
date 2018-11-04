package com.mattyws.udacity.cookner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.mattyws.udacity.cookner.database.CooknerRepository;
import com.mattyws.udacity.cookner.database.entities.Ingredient;

import java.util.List;

public class IngredientViewModel extends ViewModel {

    private CooknerRepository mRepository;
    private LiveData<List<Ingredient>> mIngredients;

    public IngredientViewModel(Context context, long recipeId) {
        mRepository = new CooknerRepository(context);
        mIngredients = mRepository.getRecipeIngredients(recipeId);
    }

    public LiveData<List<Ingredient>> getRecipeIngredients() {
        return mIngredients;
    }

    public void insert(Ingredient ingredient){mRepository.insert(ingredient);}

    public void update(Ingredient ingredient){mRepository.update(ingredient);}

    public void delete(Ingredient ingredient){mRepository.delete(ingredient);}

}
