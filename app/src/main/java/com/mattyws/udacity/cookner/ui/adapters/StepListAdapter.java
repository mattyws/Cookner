package com.mattyws.udacity.cookner.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Ingredient;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.database.entities.Step;
import com.mattyws.udacity.cookner.ui.RecyclerViewClickListener;

import java.util.List;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepViewHolder>{

    public static final String TAG = StepListAdapter.class.getCanonicalName();
    private Context mContext;
    private List<Step> mSteps;
    private RecyclerViewClickListener mListener;

    public StepListAdapter(Context mContext, List<Step> mSteps, RecyclerViewClickListener listener) {
        this.mContext = mContext;
        this.mSteps = mSteps;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.step_cell, viewGroup, false);
        StepListAdapter.StepViewHolder holder = new StepListAdapter.StepViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final StepViewHolder stepViewHolder, int i) {
        Log.d(TAG, "onBindViewHolder binding view " + mSteps.get(i).getDescription());
        stepViewHolder.mStepDescription.setText(mSteps.get(i).getDescription());
        stepViewHolder.id = mSteps.get(i).getId();
        stepViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(stepViewHolder, stepViewHolder.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mSteps != null) return mSteps.size();
        return 0;
    }

    public void swapLists(List<Step> newSteps){
        mSteps = newSteps;
        this.notifyDataSetChanged();
    }

    public void delete(int position){
        mSteps.remove(position);
        notifyItemRemoved(position);
    }

    public Step get(int position){
        return mSteps.get(position);
    }

    public void restoreItem(Step removedStep, int adapterPosition) {
        mSteps.add(adapterPosition, removedStep);
        notifyItemInserted(adapterPosition);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder{

        long id;
        TextView mStepDescription;
        public ConstraintLayout mForegroundView, mBackgroundView;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            mStepDescription = (TextView) itemView.findViewById(R.id.step_description);
            mForegroundView = itemView.findViewById(R.id.foreground_view);
            mBackgroundView = itemView.findViewById(R.id.background_view);
        }
    }
}
