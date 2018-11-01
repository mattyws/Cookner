package com.mattyws.udacity.cookner.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

import com.mattyws.udacity.cookner.database.dao.IngredientDAO;
import com.mattyws.udacity.cookner.database.dao.PictureDAO;
import com.mattyws.udacity.cookner.database.dao.RecipeDAO;
import com.mattyws.udacity.cookner.database.dao.StepDAO;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.database.entities.Step;


@Database(entities = {Recipe.class, Step.class, Ingredient.class, Picture.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "cookner";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract RecipeDAO recipeDAO();
    public abstract IngredientDAO ingredientsDAO();
    public abstract StepDAO stepDAO();
    public abstract PictureDAO pictureDAO();
}
