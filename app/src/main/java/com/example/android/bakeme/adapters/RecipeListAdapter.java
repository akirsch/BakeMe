package com.example.android.bakeme.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.bakeme.Constants;
import com.example.android.bakeme.R;
import com.example.android.bakeme.activities.RecipeDetailActivity;
import com.example.android.bakeme.models.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> mRecipeList;


    public RecipeListAdapter(){}


    @NonNull
    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);

        // Inflate the custom layout
        View recipeItemView = inflater.inflate(R.layout.recipe_list_item, parent, false);

        return new ViewHolder(recipeItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.ViewHolder viewHolder,  final int position) {

        // Get the data model based on position
        final Recipe currentRecipe = mRecipeList.get(position);

        // extract data from current Recipe object
        String recipeName = currentRecipe.getmRecipeName();

        int servings = currentRecipe.getmServingSize();

        // determine which image to display based on name of current cake
        int recipeImageResourceId = getRecipeImageId( mContext, recipeName);

        viewHolder.recipeNameView.setText(recipeName);
        viewHolder.servingSizeView.setText(mContext.getString(R.string.servings_text ) + servings);

        RequestOptions requestOptions = new RequestOptions().centerCrop();

        // load correct cake image into image view
        Glide.with(mContext)
                .load(recipeImageResourceId)
                .apply(requestOptions)
                .into(viewHolder.recipeImageView);

        // set ClickListener to handle click on each recipe card by starting the Recipe Detail Activity
        // with the correct information for the selected recipe
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();

                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.SELECTED_RECIPE_KEY, currentRecipe);

                Intent selectedRecipeIntent = new Intent(context, RecipeDetailActivity.class);
                selectedRecipeIntent.putExtras(bundle);

                if (selectedRecipeIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(selectedRecipeIntent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if (mRecipeList == null )return 0;
        else return mRecipeList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public final View view;
        @BindView(R.id.recipe_image_view) ImageView recipeImageView;
        @BindView(R.id.recipe_name_tv) TextView recipeNameView;
        @BindView(R.id.serving_size_tv) TextView servingSizeView;


        public ViewHolder(View itemView){
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

    }

    public void setRecipeList (ArrayList<Recipe> recipeList){

        mRecipeList = recipeList;
        notifyDataSetChanged();
    }

    public int getRecipeImageId (Context context, String name){

        int resourceId;

        if (name.equals(context.getString(R.string.nutella_pie))) {
            resourceId = R.drawable.nutellapie;
        } else if (name.equals(context.getString(R.string.brownies))) {
            resourceId = R.drawable.brownies;
        } else if (name.equals(context.getString(R.string.yellow_cake))){
            resourceId = R.drawable.vanillacake;
        } else if (name.equals(context.getString(R.string.cheesecake))){
            resourceId = R.drawable.cheesecake;
        } else resourceId = R.drawable.ic_launcher_background;

        return resourceId;
    }
}
