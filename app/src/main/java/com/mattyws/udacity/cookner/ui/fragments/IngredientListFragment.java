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
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.databinding.FragmentIngredientListBinding;
import com.mattyws.udacity.cookner.ui.FormActivity;
import com.mattyws.udacity.cookner.ui.RecyclerViewClickListener;
import com.mattyws.udacity.cookner.ui.adapters.IngredientListAdapter;
import com.mattyws.udacity.cookner.ui.adapters.RecipeListAdapter;
import com.mattyws.udacity.cookner.ui.helpers.IngredientListItemTouchHelper;
import com.mattyws.udacity.cookner.viewmodel.AddIngredientViewModel;
import com.mattyws.udacity.cookner.viewmodel.AddIngredientViewModelFactory;
import com.mattyws.udacity.cookner.viewmodel.IngredientViewModel;
import com.mattyws.udacity.cookner.viewmodel.IngredientViewModelFactory;

import java.util.List;

public class IngredientListFragment extends Fragment implements
        IngredientListItemTouchHelper.RecyclerItemTouchHelperListener,
        RecyclerViewClickListener {

    private static final String TAG = IngredientListFragment.class.getCanonicalName();
    private IngredientListAdapter mAdapter;
    private FragmentIngredientListBinding mDataBinding;

    private IngredientViewModel mViewModel;


    public IngredientListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient_list,
                container, false);
        mAdapter = new IngredientListAdapter(mDataBinding.getRoot().getContext(), null, this);
        mDataBinding.ingredientListRv.setLayoutManager(new LinearLayoutManager(mDataBinding.getRoot().getContext()));
        mDataBinding.ingredientListRv.setAdapter(mAdapter);
        mDataBinding.ingredientListRv.setItemAnimator(new DefaultItemAnimator());
        mDataBinding.ingredientListRv.addItemDecoration(new DividerItemDecoration(mDataBinding.getRoot().getContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new IngredientListItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mDataBinding.ingredientListRv);

        return mDataBinding.getRoot();
    }

    public void fetchAndPopulateRecipeIngredient(long recipeId){
        Log.d(TAG, "fetchAndPopulateRecipeIngredient: ");
        if(mViewModel == null) {
            IngredientViewModelFactory factory = new IngredientViewModelFactory(getActivity(), recipeId);
            mViewModel = ViewModelProviders.of(this, factory)
                    .get(IngredientViewModel.class);
        }
        mViewModel.getRecipeIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                setIngredients(ingredients);
            }
        });
    }

    public void setIngredients(List<Ingredient> mIngredients) {
        mAdapter.swapLists(mIngredients);
    }

    @Override
    public void onIngredientSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        Log.d(TAG, "onRecipeSwiped: ");
        if (viewHolder instanceof IngredientListAdapter.IngredientViewHolder) {
            final int deletedIndex = viewHolder.getAdapterPosition();
            final Ingredient removedIngredient = mAdapter.get(deletedIndex);
            mAdapter.delete(deletedIndex);
            Snackbar snackbar = Snackbar
                    .make(mDataBinding.ingredientListRv, removedIngredient.getName() + " removed!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(removedIngredient, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.addCallback(new Snackbar.Callback(){
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
//                    super.onDismissed(transientBottomBar, event);
                    if(event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        Log.d(TAG, "onDismissed: ");
                        mViewModel.delete(removedIngredient);
                    }
                }
            });
            snackbar.show();
        }
    }



    @Override
    public void onClick(RecyclerView.ViewHolder view, long id) {
        Intent editIngredientIntent = new Intent(getContext(), FormActivity.class);
        editIngredientIntent.putExtra(FormActivity.FORMULARY, FormActivity.INGREDIENT_FORM);
        editIngredientIntent.putExtra(FormActivity.ITEM_ID, id);
        startActivity(editIngredientIntent);
    }
}
