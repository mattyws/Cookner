package com.mattyws.udacity.cookner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.mattyws.udacity.cookner.database.CooknerRepository;
import com.mattyws.udacity.cookner.database.entities.Ingredient;

public class AddIngredientViewModel extends ViewModel {

    private CooknerRepository mRepository;
    private LiveData<Ingredient> ingredientLiveData;

    public AddIngredientViewModel(Context context, long ingredientId) {
        mRepository = new CooknerRepository(context);
        ingredientLiveData = mRepository.getIngredientById(ingredientId);
    }

    public LiveData<Ingredient> getIngredientLiveData() {
        return ingredientLiveData;
    }
    public void insert(Ingredient ingredient){mRepository.insert(ingredient);}
    public void update(Ingredient ingredient){mRepository.update(ingredient);}


}
