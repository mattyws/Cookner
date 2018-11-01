package com.mattyws.udacity.cookner.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "recipe_id", onDelete = CASCADE),
        indices = {@Index(value = "recipe_id")})
public class Step {
    @PrimaryKey(autoGenerate = true)
    long id;
    @ColumnInfo(name = "recipe_id")
    long recipeId;
    String description;

    public Step(long id, long recipeId, String description) {
        this.id = id;
        this.recipeId = recipeId;
        this.description = description;
    }

    @Ignore
    public Step(String stepDescription) {
        this.description = stepDescription;
    }

    @Ignore
    public Step() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
}
