package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

public class AddStepViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context mContext;
    private final long mStepId;

    public AddStepViewModelFactory(Context context, long stepId) {
        mContext = context;
        mStepId = stepId;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddStepViewModel(mContext, mStepId);
    }
}
