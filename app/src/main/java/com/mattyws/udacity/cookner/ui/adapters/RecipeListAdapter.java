package com.mattyws.udacity.cookner.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mattyws.udacity.cookner.R;
import com.mattyws.udacity.cookner.database.entities.Picture;
import com.mattyws.udacity.cookner.database.entities.Recipe;
import com.mattyws.udacity.cookner.ui.RecyclerViewClickListener;
import com.mattyws.udacity.cookner.ui.fragments.IngredientListFragment;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>{

    public static final String TAG = StepListAdapter.class.getCanonicalName();

    private Context mContext;
    private List<Recipe> mRecipes;
    private RecyclerViewClickListener mListener;
    private RecipeListAdapterListener mListListener;

    public RecipeListAdapter(Context mContext, List<Recipe> recipes, RecyclerViewClickListener listener, RecipeListAdapterListener listListener) {
        this.mContext = mContext;
        this.mRecipes = recipes;
        this.mListener = listener;
        this.mListListener = listListener;
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
        recipeViewHolder.mAdapter.setPictures(mRecipes.get(i).getPictures());
        recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(recipeViewHolder, recipeViewHolder.id);
            }
        });
        recipeViewHolder.mMenuRecipeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mContext, recipeViewHolder.mMenuRecipeItem);
                //inflating menu from xml resource
                popup.inflate(R.menu.recipe_options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete:
                                Log.d(TAG, "onMenuItemClick: ");
                                mListListener.onRecipeDelete(i);
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mRecipes != null) return mRecipes.size();
        return 0;
    }

    public void setViewPictures(int position, List<Picture> pictures){
        mRecipes.get(position).setPictures(pictures);
        notifyItemChanged(position);
    }

    public void swapLists(List<Recipe> newRecipes){
        mRecipes = newRecipes;
        this.notifyDataSetChanged();
    }

    public void delete(int position){
        mRecipes.remove(position);
        notifyItemChanged(position);
    }

    public Recipe get(int position){
        return mRecipes.get(position);
    }

    public void restoreItem(Recipe removedRecipe, int adapterPosition) {
        mRecipes.add(adapterPosition, removedRecipe);
        notifyItemInserted(adapterPosition);
    }

    public interface RecipeListAdapterListener {
        void onRecipeDelete(int pos);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{

        public long id;
        public TextView mRecipeName;
        TextView mMenuRecipeItem;
        public ViewPager mViewPager;
        ImagesPagerAdapter mAdapter;
        public ConstraintLayout mForegroundView, mBackgroundView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecipeName = (TextView) itemView.findViewById(R.id.recipe_name);
            mForegroundView = (ConstraintLayout) itemView.findViewById(R.id.foreground_view);
            mBackgroundView = (ConstraintLayout) itemView.findViewById(R.id.background_view);
            mMenuRecipeItem = (TextView) itemView.findViewById(R.id.menu_recipe_item);

            mViewPager = (ViewPager) itemView.findViewById(R.id.recipe_photo_pager);
            mAdapter = new ImagesPagerAdapter(mContext, null);
            mViewPager.setAdapter(mAdapter);
        }
    }
}
