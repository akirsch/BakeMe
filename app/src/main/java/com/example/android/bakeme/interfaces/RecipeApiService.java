package com.example.android.bakeme.interfaces;

import com.example.android.bakeme.models.Recipe;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeApiService {

    @GET("/baking.json")
    Call<List<Recipe>> getRecipes();
}
