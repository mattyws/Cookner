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
import com.mattyws.udacity.cookner.ui.RecyclerViewClickListener;

import java.util.List;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder>{

    public static final String TAG = IngredientListAdapter.class.getCanonicalName();

    private Context mContext;
    private List<Ingredient> mIngredients;
    private RecyclerViewClickListener mListener;

    public IngredientListAdapter(Context mContext, List<Ingredient> ingredients,
                                 RecyclerViewClickListener listener) {
        this.mContext = mContext;
        this.mIngredients = ingredients;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ingredient_cell, viewGroup, false);
        IngredientViewHolder holder = new IngredientViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientViewHolder ingredientViewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: binding holder");
        ingredientViewHolder.id = mIngredients.get(i).id;
        ingredientViewHolder.mIngredientName.setText(mIngredients.get(i).name);
        ingredientViewHolder.mIngredientQuantity.setText(mIngredients.get(i).quantity);
        ingredientViewHolder.mIngredientMeasure.setText(mIngredients.get(i).measure);
        ingredientViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(ingredientViewHolder, ingredientViewHolder.id);
            }
        });
    }

    public void swapLists(List<Ingredient> newIngredients){
        Log.d(TAG, "swapLists: swaping lists");
        mIngredients = newIngredients;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mIngredients != null) return mIngredients.size();
        return 0;
    }

    public void delete(int position){
        mIngredients.remove(position);
        notifyItemRemoved(position);
    }

    public Ingredient get(int position){
        return mIngredients.get(position);
    }
    public void restoreItem(Ingredient removedIngredient, int adapterPosition) {
        mIngredients.add(adapterPosition, removedIngredient);
        notifyItemInserted(adapterPosition);
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder{

        long id;
        TextView mIngredientName, mIngredientQuantity, mIngredientMeasure;
        public ConstraintLayout mForegroundView, mBackgroundView;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            mIngredientName = itemView.findViewById(R.id.ingredient_name);
            mIngredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
            mIngredientMeasure = itemView.findViewById(R.id.ingredient_measure);
            mForegroundView = itemView.findViewById(R.id.foreground_view);
            mBackgroundView = itemView.findViewById(R.id.background_view);
        }
    }

}
