package com.example.fightball.API;

import com.example.fightball.Models.CharacterModel;
import com.example.fightball.Models.LoginModel;
import com.example.fightball.Models.MatchModel;

import java.util.List;

import retrofit2.Call;

public class ApiCalls {
    private ApiInterface apiInterface;
    public ApiCalls(ApiInterface apiInterface) {
        this.apiInterface=apiInterface;
    }
    public Call<List<CharacterModel>> getCharacters(String authHeader){
        return apiInterface.getCharacters(authHeader);
    }
    public Call<String>login(LoginModel user){
        return apiInterface.login(user);
    }
    public Call<List<Integer>>rolesIdsByname(String name,String key){
        return apiInterface.rolesIdsByanme(name,key);
    }
    public Call<List<MatchModel>>winMatchesbyName(String name,String key){
        return apiInterface.getWinMatches(name,key);
    }
    public Call<List<MatchModel>>lostMatchesbyName(String name,String key){
        return apiInterface.getLostMatches(name,key);
    }
}
