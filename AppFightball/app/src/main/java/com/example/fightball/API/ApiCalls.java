package com.example.fightball.API;

import com.example.fightball.Models.CharacterModel;
import com.example.fightball.Models.ItemModel;
import com.example.fightball.Models.LoginModel;
import com.example.fightball.Models.MatchModel;
import com.example.fightball.Models.RoleModel;
import com.example.fightball.Models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

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

    public Call<List<MatchModel>>getALlMatches(String key){
        return apiInterface.getAllMatches(key);
    }
    public Call<List<MatchModel>>winMatchesbyName(String name,String key){
        return apiInterface.getWinMatches(name,key);
    }
    public Call<List<MatchModel>>lostMatchesbyName(String name,String key){
        return apiInterface.getLostMatches(name,key);
    }
    public Call<List<MatchModel>>geMatchesByName(String name, String key){
        return apiInterface.geMatchesByName(name,key);
    }
    public Call<List <ItemModel>> getItems(String key){
        return apiInterface.getItems(key);
    }
   public Call<List<UserModel>>getAllUsers(String key){
        return apiInterface.getAllUsers(key);
   }

    public Call<List<RoleModel>>getAllRoles( String key){
        return apiInterface.getAllRoles(key);
    }
    public Call<UserModel> deleteUser(String authHeader ,UserModel userModel){
        return apiInterface.deleteUser(authHeader,userModel);
    }



}
