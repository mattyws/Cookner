package com.mattyws.udacity.cookner.ui.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Step;
import com.mattyws.udacity.cookner.databinding.FragmentStepsPagerBinding;
import com.mattyws.udacity.cookner.ui.adapters.StepsPagerAdapter;
import com.mattyws.udacity.cookner.viewmodel.RecipeViewModel;
import com.mattyws.udacity.cookner.viewmodel.StepViewModel;
import com.mattyws.udacity.cookner.viewmodel.StepViewModelFactory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsPagerFragment extends Fragment {

    private static final String RECIPE_ID = "recipe.id";
    private FragmentStepsPagerBinding mDataBinding;
    private StepsPagerAdapter mAdapter;
    private StepViewModel mViewModel;

    public StepsPagerFragment() {
        // Required empty public constructor
    }

    public static StepsPagerFragment newInstance(long recipeId){
        StepsPagerFragment fragment = new StepsPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(RECIPE_ID, recipeId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps_pager,
                container, false);
        mAdapter = new StepsPagerAdapter(getContext(), null);
        mDataBinding.stepsPager.setAdapter(mAdapter);
        mDataBinding.indicator.setViewPager(mDataBinding.stepsPager);
        mAdapter.registerDataSetObserver(mDataBinding.indicator.getDataSetObserver());
        ViewModelProviders.of(getActivity()).get(RecipeViewModel.class).getRecipeStepsLiveData()
                .observe(this, new Observer<List<Step>>() {
                    @Override
                    public void onChanged(@Nullable List<Step> steps) {
                        mAdapter.setSteps(steps);
                    }
                });
        return mDataBinding.getRoot();
    }

    public void fetchAndPopulateRecipePictures(long recipeId){
        if(mViewModel == null) {
            StepViewModelFactory factory = new StepViewModelFactory(getActivity(), recipeId);
            mViewModel = ViewModelProviders.of(this, factory)
                    .get(StepViewModel.class);
        }
        mViewModel.getRecipeSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                setSteps(steps);
            }
        });
    }

    public void setSteps(List<Step> pictures){
        mAdapter.setSteps(pictures);
    }

}
