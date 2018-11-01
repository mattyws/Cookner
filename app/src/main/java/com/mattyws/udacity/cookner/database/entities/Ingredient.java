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
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String quantity;
    @ColumnInfo(name = "recipe_id")
    public long recipeId;
    public String measure;
    public String name;

    public Ingredient(long id, String quantity, long recipeId, String measure, String name) {
        this.id = id;
        this.quantity = quantity;
        this.recipeId = recipeId;
        this.measure = measure;
        this.name = name;
    }

    @Ignore
    public Ingredient(String name, String quantity, String measure) {
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
}
