package com.mattyws.udacity.cookner.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Step;

import java.util.List;

@Dao
public interface StepDAO {

    @Query("SELECT * FROM step WHERE step.recipe_id == :recipeID")
    LiveData<List<Step>> getRecipeSteps(long recipeID);

    @Query("SELECT * FROM step WHERE step.id == :stepId")
    LiveData<Step> getStepById(long stepId);

    @Insert
    long insert(Step step);

    @Update
    void update(Step step);

    @Delete
    void delete(Step step);
}
