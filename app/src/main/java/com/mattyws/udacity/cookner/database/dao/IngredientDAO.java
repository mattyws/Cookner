package com.mattyws.udacity.cookner.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mattyws.udacity.cookner.database.entities.Ingredient;

import java.util.List;

@Dao
public interface IngredientDAO {

    @Query("SELECT * FROM ingredient WHERE ingredient.recipe_id == :recipeId")
    LiveData<List<Ingredient>> getRecipeIngredients(long recipeId);

    @Query("SELECT * FROM ingredient WHERE ingredient.id == :ingredientId")
    LiveData<Ingredient> getIngredientById(long ingredientId);

    @Insert
    long insert(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);

    @Update
    void update(Ingredient ingredient);
}
