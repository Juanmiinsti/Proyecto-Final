package com.example.fightball.API;

// Import data models used in the calls
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
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Class ApiCalls
 * Encapsulates the API calls defined in the ApiInterface interface.
 * Serves as an intermediary between the client (Android app) and the REST backend.
 */
public class ApiCalls {

    // Instance of the Retrofit interface
    private ApiInterface apiInterface;

    /**
     * Constructor that receives an instance of ApiInterface.
     * @param apiInterface Interface that defines the HTTP calls.
     */
    public ApiCalls(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    // =========================================================================
    // =                        AUTHENTICATION AND USERS                      =
    // =========================================================================

    /**
     * Logs in with the login model (username and password).
     * @param user LoginModel object with credentials.
     * @return Call returning JWT token (String type).
     */
    public Call<String> login(LoginModel user) {
        return apiInterface.login(user);
    }

    /**
     * Retrieves all users in the system.
     * @param key Authorization token.
     * @return List of users.
     */
    public Call<List<UserModel>> getAllUsers(String key) {
        return apiInterface.getAllUsers(key);
    }

    public Call<RegisterResponse> registerUser(String authHeader, UserModel user){
        return  apiInterface.registerUser(authHeader,user);
    }

    /**
     * Edits the roles of a specific user.
     * @param authHeader Authorization token.
     * @param id ID of the user to edit.
     * @param user Object with the user's new roles.
     * @return Call with the updated object.
     */
    public Call<UserRolesModel> editUser(String authHeader, int id, UserRolesModel user) {
        return apiInterface.editUser(authHeader, id, user);
    }

    /**
     * Deletes a user by ID.
     * @param authHeader Authorization token.
     * @param id ID of the user to delete.
     * @return Call with a boolean success value.
     */
    public Call<Boolean> deleteUser(String authHeader, int id) {
        return apiInterface.deleteUser(authHeader, id);
    }

    // =========================================================================
    // =                              ROLES                                   =
    // =========================================================================

    /**
     * Retrieves role IDs by role name.
     * @param name Role name.
     * @param key Authorization token.
     * @return List of IDs.
     */
    public Call<List<Integer>> rolesIdsByname(String name, String key) {
        return apiInterface.rolesIdsByanme(name, key);
    }

    /**
     * Retrieves all roles in the system.
     * @param key Authorization token.
     * @return List of roles.
     */
    public Call<List<RoleModel>> getAllRoles(String key) {
        return apiInterface.getAllRoles(key);
    }

    // =========================================================================
    // =                            CHARACTERS                                =
    // =========================================================================

    /**
     * Retrieves all available characters.
     * @param authHeader Authorization token.
     * @return List of characters.
     */
    public Call<List<CharacterModel>> getCharacters(String authHeader) {
        return apiInterface.getCharacters(authHeader);
    }

    // =========================================================================
    // =                                ITEMS                                 =
    // =========================================================================

    /**
     * Retrieves all available items.
     * @param key Authorization token.
     * @return List of items.
     */
    public Call<List<ItemModel>> getItems(String key) {
        return apiInterface.getItems(key);
    }

    public Call<List<ItemModelAdmin>> getItemsAdmin(String key) {
        return apiInterface.getItemsAdmin(key);
    }

    /**
     * Deletes an item by its ID.
     * @param authHeader Authorization token.
     * @param id Item ID.
     * @return Call with a boolean success value.
     */
    public Call<Boolean> deleteItem(String authHeader, int id) {
        return apiInterface.deleteItem(authHeader, id);
    }

    /**
     * Creates a new item.
     * @param authHeader Authorization token.
     * @param item New item object.
     * @return Created item.
     */
    public Call<ItemModelAdmin> createItem(String authHeader, ItemModelAdmin item) {
        return apiInterface.createItem(authHeader, item);
    }

    /**
     * Edits an item.
     * @param authHeader Authorization token.
     * @param id ID of the item to edit.
     * @param item New item object.
     * @return Edited item.
     */
    public Call<ItemModelAdmin> editItem(String authHeader, int id, ItemModelAdmin item){
        return apiInterface.EditItem(authHeader,id,item);
    }

    // =========================================================================
    // =                               MATCHES                                =
    // =========================================================================

    /**
     * Retrieves all registered matches.
     * @param key Authorization token.
     * @return List of matches.
     */
    public Call<List<MatchModel>> getALlMatches(String key) {
        return apiInterface.getAllMatches(key);
    }

    /**
     * Retrieves matches won by a user.
     * @param name Username.
     * @param key Authorization token.
     * @return List of won matches.
     */
    public Call<List<MatchModel>> winMatchesbyName(String name, String key) {
        return apiInterface.getWinMatches(name, key);
    }

    /**
     * Retrieves matches lost by a user.
     * @param name Username.
     * @param key Authorization token.
     * @return List of lost matches.
     */
    public Call<List<MatchModel>> lostMatchesbyName(String name, String key) {
        return apiInterface.getLostMatches(name, key);
    }

    /**
     * Retrieves all matches (won and lost) by username.
     * @param name Username.
     * @param key Authorization token.
     * @return List of user's matches.
     */
    public Call<List<MatchModel>> geMatchesByName(String name, String key) {
        return apiInterface.geMatchesByName(name, key);
    }

    // =========================================================================
    // =                               ENEMIES                                =
    // =========================================================================

    /**
     * Retrieves all registered enemies.
     * @param key Authorization token.
     * @return List of enemies.
     */
    public Call<List<EnemyModel>> getAllEnemies(String key) {
        return apiInterface.getenemys(key);
    }

    /**
     * Deletes a specific enemy.
     * @param key Authorization token.
     * @param id Enemy ID to delete.
     * @return Operation result (true/false).
     */
    public Call<Boolean> deleteEnemy(String key, int id) {
        return apiInterface.deletEnemy(key, id);
    }

    /**
     * Creates a new enemy.
     * @param key Authorization token.
     * @param enemy Enemy data to create.
     * @return Created enemy.
     */
    public Call<EnemyModel> createEnemy(String key, EnemyModel enemy) {
        return apiInterface.creatEnemy(key, enemy);
    }

    /**
     * Edits an existing enemy.
     * @param key Authorization token.
     * @param id Enemy ID to edit.
     * @param enemy Updated enemy data.
     * @return Updated enemy.
     */
    public Call<EnemyModel> editEnemy(String key, int id, EnemyModel enemy) {
        return apiInterface.EditEnemy(key, id, enemy);
    }

    // =========================================================================
    // =                               TUTORIAL                               =
    // =========================================================================

    /**
     * Retrieves all tutorials.
     * @param key Authorization token.
     * @return List of tutorials.
     */
    public Call<List<TutorialModel>> getTutorials(String key) {
        return apiInterface.getTutorials(key);
    }

    /**
     * Deletes a tutorial by its ID.
     * @param authHeader Authorization token.
     * @param id Tutorial ID.
     * @return Call with a boolean success value.
     */
    public Call<Boolean> deleteTutorial(String authHeader, int id) {
        return apiInterface.deleteTutorial(authHeader, id);
    }

    /**
     * Creates a new tutorial.
     * @param authHeader Authorization token.
     * @param tutorial New tutorial object.
     * @return Created tutorial.
     */
    public Call<TutorialModel> createTutorial(String authHeader, TutorialModel tutorial) {
        return apiInterface.createTutorial(authHeader, tutorial);
    }

    /**
     * Edits an existing tutorial.
     * @param authHeader Authorization token.
     * @param id Tutorial ID.
     * @param tutorial Updated tutorial object.
     * @return Updated tutorial.
     */
    public Call<TutorialModel> editTutorial(String authHeader, int id, TutorialModel tutorial) {
        return apiInterface.editTutorial(authHeader, id, tutorial);
    }

}
