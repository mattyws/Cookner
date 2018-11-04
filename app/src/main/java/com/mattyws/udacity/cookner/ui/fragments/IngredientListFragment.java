package com.mattyws.udacity.cookner.ui.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import com.mattyws.udacity.cookner.databinding.FragmentIngredientListBinding;
import com.mattyws.udacity.cookner.ui.FormActivity;
import com.mattyws.udacity.cookner.ui.RecyclerViewClickListener;
import com.mattyws.udacity.cookner.ui.adapters.IngredientListAdapter;
import com.mattyws.udacity.cookner.ui.helpers.IngredientListItemTouchHelper;
import com.mattyws.udacity.cookner.viewmodel.IngredientViewModel;
import com.mattyws.udacity.cookner.viewmodel.IngredientViewModelFactory;
import com.mattyws.udacity.cookner.viewmodel.RecipeViewModel;

import java.util.List;

public class IngredientListFragment extends Fragment implements
        IngredientListItemTouchHelper.RecyclerItemTouchHelperListener,
        RecyclerViewClickListener {

    public static final String READ_ONLY = "read_only";
    public static final String RECIPE_ID = "recipe.id";

    private static final String TAG = IngredientListFragment.class.getCanonicalName();
    private IngredientListAdapter mAdapter;
    private FragmentIngredientListBinding mDataBinding;
    private boolean readOnly = false;


    private IngredientViewModel mViewModel;
    private IngredientListListener mListener;
    private long mRecipeId;


    public IngredientListFragment() {
    }

    public static IngredientListFragment newInstance(long recipeId){
        IngredientListFragment fragment = new IngredientListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(RECIPE_ID, recipeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static IngredientListFragment newInstance(long recipeId, boolean readOnly){
        IngredientListFragment fragment = new IngredientListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(RECIPE_ID, recipeId);
        bundle.putBoolean(READ_ONLY, readOnly);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient_list,
                container, false);
        mAdapter = new IngredientListAdapter(mDataBinding.getRoot().getContext(), null, this, readOnly);
        mDataBinding.ingredientListRv.setLayoutManager(new LinearLayoutManager(mDataBinding.getRoot().getContext()));
        mDataBinding.ingredientListRv.setAdapter(mAdapter);
        mDataBinding.ingredientListRv.setItemAnimator(new DefaultItemAnimator());
        mDataBinding.ingredientListRv.addItemDecoration(new DividerItemDecoration(mDataBinding.getRoot().getContext(), DividerItemDecoration.VERTICAL));

        if(!readOnly) {
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new IngredientListItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mDataBinding.ingredientListRv);
        }

        ViewModelProviders.of(getActivity()).get(RecipeViewModel.class).getRecipeIngredientsLiveData()
                .observe(this, new Observer<List<Ingredient>>() {
                    @Override
                    public void onChanged(@Nullable List<Ingredient> ingredients) {
                        mAdapter.swapLists(ingredients);
                    }
                });

        return mDataBinding.getRoot();
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
                        mListener.onIngredientSwipedDismiss(removedIngredient);
//                        mViewModel.delete(removedIngredient);
                    }
                }
            });
            snackbar.show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(READ_ONLY)){
            readOnly = bundle.getBoolean(READ_ONLY);
        }
        if(bundle != null && bundle.containsKey(RECIPE_ID)){
            mRecipeId = bundle.getLong(RECIPE_ID);
        } else if (bundle != null){
            throw new RuntimeException(context.toString()
                    + " must have RECIPE_ID argument.");
        }
        if(!readOnly) {
            if (context instanceof IngredientListListener) {
                mListener = (IngredientListListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement IngredientListListener");
            }
        }
    }


    @Override
    public void onClick(RecyclerView.ViewHolder view, long id) {
        Log.d(TAG, "onClick: ");
        if(!readOnly) {
            Intent editIngredientIntent = new Intent(getContext(), FormActivity.class);
            editIngredientIntent.putExtra(FormActivity.FORMULARY, FormActivity.INGREDIENT_FORM);
            editIngredientIntent.putExtra(FormActivity.ITEM_ID, id);
            startActivity(editIngredientIntent);
        }
    }

    public interface IngredientListListener{
        void onIngredientSwipedDismiss(Ingredient removedIngredient);
    }
}
