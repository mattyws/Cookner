package com.mattyws.udacity.cookner.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.ui.fragments.RecipeListFragment;
import com.mattyws.udacity.cookner.viewmodel.RecipesViewModel;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final int ADD_NEW_RECIPE = 1;

    private RecipeListFragment mRecipeListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecipeListFragment = new RecipeListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipe_list_holder, mRecipeListFragment)
                .commit();

    }

    public void fab_onClick(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Insert Name");
        alert.setMessage("Recipe name");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alert.setView(input);
        alert.setIcon(R.drawable.ic_add_black_24dp);
        alert.setPositiveButton("Ok",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Here actually start the GoLauncher
                    Intent addRecipeIntent = new Intent(MainActivity.this, FormRecipeActivity.class);
                    addRecipeIntent.putExtra(FormRecipeActivity.RECIPE_NAME, input.getText().toString());
                    startActivity(addRecipeIntent);
                }
        });
        alert.setNegativeButton("No",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
            }
        });
        alert.show();
    }

}
