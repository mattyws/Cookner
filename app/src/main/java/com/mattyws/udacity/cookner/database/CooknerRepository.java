package com.mattyws.udacity.cookner.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.mattyws.udacity.cookner.database.dao.IngredientDAO;
import com.mattyws.udacity.cookner.database.dao.PictureDAO;
import com.mattyws.udacity.cookner.database.dao.RecipeDAO;
import com.mattyws.udacity.cookner.database.dao.StepDAO;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.database.entities.Step;

import java.util.List;

public class CooknerRepository {

    private RecipeDAO mRecipeDAO;
    private IngredientDAO mIngredientDAO;
    private StepDAO mStepsDAO;
    private PictureDAO mPictureDAO;
    private LiveData<List<Recipe>> mAllUserRecipes;
    private InsertDataListener mListener;

    public interface InsertDataListener{
        void performOperation(Long newItemId);
    }

    public CooknerRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        mRecipeDAO = db.recipeDAO();
        mIngredientDAO = db.ingredientsDAO();
        mStepsDAO = db.stepDAO();
        mPictureDAO = db.pictureDAO();
    }

    public CooknerRepository(Application application, InsertDataListener listener) {
        AppDatabase db = AppDatabase.getInstance(application);
        mRecipeDAO = db.recipeDAO();
        mIngredientDAO = db.ingredientsDAO();
        mStepsDAO = db.stepDAO();
        mPictureDAO = db.pictureDAO();
        this.mListener = listener;
    }

    public LiveData<Recipe> getRecipeById(long recipeId) {
        return mRecipeDAO.loadRecipeById(recipeId);
    }

    public LiveData<Ingredient> getIngredientById(long ingredientId){
        return mIngredientDAO.getIngredientById(ingredientId);
    }

    public LiveData<Step> getStepById(long stepId) {
        return mStepsDAO.getStepById(stepId);
    }

    public LiveData<Picture> getPictureById(long pictureId){return mPictureDAO.getPictureById(pictureId);}

    public LiveData<List<Ingredient>> getRecipeIngredients(long recipeId) {
        return mIngredientDAO.getRecipeIngredients(recipeId);
    }

    public LiveData<List<Step>> getRecipeSteps(long recipeId) {
        return mStepsDAO.getRecipeSteps(recipeId);
    }

    public LiveData<List<Picture>> getRecipePictures(long recipeId){return mPictureDAO.getRecipePictures(recipeId);}

    public LiveData<List<Recipe>> getAllUserRecipes(String userId) {
        return mRecipeDAO.getUserRecipes(userId);
    }

    public void insert(Recipe recipe){
        new InsertRecipeAsyncTask(mRecipeDAO, mListener).execute(recipe);
    }

    public void insert(Ingredient ingredient){
        new InsertIngredientAsyncTask(mIngredientDAO, mListener).execute(ingredient);
    }

    public void insert(Step step){
        new InsertStepAsyncTask(mStepsDAO, mListener).execute(step);
    }

    public void insert(Picture picture) {new InsertPictureAsyncTask(mPictureDAO, mListener).execute(picture);}

    public void update(Ingredient ingredient){
        new AsyncTask<Ingredient, Void, Void>(){

            @Override
            protected Void doInBackground(Ingredient... ingredients) {
                mIngredientDAO.update(ingredients[0]);
                return null;
            }
        }.execute(ingredient);
    }

    public void update(Picture picture){
        new AsyncTask<Picture, Void, Void>(){

            @Override
            protected Void doInBackground(Picture... pictures) {
                mPictureDAO.update(pictures[0]);
                return null;
            }
        }.execute(picture);
    }

    public void update(Recipe recipe){
        new AsyncTask<Recipe, Void, Void>(){

            @Override
            protected Void doInBackground(Recipe... recipes) {
                mRecipeDAO.update(recipes[0]);
                return null;
            }
        }.execute(recipe);
    }

    public void update(Step step){
        new AsyncTask<Step, Void, Void>(){

            @Override
            protected Void doInBackground(Step... steps) {
                mStepsDAO.update(steps[0]);
                return null;
            }
        }.execute(step);
    }

    public void delete(Ingredient ingredient){
        new AsyncTask<Ingredient, Void, Void>(){

            @Override
            protected Void doInBackground(Ingredient... ingredients) {
                mIngredientDAO.delete(ingredients[0]);
                return null;
            }
        }.execute(ingredient);
    }

    public void delete(Recipe recipe){
        new AsyncTask<Recipe, Void, Void>(){

            @Override
            protected Void doInBackground(Recipe... recipes) {
                mRecipeDAO.delete(recipes[0]);
                return null;
            }
        }.execute(recipe);
    }

    public void delete(Step step){
        new AsyncTask<Step, Void, Void>(){

            @Override
            protected Void doInBackground(Step... steps) {
                mStepsDAO.delete(steps[0]);
                return null;
            }
        }.execute(step);
    }

    public void delete(Picture picture){
        new AsyncTask<Picture, Void, Void>(){

            @Override
            protected Void doInBackground(Picture... pictures) {
                mPictureDAO.delete(pictures[0]);
                return null;
            }
        }.execute(picture);
    }

    private static class InsertRecipeAsyncTask extends AsyncTask<Recipe, Void, Long> {

        private RecipeDAO mAsyncTaskDao;
        private InsertDataListener mListener;

        public InsertRecipeAsyncTask(RecipeDAO dao, InsertDataListener listener) {
            mAsyncTaskDao = dao;
            this.mListener = listener;
        }

        @Override
        protected Long doInBackground(final Recipe... params) {
            long newItemId = mAsyncTaskDao.insert(params[0]);
            return newItemId;
        }

        @Override
        protected void onPostExecute(Long newItemId) {
            super.onPostExecute(newItemId);
            if(mListener != null)
                mListener.performOperation(newItemId);
        }
    }

    private static class InsertPictureAsyncTask extends AsyncTask<Picture, Void, Long> {

        private PictureDAO mAsyncTaskDao;
        private InsertDataListener mListener;

        public InsertPictureAsyncTask(PictureDAO dao, InsertDataListener listener) {
            mAsyncTaskDao = dao;
            this.mListener = listener;
        }

        @Override
        protected Long doInBackground(final Picture... params) {
            long newItemId = mAsyncTaskDao.insert(params[0]);
            return newItemId;
        }

        @Override
        protected void onPostExecute(Long newItemId) {
            super.onPostExecute(newItemId);
            if(mListener != null)
                mListener.performOperation(newItemId);
        }
    }

    private static class InsertIngredientAsyncTask extends AsyncTask<Ingredient, Void, Long> {

        private InsertDataListener mListener;
        private IngredientDAO mAsyncTaskDao;

        public InsertIngredientAsyncTask(IngredientDAO dao, InsertDataListener listener) {
            mAsyncTaskDao = dao;
            this.mListener = listener;
        }

        @Override
        protected Long doInBackground(final Ingredient... params) {
            long newItemId = mAsyncTaskDao.insert(params[0]);
            return newItemId;
        }

        @Override
        protected void onPostExecute(Long newItemId) {
            super.onPostExecute(newItemId);
            if(mListener != null)
                mListener.performOperation(newItemId);
        }
    }

    private static class InsertStepAsyncTask extends AsyncTask<Step, Void, Long> {

        private InsertDataListener mListener;
        private StepDAO mAsyncTaskDao;

        public InsertStepAsyncTask(StepDAO dao, InsertDataListener listener) {
            mAsyncTaskDao = dao;
            this.mListener = listener;
        }

        @Override
        protected Long doInBackground(final Step... params) {
            long newItemId = mAsyncTaskDao.insert(params[0]);
            return newItemId;
        }

        @Override
        protected void onPostExecute(Long newItemId) {
            super.onPostExecute(newItemId);
            if(mListener != null)
                mListener.performOperation(newItemId);
        }
    }
}
