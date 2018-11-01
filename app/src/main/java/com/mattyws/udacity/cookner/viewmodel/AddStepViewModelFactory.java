package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class AddStepViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application mContext;
    private final long mStepId;

    public AddStepViewModelFactory(Application application, long stepId) {
        mContext = application;
        mStepId = stepId;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddStepViewModel(mContext, mStepId);
    }
}
