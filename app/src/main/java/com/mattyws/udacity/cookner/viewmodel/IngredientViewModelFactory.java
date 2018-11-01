package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class IngredientViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application mContext;
    private final long mRecipeId;

    public IngredientViewModelFactory(Application application, long recipeId) {
        mContext = application;
        mRecipeId = recipeId;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new IngredientViewModel(mContext, mRecipeId);
    }
}
