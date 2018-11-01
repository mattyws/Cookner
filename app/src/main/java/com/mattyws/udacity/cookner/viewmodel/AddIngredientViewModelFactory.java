package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

public class AddIngredientViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context mContext;
    private final long mIngredientId;

    public AddIngredientViewModelFactory(Context context, long ingredientId) {
        mContext = context;
        mIngredientId = ingredientId;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddIngredientViewModel(mContext, mIngredientId);
    }
}
