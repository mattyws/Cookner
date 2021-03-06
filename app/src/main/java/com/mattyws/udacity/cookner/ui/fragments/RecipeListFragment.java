package com.mattyws.udacity.cookner.ui.fragments;


import android.app.Activity;
import android.app.ActivityOptions;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.CooknerRepository;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.databinding.FragmentRecipeListBinding;
import com.mattyws.udacity.cookner.ui.FormRecipeActivity;
import com.mattyws.udacity.cookner.ui.RecyclerViewClickListener;
import com.mattyws.udacity.cookner.ui.adapters.RecipeListAdapter;
import com.mattyws.udacity.cookner.viewmodel.RecipeViewModel;
import com.mattyws.udacity.cookner.viewmodel.RecipesViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment implements
        RecyclerViewClickListener,
        RecipeListAdapter.RecipeListAdapterListener
{
    private static final String TAG = RecipeListFragment.class.getCanonicalName();
    private static final String USER_ID = "user.id";
    private RecipeListAdapter mAdapter;
    private FragmentRecipeListBinding mDataBinding;

    private RecipeListFragmentListener mListener;


    public RecipeListFragment() {}

    public static RecipeListFragment newInstance() {
        RecipeListFragment fragment = new RecipeListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list,
                container, false);
        mAdapter = new RecipeListAdapter(mDataBinding.getRoot().getContext(), null, this, this);

        mDataBinding.recipeListRv.setLayoutManager(new GridLayoutManager(mDataBinding.getRoot().getContext(), 2));
        mDataBinding.recipeListRv.setAdapter(mAdapter);
        mDataBinding.recipeListRv.setItemAnimator(new DefaultItemAnimator());
        ViewModelProviders.of(getActivity()).get(RecipesViewModel.class).getAllUserRecipes()
                .observe(this, new Observer<List<Recipe>>() {
                    @Override
                    public void onChanged(@Nullable List<Recipe> recipes) {
                        swapLists(recipes);
                    }
                });
        return mDataBinding.getRoot();
    }

    private void swapLists(List<Recipe> recipes) {
        mAdapter.swapLists(recipes);
        for (int i = 0; i < recipes.size(); i++){
            final int finalI = i;
            ViewModelProviders.of(getActivity()).get(RecipesViewModel.class)
                    .getRecipePictures(recipes.get(i).getId())
                    .observe(this, new Observer<List<Picture>>() {
                        @Override
                        public void onChanged(@Nullable List<Picture> pictures) {
                            mAdapter.setViewPictures(finalI, pictures);
                        }
                    });
        }
    }

    public void setRecipes(List<Recipe> recipes){
        mAdapter.swapLists(recipes);
    }

    public void setRecipePictures(int pos ,List<Picture> recipePictures){
        mAdapter.setViewPictures(pos, recipePictures);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeListFragmentListener) {
            mListener = (RecipeListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onClick(RecyclerView.ViewHolder view, long id) {
        Log.d(TAG, "onClick: " + id);
        Intent intent = new Intent(getActivity(), FormRecipeActivity.class);
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(
                        getActivity(),
                        ((RecipeListAdapter.RecipeViewHolder)view).mViewPager,
                        getActivity().getResources().getString(R.string.image_pager_shared_name)
                );
        intent.putExtra(FormRecipeActivity.RECIPE_ID, id);
        getActivity().startActivity(intent, options.toBundle());
    }

    @Override
    public void onRecipeDelete(final int pos) {
        Log.d(TAG, "onRecipeSwiped: ");
        final Recipe removedRecipe = mAdapter.get(pos);
        mAdapter.delete(pos);
        Snackbar snackbar = Snackbar
                .make(mDataBinding.recipeListRv, removedRecipe.getName() + " removed from cart!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.restoreItem(removedRecipe, pos);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
//                    super.onDismissed(transientBottomBar, event);
                if(event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    ViewModelProviders.of(getActivity()).get(RecipesViewModel.class)
                                .delete(removedRecipe);
                }
            }
        });
        snackbar.show();
    }

    public interface RecipeListFragmentListener{
        void onRecipeDelete(Recipe deletedRecipe);
    }
}
