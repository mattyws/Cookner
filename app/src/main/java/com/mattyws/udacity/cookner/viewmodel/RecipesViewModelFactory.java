package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class RecipesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application mContext;
    private final String mUserId;

    public RecipesViewModelFactory(Application application, String userId) {
        mContext = application;
        mUserId = userId;
    }

//    @Override
//    public <T extends ViewModel> T create(Class<T> modelClass) {
//        //noinspection unchecked
//        return (T) new RecipeViewModel(mContext, mRecipeId);
//    }
}