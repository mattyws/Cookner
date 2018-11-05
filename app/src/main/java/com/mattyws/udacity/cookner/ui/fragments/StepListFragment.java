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
import com.mattyws.udacity.cookner.database.entities.Step;
import com.mattyws.udacity.cookner.databinding.FragmentStepListBinding;
import com.mattyws.udacity.cookner.ui.FormActivity;
import com.mattyws.udacity.cookner.ui.RecyclerViewClickListener;
import com.mattyws.udacity.cookner.ui.adapters.IngredientListAdapter;
import com.mattyws.udacity.cookner.ui.adapters.RecipeListAdapter;
import com.mattyws.udacity.cookner.ui.adapters.StepListAdapter;
import com.mattyws.udacity.cookner.ui.helpers.StepListItemTouchHelper;
import com.mattyws.udacity.cookner.viewmodel.RecipeViewModel;
import com.mattyws.udacity.cookner.viewmodel.StepViewModel;
import com.mattyws.udacity.cookner.viewmodel.StepViewModelFactory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepListFragment extends Fragment implements RecyclerViewClickListener,
        StepListItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String TAG = StepListFragment.class.getCanonicalName();
    public static final String READ_ONLY = "read_only";
    private static final String RECIPE_ID = "recipe.id";

    private boolean readOnly = false;

    private StepListAdapter mAdapter;
    private FragmentStepListBinding mDataBinding;
    private StepListListener mListener;


    public StepListFragment() {}

    public static StepListFragment newInstance(){
        StepListFragment fragment = new StepListFragment();
        return fragment;
    }

    public static StepListFragment newInstance(boolean readOnly){
        StepListFragment fragment = new StepListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(READ_ONLY, readOnly);
        fragment.setArguments(bundle);
        return fragment;
    }

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

        if(!readOnly) {
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new StepListItemTouchHelper(
                    0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mDataBinding.stepListRv);
        }

        ViewModelProviders.of(getActivity()).get(RecipeViewModel.class).getRecipeStepsLiveData()
                .observe(this, new Observer<List<Step>>() {
                    @Override
                    public void onChanged(@Nullable List<Step> steps) {
                        mAdapter.swapLists(steps);
                    }
                });

        return mDataBinding.getRoot();
    }

    public void setSteps(List<Step> mSteps) {
        mAdapter.swapLists(mSteps);
    }

    @Override
    public void onClick(RecyclerView.ViewHolder view, long id) {
        if(!readOnly) {
            Intent editStepIntent = new Intent(getContext(), FormActivity.class);
            editStepIntent.putExtra(FormActivity.FORMULARY, FormActivity.STEP_FORM);
            editStepIntent.putExtra(FormActivity.ITEM_ID, id);
            startActivity(editStepIntent);
        }
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
                        ViewModelProviders.of(getActivity()).get(RecipeViewModel.class)
                                .delete(removedStep);
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
        if(!readOnly) {
            if (context instanceof StepListListener) {
                mListener = (StepListListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement IngredientListListener");
            }
        }
    }

    public interface StepListListener{
        void onStepSwipedDismiss(Step deletedStep);
    }
}
