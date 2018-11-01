package com.mattyws.udacity.cookner.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.ui.RecyclerViewClickListener;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>{

    public static final String TAG = StepListAdapter.class.getCanonicalName();

    private Context mContext;
    private List<Recipe> mRecipes;
    private RecyclerViewClickListener mListener;

    public RecipeListAdapter(Context mContext, List<Recipe> recipes, RecyclerViewClickListener listener) {
        this.mContext = mContext;
        this.mRecipes = recipes;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecipeListAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_cell, viewGroup, false);
        RecipeListAdapter.RecipeViewHolder holder = new RecipeListAdapter.RecipeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeListAdapter.RecipeViewHolder recipeViewHolder, final int i) {
        recipeViewHolder.mRecipeName.setText(mRecipes.get(i).getName());
        recipeViewHolder.id = mRecipes.get(i).getId();
        recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(recipeViewHolder, recipeViewHolder.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mRecipes != null) return mRecipes.size();
        return 0;
    }

    public void swapLists(List<Recipe> newRecipes){
        mRecipes = newRecipes;
        this.notifyDataSetChanged();
    }

    public void delete(int position){
        Recipe recipe = mRecipes.get(position);
        mRecipes.remove(position);
        notifyDataSetChanged();
    }

    public Recipe get(int position){
        return mRecipes.get(position);
    }

    public void restoreItem(Recipe removedRecipe, int adapterPosition) {
        mRecipes.add(adapterPosition, removedRecipe);
        notifyItemInserted(adapterPosition);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{

        public long id;
        public TextView mRecipeName;
        public ConstraintLayout mForegroundView, mBackgroundView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecipeName = (TextView) itemView.findViewById(R.id.recipe_name);
            mForegroundView = (ConstraintLayout) itemView.findViewById(R.id.foreground_view);
            mBackgroundView = (ConstraintLayout) itemView.findViewById(R.id.background_view);
        }
    }
}
