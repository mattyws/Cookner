package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class AddIngredientViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application mContext;
    private final long mIngredientId;

    public AddIngredientViewModelFactory(Application application, long ingredientId) {
        mContext = application;
        mIngredientId = ingredientId;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddIngredientViewModel(mContext, mIngredientId);
    }
}
