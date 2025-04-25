package com.example.fightball.API;

import com.example.fightball.PostModels.CharacterModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("/api/characters")
    Call<List <CharacterModel>> getCharacters();
}
