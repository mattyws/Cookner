package com.mattyws.udacity.cookner.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.databinding.FragmentIngredientFormBinding;

public class IngredientFormFragment extends Fragment {

    private IngredientFormListener mListener;
    public FragmentIngredientFormBinding mDataBinding;
    private Ingredient mIngredient;

    public IngredientFormFragment() {
    }

    public void setIngredient(Ingredient ingredient){
        this.mIngredient = ingredient;
    }
    public Ingredient getIngredient(){return mIngredient;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient_form,
                container, false);
        if(mIngredient != null){
            mDataBinding.ingredientNameEt.setText(mIngredient.getName());
            mDataBinding.ingredientMeasureEt.setText(mIngredient.getMeasure());
            mDataBinding.ingredientQuantityEt.setText(mIngredient.getQuantity());
        }
        return mDataBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IngredientFormListener) {
            mListener = (IngredientFormListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface IngredientFormListener {
        void onIngredientFormInteraction();
    }
}
