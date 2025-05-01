package com.example.fightball.API;

import com.example.fightball.Models.CharacterModel;
import com.example.fightball.Models.ItemModel;
import com.example.fightball.Models.LoginModel;
import com.example.fightball.Models.MatchModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("/api/object")
    Call<List <ItemModel>> getItems(@Header("Authorization")String authHeader);

    @GET("api/role/{name}")
    Call <List<Integer>> rolesIdsByanme(@Path("name")String name, @Header("Authorization")String authHeader);
    @GET("/api/characters")
    Call<List <CharacterModel>> getCharacters(@Header("Authorization")String authHeader);
    @GET("/api/matches/total/{name}")
    Call<List<MatchModel>>geMatchesByName(@Path("name")String name,@Header("Authorization") String authHeader);

    @GET("/api/matches/win/{name}")
    Call<List<MatchModel>>getWinMatches(@Path("name")String name,@Header("Authorization") String authHeader);

    @GET("/api/matches/lost/{name}")
    Call<List<MatchModel>>getLostMatches(@Path("name")String name,@Header("Authorization") String authHeader);

    @POST("/auth/login")
    Call<String> login(@Body LoginModel user);


}
