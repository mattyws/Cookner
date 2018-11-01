package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mattyws.udacity.cookner.database.AppDatabase;
import com.mattyws.udacity.cookner.database.RecipeRepository;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Step;

public class AddStepViewModel extends ViewModel {

    private RecipeRepository mRepository;
    private LiveData<Step> stepLiveData;

    public AddStepViewModel(Application application, long stepId) {
        mRepository = new RecipeRepository(application);
        AppDatabase db = AppDatabase.getInstance(application);
        stepLiveData = mRepository.getStepById(stepId);
    }

    public LiveData<Step> getStepLiveData() {
        return stepLiveData;
    }

    public void insert(Step step){mRepository.insert(step);}

    public void update(Step step){mRepository.update(step);}


}