package com.example.fightball.API;

import com.example.fightball.Models.CharacterModel;
import com.example.fightball.Models.EnemyModel;
import com.example.fightball.Models.ItemModel;
import com.example.fightball.Models.ItemModelAdmin;
import com.example.fightball.Models.LoginModel;
import com.example.fightball.Models.MatchModel;
import com.example.fightball.Models.RegisterResponse;
import com.example.fightball.Models.RoleModel;
import com.example.fightball.Models.TutorialModel;
import com.example.fightball.Models.UserModel;
import com.example.fightball.Models.UserRolesModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    //CHARACTERS ------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------------//

    @GET("/api/characters")
    Call<List <CharacterModel>> getCharacters(@Header("Authorization")String authHeader);

    //MATCHES ------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------------//

    @GET("api/role/{name}")
    Call <List<Integer>> rolesIdsByanme(@Path("name")String name, @Header("Authorization")String authHeader);
    @GET("/api/matches")
    Call<List<MatchModel>>getAllMatches(@Header("Authorization") String authHeader);
    @GET("/api/matches/total/{name}")
    Call<List<MatchModel>>geMatchesByName(@Path("name")String name,@Header("Authorization") String authHeader);

    @GET("/api/matches/win/{name}")
    Call<List<MatchModel>>getWinMatches(@Path("name")String name,@Header("Authorization") String authHeader);

    @GET("/api/matches/lost/{name}")
    Call<List<MatchModel>>getLostMatches(@Path("name")String name,@Header("Authorization") String authHeader);

    //ITEMS ------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------------//
    @GET("/api/items")
    Call<List <ItemModel>> getItems(@Header("Authorization")String authHeader);
    @GET("/api/items")
    Call<List <ItemModelAdmin>> getItemsAdmin(@Header("Authorization")String authHeader);
    @DELETE("/api/items/{id}")
    Call<Boolean> deleteItem(@Header("Authorization")String authHeader,@Path("id") int id);
    @POST("/api/items")
    Call<ItemModelAdmin> createItem(@Header("Authorization")String authHeader,@Body ItemModelAdmin item);

    @PUT("/api/items/{id}")
    Call<ItemModelAdmin> EditItem(@Header("Authorization")String authHeader, @Path("id") int id, @Body ItemModelAdmin item);

    //USERS ------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------------//

    @GET("/api/users")
    Call<List<UserModel>>getAllUsers(@Header("Authorization") String authHeader);
    @PUT("/api/users/{id}")
    Call<UserRolesModel>editUser(@Header("Authorization") String authHeader,@Path("id") int id,@Body UserRolesModel user);
    //ROLE ------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------------//
    @GET("/api/role")
    Call<List<RoleModel>>getAllRoles(@Header("Authorization") String authHeader);
    //LOGIN AND REGISTER ------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------------//

    @POST("/auth/login")
    Call<String> login(@Body LoginModel user);

    @POST("/auth/signup")
    Call<RegisterResponse>registerUser(@Header("Authorization") String authHeader, @Body UserModel user);
    @DELETE("/api/users/{id}")
    Call<Boolean> deleteUser(@Header("Authorization" ) String authHeader ,@Path("id") int id);

    //ENEMY------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------------//
    @GET("/api/enemies")
    Call<List <EnemyModel>> getenemys(@Header("Authorization")String authHeader);
    @DELETE("/api/enemies/{id}")
    Call<Boolean> deletEnemy(@Header("Authorization")String authHeader,@Path("id") int id);
    @POST("/api/enemies")
    Call<EnemyModel> creatEnemy(@Header("Authorization")String authHeader,@Body EnemyModel item);

    @PUT("/api/enemies/{id}")
    Call<EnemyModel> EditEnemy(@Header("Authorization")String authHeader, @Path("id") int id, @Body EnemyModel item);

    //TUTORIALS------------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------------------//
    // ---------------------------------------------------------------------------------------------------------------------//


    @GET("/api/tutorials")
    Call<List<TutorialModel>> getTutorials(@Header("Authorization") String authHeader);

    @DELETE("/api/tutorials/{id}")
    Call<Boolean> deleteTutorial(@Header("Authorization") String authHeader, @Path("id") int id);

    @POST("/api/tutorials")
    Call<TutorialModel> createTutorial(@Header("Authorization") String authHeader, @Body TutorialModel tutorial);

    @PUT("/api/tutorials/{id}")
    Call<TutorialModel> editTutorial(@Header("Authorization") String authHeader, @Path("id") int id, @Body TutorialModel tutorial);




}
