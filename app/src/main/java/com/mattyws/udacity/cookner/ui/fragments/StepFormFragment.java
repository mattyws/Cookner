package com.mattyws.udacity.cookner.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Step;
import com.mattyws.udacity.cookner.databinding.FragmentStepFormBinding;

public class StepFormFragment extends Fragment {

    private static final String TAG = StepFormFragment.class.getCanonicalName();
    private StepFormListener mListener;
    public FragmentStepFormBinding mDataBinding;
    private Step mStep;

    public StepFormFragment() {
        // Required empty public constructor
    }

    public void setStep(Step step){
        mStep = step;
    }

    public Step getStep() {
        return mStep;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_form,
                container, false);
        if(mStep != null){
            mDataBinding.stepDescriptionEt.setText(mStep.getDescription());
        }
        return mDataBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepFormListener) {
            mListener = (StepFormListener) context;
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

    public interface StepFormListener {
        void onStepFormInteraction();
    }
}
