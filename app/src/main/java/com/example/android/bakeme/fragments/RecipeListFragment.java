package com.example.android.bakeme.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakeme.R;
import com.example.android.bakeme.activities.MainActivity;
import com.example.android.bakeme.adapters.RecipeListAdapter;
import com.example.android.bakeme.interfaces.RecipeApiService;
import com.example.android.bakeme.models.Recipe;
import com.example.android.bakeme.networking.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListFragment extends Fragment {

    private Unbinder mUnbinder;
    private ArrayList<Recipe> mRecipeList;
    private RecipeListAdapter mRecipeListAdapter;
    private MainActivity parentActivity;


    @BindView(R.id.recipe_list_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.loading_spinner) ProgressBar mProgressBar;
    @BindView(R.id.empty_list_view) TextView mEmptyListTextView;


    // mandatory empty constructor
    public RecipeListFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        parentActivity = (MainActivity) getActivity();

        final View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mUnbinder = ButterKnife.bind(this, rootView);

        // initialize RecyclerView properties
        mRecyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        mRecyclerView.hasFixedSize();

        // create instance of RecipeListAdapter
        mRecipeListAdapter = new RecipeListAdapter();
        mRecipeListAdapter.setRecipeList(mRecipeList);

        // connect the RecyclerView with the Adapter
        mRecyclerView.setAdapter(mRecipeListAdapter);

        mEmptyListTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        if (hasNetworkConnection()){
            getRecipeDataFromApi();
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mEmptyListTextView.setText(getString(R.string.no_connection_found));
            mEmptyListTextView.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    public boolean hasNetworkConnection(){

        // check there is a network connection
        ConnectivityManager cm = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();


        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    public void getRecipeDataFromApi(){

        RecipeApiService recipeApiService = RetrofitClientInstance
                .getRetrofitInstance(parentActivity).create(RecipeApiService.class);

        final Call<List<Recipe>> recipeCall = recipeApiService.getRecipes();

        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if (response.isSuccessful()){

                    mRecipeList = (ArrayList<Recipe>) response.body();
                    MainActivity.arrayList = mRecipeList;
                    mRecipeListAdapter.setRecipeList(mRecipeList);
                    mRecipeListAdapter.notifyDataSetChanged();

                    mRecyclerView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mEmptyListTextView.setVisibility(View.GONE);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                    mEmptyListTextView.setVisibility(View.VISIBLE);
                    mEmptyListTextView.setText(getString(R.string.placeholder_no_recipes));
                }

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable throwable) {
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mEmptyListTextView.setVisibility(View.VISIBLE);
                mEmptyListTextView.setText(getString(R.string.placeholder_no_recipes));
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
