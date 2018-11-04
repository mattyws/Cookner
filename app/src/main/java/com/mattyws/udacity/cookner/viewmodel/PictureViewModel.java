package com.mattyws.udacity.cookner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.mattyws.udacity.cookner.database.CooknerRepository;
import com.mattyws.udacity.cookner.database.entities.Picture;

import java.util.List;

public class PictureViewModel extends ViewModel {

    private CooknerRepository mRepository;
    private LiveData<List<Picture>> mPictures;

    public PictureViewModel(Context context, long recipeId) {
        mRepository = new CooknerRepository(context);
        mPictures = mRepository.getRecipePictures(recipeId);
    }

    public LiveData<List<Picture>> getRecipePictures() {
        return mPictures;
    }

    public void insert(Picture picture){mRepository.insert(picture);}

    public void update(Picture picture){mRepository.update(picture);}


}
