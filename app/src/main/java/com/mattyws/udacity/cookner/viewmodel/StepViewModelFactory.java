package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

public class StepViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context mContext;
    private final long mRecipeId;

    public StepViewModelFactory(Context context, long recipeId) {
        mContext = context;
        mRecipeId = recipeId;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new StepViewModel(mContext, mRecipeId);
    }
}
