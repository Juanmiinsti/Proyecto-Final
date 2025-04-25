package com.example.fightball.API;

import com.example.fightball.PostModels.CharacterModel;

import java.util.List;

import retrofit2.Call;

public class ApiCalls {
    private ApiInterface apiInterface;
    public ApiCalls(ApiInterface apiInterface) {
        this.apiInterface=apiInterface;
    }
    public Call<List<CharacterModel>> getCharacters(){
        return apiInterface.getCharacters();
    }
}
