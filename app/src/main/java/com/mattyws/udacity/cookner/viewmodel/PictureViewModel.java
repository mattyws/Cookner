package com.mattyws.udacity.cookner.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mattyws.udacity.cookner.database.RecipeRepository;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Step;

import java.util.List;

public class PictureViewModel extends ViewModel {

    private RecipeRepository mRepository;
    private LiveData<List<Picture>> mPictures;

    public PictureViewModel(Application application, long recipeId) {
        mRepository = new RecipeRepository(application);
        mPictures = mRepository.getRecipePictures(recipeId);
    }

    public LiveData<List<Picture>> getRecipePictures() {
        return mPictures;
    }

    public void insert(Picture picture){mRepository.insert(picture);}

    public void update(Picture picture){mRepository.update(picture);}


}
