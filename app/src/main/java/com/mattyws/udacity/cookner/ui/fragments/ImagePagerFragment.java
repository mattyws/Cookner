package com.mattyws.udacity.cookner.ui.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Step;
import com.mattyws.udacity.cookner.databinding.FragmentImagePagerBinding;
import com.mattyws.udacity.cookner.ui.adapters.ImagesPagerAdapter;
import com.mattyws.udacity.cookner.viewmodel.PictureViewModel;
import com.mattyws.udacity.cookner.viewmodel.PictureViewModelFactory;
import com.mattyws.udacity.cookner.viewmodel.RecipeViewModel;
import com.mattyws.udacity.cookner.viewmodel.StepViewModel;
import com.mattyws.udacity.cookner.viewmodel.StepViewModelFactory;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePagerFragment extends Fragment {

    private static final String TAG = ImagePagerFragment.class.getCanonicalName();
    private static final String RECIPE_ID = "recipe.id";
    private FragmentImagePagerBinding mDataBinding;
    private ImagesPagerAdapter mAdapter;
    private long mRecipeId;
//    private PictureViewModel mViewModel;

    public ImagePagerFragment() {}

    public static ImagePagerFragment newInstance(){
        ImagePagerFragment fragment = new ImagePagerFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_pager,
                container, false);
        mAdapter = new ImagesPagerAdapter(getContext(), null);
        mDataBinding.imageViewPager.setAdapter(mAdapter);
        mDataBinding.indicator.setViewPager(mDataBinding.imageViewPager);
        mAdapter.registerDataSetObserver(mDataBinding.indicator.getDataSetObserver());

        ViewModelProviders.of(getActivity()).get(RecipeViewModel.class).getRecipePictures()
                .observe(this, new Observer<List<Picture>>() {
                    @Override
                    public void onChanged(@Nullable List<Picture> pictures) {
                        mAdapter.setPictures(pictures);
                    }
                });

        return mDataBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setPictures(List<Picture> pictures){
        mAdapter.setPictures(pictures);
    }

    public void notifyDatasetChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
