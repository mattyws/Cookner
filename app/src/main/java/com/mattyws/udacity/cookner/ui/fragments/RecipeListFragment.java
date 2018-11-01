package com.mattyws.udacity.cookner.ui.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.databinding.FragmentRecipeListBinding;
import com.mattyws.udacity.cookner.ui.FormRecipeActivity;
import com.mattyws.udacity.cookner.ui.RecyclerViewClickListener;
import com.mattyws.udacity.cookner.ui.adapters.RecipeListAdapter;
import com.mattyws.udacity.cookner.ui.helpers.RecipeListItemTouchHelper;
import com.mattyws.udacity.cookner.viewmodel.RecipesViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment implements
        RecipeListItemTouchHelper.RecyclerItemTouchHelperListener,
        RecyclerViewClickListener{


    private static final String TAG = RecipeListFragment.class.getCanonicalName();
    private RecipeListAdapter mAdapter;
    private FragmentRecipeListBinding mDataBinding;

    private RecipesViewModel mViewModel;


    public RecipeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list,
                container, false);
        mAdapter = new RecipeListAdapter(mDataBinding.getRoot().getContext(), null, this);

        mDataBinding.recipeListRv.setLayoutManager(new LinearLayoutManager(mDataBinding.getRoot().getContext()));
        mDataBinding.recipeListRv.setAdapter(mAdapter);
        mDataBinding.recipeListRv.setItemAnimator(new DefaultItemAnimator());
        mDataBinding.recipeListRv.addItemDecoration(new DividerItemDecoration(mDataBinding.getRoot().getContext(), DividerItemDecoration.VERTICAL));

        mViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);

        mViewModel.getAllUserRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                populateUI(recipes);
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecipeListItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mDataBinding.recipeListRv);

        return mDataBinding.getRoot();
    }

    private void populateUI(List<Recipe> recipes) {
        mAdapter.swapLists(recipes);
    }

    @Override
    public void onRecipeSwiped(final RecyclerView.ViewHolder viewHolder, int direction, int position) {

        Log.d(TAG, "onRecipeSwiped: ");
        if (viewHolder instanceof RecipeListAdapter.RecipeViewHolder) {
            final int deletedIndex = viewHolder.getAdapterPosition();
            final Recipe removedRecipe = mAdapter.get(deletedIndex);
            mAdapter.delete(deletedIndex);
            Snackbar snackbar = Snackbar
                    .make(mDataBinding.recipeListRv, removedRecipe.getName() + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(removedRecipe, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.addCallback(new Snackbar.Callback(){
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
//                    super.onDismissed(transientBottomBar, event);
                    if(event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        Log.d(TAG, "onDismissed: ");
                        mViewModel.delete(removedRecipe);
                    }
                }
            });
            snackbar.show();
        }
    }

    @Override
    public void onClick(RecyclerView.ViewHolder view, long id) {
        Log.d(TAG, "onClick: " + id);
        Intent intent = new Intent(getActivity(), FormRecipeActivity.class);
        intent.putExtra(FormRecipeActivity.RECIPE_ID, id);
        getActivity().startActivity(intent);
    }
}
