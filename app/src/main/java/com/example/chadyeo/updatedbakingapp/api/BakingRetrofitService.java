package com.example.chadyeo.updatedbakingapp.api;

import com.example.chadyeo.updatedbakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingRetrofitService {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
