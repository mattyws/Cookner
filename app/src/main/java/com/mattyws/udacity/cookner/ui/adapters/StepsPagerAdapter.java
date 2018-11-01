package com.mattyws.udacity.cookner.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Step;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StepsPagerAdapter extends PagerAdapter {


    private static final String TAG = StepsPagerAdapter.class.getCanonicalName();
    private List<Step> mSteps;
    private LayoutInflater inflater;
    private Context mContext;


    public StepsPagerAdapter(Context mContext, List<Step> steps) {
        this.mContext = mContext;
        this.mSteps =steps;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if(mSteps == null) return 0;
        return mSteps.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View stepLayout = inflater.inflate(R.layout.step_cell, view, false);
        TextView stepDescription = (TextView) stepLayout.findViewById(R.id.step_description);
        stepDescription.setText(mSteps.get(position).getDescription());
        view.addView(stepLayout, 0);

        return stepLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void setSteps(List<Step> steps) {
        if (steps == null){
            steps = new ArrayList<>();
        }
        if(steps.isEmpty()){
            Step step = new Step();
            step.setDescription("No step found!");
            steps.add(step);
        }
        this.mSteps = steps;
        notifyDataSetChanged();
    }
}
