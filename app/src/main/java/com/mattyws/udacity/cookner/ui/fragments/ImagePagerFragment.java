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
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Step;
import com.mattyws.udacity.cookner.databinding.FragmentImagePagerBinding;
import com.mattyws.udacity.cookner.ui.adapters.ImagesPagerAdapter;
import com.mattyws.udacity.cookner.viewmodel.PictureViewModel;
import com.mattyws.udacity.cookner.viewmodel.PictureViewModelFactory;
import com.mattyws.udacity.cookner.viewmodel.StepViewModel;
import com.mattyws.udacity.cookner.viewmodel.StepViewModelFactory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePagerFragment extends Fragment {

    private FragmentImagePagerBinding mDataBinding;
    private ImagesPagerAdapter mAdapter;
    private PictureViewModel mViewModel;

    public ImagePagerFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_pager,
                container, false);
        mAdapter = new ImagesPagerAdapter(getContext(), null);
        mDataBinding.imageViewPager.setAdapter(mAdapter);
        return mDataBinding.getRoot();
    }

    public void fetchAndPopulateRecipePictures(long recipeId){
        if(mViewModel == null) {
            PictureViewModelFactory factory = new PictureViewModelFactory(getActivity().getApplication(), recipeId);
            mViewModel = ViewModelProviders.of(this, factory)
                    .get(PictureViewModel.class);
        }
        mViewModel.getRecipePictures().observe(this, new Observer<List<Picture>>() {
            @Override
            public void onChanged(@Nullable List<Picture> pictures) {
                setPictures(pictures);
            }
        });
    }

    public void setPictures(List<Picture> pictures){
        mAdapter.setPictures(pictures);
    }

}
