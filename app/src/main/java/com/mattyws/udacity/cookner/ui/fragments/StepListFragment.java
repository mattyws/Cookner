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
import com.mattyws.udacity.cookner.database.entities.Step;
import com.mattyws.udacity.cookner.databinding.FragmentStepListBinding;
import com.mattyws.udacity.cookner.ui.FormActivity;
import com.mattyws.udacity.cookner.ui.RecyclerViewClickListener;
import com.mattyws.udacity.cookner.ui.adapters.IngredientListAdapter;
import com.mattyws.udacity.cookner.ui.adapters.StepListAdapter;
import com.mattyws.udacity.cookner.ui.helpers.StepListItemTouchHelper;
import com.mattyws.udacity.cookner.viewmodel.StepViewModel;
import com.mattyws.udacity.cookner.viewmodel.StepViewModelFactory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepListFragment extends Fragment implements RecyclerViewClickListener,
        StepListItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String TAG = StepListFragment.class.getCanonicalName();
    private StepListAdapter mAdapter;
    private FragmentStepListBinding mDataBinding;
    private StepViewModel mViewModel;


    public StepListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_list,
                container, false);
        mAdapter = new StepListAdapter(mDataBinding.getRoot().getContext(), null, this);
        mDataBinding.stepListRv.setLayoutManager(new LinearLayoutManager(mDataBinding.getRoot().getContext()));
        mDataBinding.stepListRv.setAdapter(mAdapter);
        mDataBinding.stepListRv.setItemAnimator(new DefaultItemAnimator());
        mDataBinding.stepListRv.addItemDecoration(
                new DividerItemDecoration(mDataBinding.getRoot().getContext(),
                DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new StepListItemTouchHelper(
                0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mDataBinding.stepListRv);
        return mDataBinding.getRoot();
    }

    public void setSteps(List<Step> mSteps) {
        mAdapter.swapLists(mSteps);
    }

    @Override
    public void onClick(RecyclerView.ViewHolder view, long id) {
        Intent editStepIntent = new Intent(getContext(), FormActivity.class);
        editStepIntent.putExtra(FormActivity.FORMULARY, FormActivity.STEP_FORM);
        editStepIntent.putExtra(FormActivity.ITEM_ID, id);
        startActivity(editStepIntent);
    }

    @Override
    public void onStepSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        Log.d(TAG, "onRecipeSwiped: ");
        if (viewHolder instanceof StepListAdapter.StepViewHolder) {
            final int deletedIndex = viewHolder.getAdapterPosition();
            final Step removedStep = mAdapter.get(deletedIndex);
            mAdapter.delete(deletedIndex);
            Snackbar snackbar = Snackbar
                    .make(mDataBinding.stepListRv, "Step removed!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapter.restoreItem(removedStep, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.addCallback(new Snackbar.Callback(){
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
//                    super.onDismissed(transientBottomBar, event);
                    if(event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        Log.d(TAG, "onDismissed: ");
                        mViewModel.delete(removedStep);
                    }
                }
            });
            snackbar.show();
        }
    }

    public void fetchAndPopulateRecipeSteps(long recipeId){
        if(mViewModel == null) {
            StepViewModelFactory factory = new StepViewModelFactory(getActivity().getApplication(), recipeId);
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
}