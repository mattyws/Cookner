package com.mattyws.udacity.cookner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.mattyws.udacity.cookner.database.CooknerRepository;
import com.mattyws.udacity.cookner.database.entities.Step;

import java.util.List;

public class StepViewModel extends ViewModel {

    private CooknerRepository mRepository;
    private LiveData<List<Step>> mSteps;

    public StepViewModel(Context context, long recipeId) {
        mRepository = new CooknerRepository(context);
        mSteps = mRepository.getRecipeSteps(recipeId);
    }

    public LiveData<List<Step>> getRecipeSteps() {
        return mSteps;
    }

    public void insert(Step step){mRepository.insert(step);}

    public void update(Step step){mRepository.update(step);}


    public void delete(Step removedStep) {
        mRepository.delete(removedStep);
    }
}
