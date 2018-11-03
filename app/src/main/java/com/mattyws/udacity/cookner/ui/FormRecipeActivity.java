package com.mattyws.udacity.cookner.ui;

import android.Manifest;
import android.animation.Animator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.RecipeRepository;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.database.entities.Step;
import com.mattyws.udacity.cookner.ui.fragments.ImagePagerFragment;
import com.mattyws.udacity.cookner.ui.fragments.IngredientListFragment;
import com.mattyws.udacity.cookner.ui.fragments.StepListFragment;
import com.mattyws.udacity.cookner.viewmodel.RecipeViewModel;
import com.mattyws.udacity.cookner.viewmodel.RecipeViewModelFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FormRecipeActivity extends AppCompatActivity implements
        RecipeRepository.InsertDataListener,
        IngredientListFragment.IngredientListListener,
        StepListFragment.StepListListener

{

    // TODO: comment
    // TODO: add photo picker and add database logic to handle photos

    private static final int RC_PHOTO_PICKER = 1;
    public static final int ADD_INGREDIENT_ACTION = 2;
    public static final int ADD_STEP_ACTION = 3;
    public static final long DEFAULT_RECIPE_ID = -1;
    private static final String TAG = FormRecipeActivity.class.getCanonicalName();
    public static final String RECIPE_NAME = "recipe.name";
    public static final String RECIPE_ID = "recipe.id";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 222;


    private RecipeRepository mRepository;
    private RecipeViewModel mViewModel;

    private Long mRecipeId = DEFAULT_RECIPE_ID;
    private Recipe mRecipe;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;
    private List<Picture> mPictures;

    private IngredientListFragment mIngredientListFragment;
    private StepListFragment mStepListFragment;
    private ImagePagerFragment mImagePagerFragment;

    private boolean isFABOpen = false;
    private Toolbar mToolbar;
    FrameLayout mContentFrameLayout;
    ProgressBar mProgressBar;
    FloatingActionButton fabMenu, fabAddIngredient, fabAddStep, fabAddPhoto;
    LinearLayout fabIngredientLinearLayout, fabStepLinearLayout, fabPhotoLinearLayout;
    View fabBGLayout;
    // TODO: use databinding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        setUI();

        Intent intent = getIntent();
        if(!intent.hasExtra(RECIPE_ID)) {
            if (!intent.hasExtra(RECIPE_NAME)) {
                finish();
            }

            String name = intent.getStringExtra(RECIPE_NAME);
            mRepository = new RecipeRepository(getApplication(), this);

            Recipe recipe = new Recipe();
            recipe.setName(name);
            mRepository.insert(recipe);
        } else {
            mRecipeId = intent.getLongExtra(RECIPE_ID, DEFAULT_RECIPE_ID);
            performOperation(mRecipeId);
        }
    }

    private void setUI() {
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mToolbar.setNavigationIcon(R.drawable.ic_add_black_24dp);
        setSupportActionBar(mToolbar);

        mIngredients = new ArrayList<>();
        mSteps = new ArrayList<>();

        mIngredientListFragment = new IngredientListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ingredient_list_holder, mIngredientListFragment)
                .commit();

        mStepListFragment = new StepListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.step_list_holder, mStepListFragment)
                .commit();

        mImagePagerFragment = new ImagePagerFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.pictures_pager_holder, mImagePagerFragment)
                .commit();

        mContentFrameLayout = (FrameLayout) findViewById(R.id.content_holder);
        mProgressBar = (ProgressBar) findViewById(R.id.content_progress_bar);
        fabIngredientLinearLayout = (LinearLayout) findViewById(R.id.ingredient_fab_linear_layout);
        fabStepLinearLayout = (LinearLayout) findViewById(R.id.step_fab_linear_layout);
        fabPhotoLinearLayout = (LinearLayout) findViewById(R.id.photo_fab_linear_layout);

        fabMenu = (FloatingActionButton) findViewById(R.id.add_fab_menu);
        fabAddIngredient = (FloatingActionButton) findViewById(R.id.add_ingredient_fab);
        fabAddStep = (FloatingActionButton) findViewById(R.id.add_step_fab);
        fabAddPhoto = (FloatingActionButton) findViewById(R.id.add_photo_fab);
        fabBGLayout = findViewById(R.id.fabBGLayout);

        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });
        fabAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFABMenu();
                Intent addIngredientIntent = new Intent(v.getContext(), FormActivity.class);
                addIngredientIntent.putExtra(FormActivity.FORMULARY, FormActivity.INGREDIENT_FORM);
                addIngredientIntent.putExtra(FormActivity.RECIPE_ID, mRecipeId);
                startActivity(addIngredientIntent);
            }
        });
        fabAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFABMenu();
                Intent addStepIntent = new Intent(v.getContext(), FormActivity.class);
                addStepIntent.putExtra(FormActivity.FORMULARY, FormActivity.STEP_FORM);
                addStepIntent.putExtra(FormActivity.RECIPE_ID, mRecipeId);
                startActivity(addStepIntent);
            }
        });
        fabAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFABMenu();
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                }
            }
        });
        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabBGLayout.getVisibility() == View.VISIBLE)
                    closeFABMenu();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void populateUI(Recipe recipe) {
        mToolbar.setTitle("Add " + recipe.getName());
        mRecipe = recipe;
//        mIngredientListFragment.fetchAndPopulateRecipeIngredient(recipe.getId());
//        mStepListFragment.fetchAndPopulateRecipeSteps(recipe.getId());
//        mImagePagerFragment.fetchAndPopulateRecipePictures(recipe.getId());
//        swapVisibility();
        mViewModel.getRecipeIngredientsLiveData().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                setRecipeIngredients(ingredients);
            }
        });
        mViewModel.getRecipeStepsLiveData().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                setRecipeSteps(steps);
            }
        });
        mViewModel.getRecipePictures().observe(this, new Observer<List<Picture>>() {
            @Override
            public void onChanged(@Nullable List<Picture> pictures) {
                setRecipePictures(pictures);
                swapVisibility();
            }
        });
    }

    private void showFABMenu() {
        isFABOpen = true;
        fabIngredientLinearLayout.setVisibility(View.VISIBLE);
        fabStepLinearLayout.setVisibility(View.VISIBLE);
        fabPhotoLinearLayout.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);
        fabMenu.animate().rotationBy(135);
        fabIngredientLinearLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabStepLinearLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabPhotoLinearLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        fabBGLayout.setOnClickListener(null);
        fabMenu.animate().rotationBy(-135);
        fabStepLinearLayout.animate().translationY(0);
        fabPhotoLinearLayout.animate().translationY(0);
        fabIngredientLinearLayout.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fabIngredientLinearLayout.setVisibility(View.GONE);
                    fabStepLinearLayout.setVisibility(View.GONE);
                    fabPhotoLinearLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isFABOpen) {
            closeFABMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_recipe_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.menu_action_cooking_mode == id) {
            Intent cookingModeIntent = new Intent(this, CookingViewActivity.class);
            cookingModeIntent.putExtra(CookingViewActivity.RECIPE_ID, mRecipe.getId());
            startActivity(cookingModeIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void swapVisibility() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
            mContentFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            mContentFrameLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void performOperation(Long newItemId) {
        Log.d(TAG, "performOperation: recipe was inserted on database");
        RecipeViewModelFactory factory = new RecipeViewModelFactory(getApplication(), newItemId);
        mViewModel = ViewModelProviders.of(this, factory)
                .get(RecipeViewModel.class);
        mViewModel.getRecipeLiveData().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                mViewModel.getRecipeLiveData().removeObserver(this);
                populateUI(recipe);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            String filename = mRecipe.getName().replace(" ", "_") + "_" + UUID.randomUUID().toString() + ".jpg";
            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                File file = new File(getFilesDir(), filename);
                FileOutputStream fOutStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOutStream);
                fOutStream.flush();
                fOutStream.close();
                MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
                Picture picture = new Picture();
                picture.setFilename(filename);
                picture.setAbsoluteFilepath(file.getAbsolutePath());
                picture.setRecipeId(mRecipe.getId());
                mViewModel.insertNewPicture(picture);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (Exception e) {
                Log.d("saveImage", "Exception 2, Something went wrong!");
                e.printStackTrace();
            }

        }
    }

    private void setRecipeIngredients(List<Ingredient> ingredients) {
        mIngredientListFragment.setIngredients(ingredients);
    }

    public void setRecipeSteps(List<Step> recipeSteps) {
        mStepListFragment.setSteps(recipeSteps);
    }

    public void setRecipePictures(List<Picture> pictures) {
        mImagePagerFragment.setPictures(pictures);
    }

    @Override
    public void onIngredientSwipedDismiss(Ingredient removedIngredient) {
        mViewModel.delete(removedIngredient);
    }

    @Override
    public void onStepSwipedDismiss(Step deletedStep) {
        mViewModel.delete(deletedStep);
    }
}
