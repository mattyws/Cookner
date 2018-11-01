package com.mattyws.udacity.cookner.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class, parentColumns = "id", childColumns = "recipe_id", onDelete = CASCADE),
        indices = {@Index(value = "recipe_id")})
public class Picture {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String filename;
    public String absoluteFilepath;
    @ColumnInfo(name = "recipe_id")
    public long recipeId;


    public Picture() {
    }

    public Picture(long id, String filename, String absoluteFilepath, long recipeId) {
        this.id = id;
        this.filename = filename;
        this.absoluteFilepath = absoluteFilepath;
        this.recipeId = recipeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAbsoluteFilepath() {
        return absoluteFilepath;
    }

    public void setAbsoluteFilepath(String absoluteFilepath) {
        this.absoluteFilepath = absoluteFilepath;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
}
