package com.mattyws.udacity.cookner.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Picture;

import java.util.List;

@Dao
public interface PictureDAO {

    @Query("SELECT * FROM picture WHERE picture.recipe_id == :recipeId")
    LiveData<List<Picture>> getRecipePictures(long recipeId);

    @Query("SELECT * FROM picture WHERE picture.id == :ingredientId")
    LiveData<Picture> getPictureById(long ingredientId);

    @Insert
    long insert(Picture picture);

    @Delete
    void delete(Picture picture);

    @Update
    void update(Picture picture);
}
