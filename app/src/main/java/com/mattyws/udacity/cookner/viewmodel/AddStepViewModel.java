package com.mattyws.udacity.cookner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.mattyws.udacity.cookner.database.AppDatabase;
import com.mattyws.udacity.cookner.database.CooknerRepository;
import com.mattyws.udacity.cookner.database.entities.Step;

public class AddStepViewModel extends ViewModel {

    private CooknerRepository mRepository;
    private LiveData<Step> stepLiveData;

    public AddStepViewModel(Context context, long stepId) {
        mRepository = new CooknerRepository(context);
        AppDatabase db = AppDatabase.getInstance(context);
        stepLiveData = mRepository.getStepById(stepId);
    }

    public LiveData<Step> getStepLiveData() {
        return stepLiveData;
    }

    public void insert(Step step){mRepository.insert(step);}

    public void update(Step step){mRepository.update(step);}


}
