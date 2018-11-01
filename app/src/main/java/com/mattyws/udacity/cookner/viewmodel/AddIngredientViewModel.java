package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mattyws.udacity.cookner.database.AppDatabase;
import com.mattyws.udacity.cookner.database.RecipeRepository;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.database.entities.Step;

import java.util.List;

public class AddIngredientViewModel extends ViewModel {

    private RecipeRepository mRepository;
    private LiveData<Ingredient> ingredientLiveData;

    public AddIngredientViewModel(Application application, long ingredientId) {
        mRepository = new RecipeRepository(application);
        ingredientLiveData = mRepository.getIngredientById(ingredientId);
    }

    public LiveData<Ingredient> getIngredientLiveData() {
        return ingredientLiveData;
    }
    public void insert(Ingredient ingredient){mRepository.insert(ingredient);}
    public void update(Ingredient ingredient){mRepository.update(ingredient);}


}
