package com.mattyws.udacity.cookner.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mattyws.udacity.cookner.database.entities.Recipe;

import java.util.List;

@Dao
public interface RecipeDAO {

    @Query("SELECT * FROM recipe WHERE user_id = :userId")
    LiveData<List<Recipe>> getUserRecipes(String userId);

    @Insert
    long insert(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Query("SELECT * FROM recipe WHERE recipe.id = :recipeId")
    LiveData<Recipe> loadRecipeById(long recipeId);
}
