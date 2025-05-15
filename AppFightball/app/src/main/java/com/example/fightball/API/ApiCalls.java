package com.example.fightball.API;

// Importación de modelos de datos utilizados en las llamadas
import com.example.fightball.Models.CharacterModel;
import com.example.fightball.Models.EnemyModel;
import com.example.fightball.Models.ItemModel;
import com.example.fightball.Models.ItemModelAdmin;
import com.example.fightball.Models.LoginModel;
import com.example.fightball.Models.MatchModel;
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
 * Clase ApiCalls
 * Encapsula las llamadas a la API definidas en la interfaz ApiInterface.
 * Sirve como intermediario entre el cliente (app Android) y el backend REST.
 */
public class ApiCalls {

    // Instancia de la interfaz Retrofit
    private ApiInterface apiInterface;

    /**
     * Constructor que recibe una instancia de ApiInterface.
     * @param apiInterface Interfaz que define las llamadas HTTP.
     */
    public ApiCalls(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    // =========================================================================
    // =                        AUTENTICACIÓN Y USUARIOS                       =
    // =========================================================================

    /**
     * Inicia sesión con el modelo de login (usuario y contraseña).
     * @param user Objeto LoginModel con credenciales.
     * @return Llamada con token JWT (tipo String).
     */
    public Call<String> login(LoginModel user) {
        return apiInterface.login(user);
    }

    /**
     * Obtiene todos los usuarios del sistema.
     * @param key Token de autorización.
     * @return Lista de usuarios.
     */
    public Call<List<UserModel>> getAllUsers(String key) {
        return apiInterface.getAllUsers(key);
    }

    /**
     * Edita los roles de un usuario específico.
     * @param authHeader Token de autorización.
     * @param id ID del usuario a editar.
     * @param user Objeto con los nuevos roles del usuario.
     * @return Llamada con el objeto actualizado.
     */
    public Call<UserRolesModel> editUser(String authHeader, int id, UserRolesModel user) {
        return apiInterface.editUser(authHeader, id, user);
    }

    /**
     * Elimina un usuario por ID.
     * @param authHeader Token de autorización.
     * @param id ID del usuario a eliminar.
     * @return Llamada con valor booleano de éxito.
     */
    public Call<Boolean> deleteUser(String authHeader, int id) {
        return apiInterface.deleteUser(authHeader, id);
    }

    // =========================================================================
    // =                              ROLES                                    =
    // =========================================================================

    /**
     * Obtiene los IDs de roles a partir del nombre del rol.
     * @param name Nombre del rol.
     * @param key Token de autorización.
     * @return Lista de IDs.
     */
    public Call<List<Integer>> rolesIdsByname(String name, String key) {
        return apiInterface.rolesIdsByanme(name, key);
    }

    /**
     * Obtiene todos los roles del sistema.
     * @param key Token de autorización.
     * @return Lista de roles.
     */
    public Call<List<RoleModel>> getAllRoles(String key) {
        return apiInterface.getAllRoles(key);
    }

    // =========================================================================
    // =                             PERSONAJES                                =
    // =========================================================================

    /**
     * Obtiene todos los personajes disponibles.
     * @param authHeader Token de autorización.
     * @return Lista de personajes.
     */
    public Call<List<CharacterModel>> getCharacters(String authHeader) {
        return apiInterface.getCharacters(authHeader);
    }

    // =========================================================================
    // =                                ÍTEMS                                  =
    // =========================================================================

    /**
     * Obtiene todos los ítems disponibles.
     * @param key Token de autorización.
     * @return Lista de ítems.
     */
    public Call<List<ItemModel>> getItems(String key) {
        return apiInterface.getItems(key);
    }

    public Call<List<ItemModelAdmin>> getItemsAdmin(String key) {
        return apiInterface.getItemsAdmin(key);
    }

    /**
     * Elimina un ítem por su ID.
     * @param authHeader Token de autorización.
     * @param id ID del ítem.
     * @return Llamada con valor booleano de éxito.
     */
    public Call<Boolean> deleteItem(String authHeader, int id) {
        return apiInterface.deleteItem(authHeader, id);
    }

    /**
     * Crea un nuevo ítem.
     * @param authHeader Token de autorización.
     * @param item Objeto del nuevo ítem.
     * @return Ítem creado.
     */
    public Call<ItemModelAdmin> createItem(String authHeader, ItemModelAdmin item) {
        return apiInterface.createItem(authHeader, item);
    }
    /**
     * Edita un itemm
     * @param authHeader Token de autorización.
     * @param id id del item que se busca editar
     * @param item Objeto del nuevo ítem.
     * @return Ítem creado.
     */
    public Call<ItemModelAdmin> editItem(String authHeader, int id, ItemModelAdmin item){
        return apiInterface.EditItem(authHeader,id,item);
    }


    // =========================================================================
    // =                               PARTIDAS                                =
    // =========================================================================

    /**
     * Obtiene todas las partidas registradas.
     * @param key Token de autorización.
     * @return Lista de partidas.
     */
    public Call<List<MatchModel>> getALlMatches(String key) {
        return apiInterface.getAllMatches(key);
    }

    /**
     * Obtiene las partidas ganadas por un usuario.
     * @param name Nombre del usuario.
     * @param key Token de autorización.
     * @return Lista de partidas ganadas.
     */
    public Call<List<MatchModel>> winMatchesbyName(String name, String key) {
        return apiInterface.getWinMatches(name, key);
    }

    /**
     * Obtiene las partidas perdidas por un usuario.
     * @param name Nombre del usuario.
     * @param key Token de autorización.
     * @return Lista de partidas perdidas.
     */
    public Call<List<MatchModel>> lostMatchesbyName(String name, String key) {
        return apiInterface.getLostMatches(name, key);
    }

    /**
     * Obtiene todas las partidas (ganadas y perdidas) por nombre de usuario.
     * @param name Nombre del usuario.
     * @param key Token de autorización.
     * @return Lista de partidas del usuario.
     */
    public Call<List<MatchModel>> geMatchesByName(String name, String key) {
        return apiInterface.geMatchesByName(name, key);
    }

    // =========================================================================
    // =                               ENEMIES                             =
    // =========================================================================

    /**
     * Obtiene todos los enemigos registrados.
     * @param key Token de autorización.
     * @return Lista de enemigos.
     */
    public Call<List<EnemyModel>> getAllEnemies(String key) {
        return apiInterface.getenemys(key);
    }

    /**
     * Elimina un enemigo específico.
     * @param key Token de autorización.
     * @param id ID del enemigo a eliminar.
     * @return Resultado de la operación (true/false).
     */
    public Call<Boolean> deleteEnemy(String key, int id) {
        return apiInterface.deletEnemy(key, id);
    }

    /**
     * Crea un nuevo enemigo.
     * @param key Token de autorización.
     * @param enemy Datos del enemigo a crear.
     * @return Enemigo creado.
     */
    public Call<EnemyModel> createEnemy(String key, EnemyModel enemy) {
        return apiInterface.creatEnemy(key, enemy);
    }

    /**
     * Edita un enemigo existente.
     * @param key Token de autorización.
     * @param id ID del enemigo a editar.
     * @param enemy Datos actualizados del enemigo.
     * @return Enemigo actualizado.
     */
    public Call<EnemyModel> editEnemy(String key, int id, EnemyModel enemy) {
        return apiInterface.EditEnemy(key, id, enemy);
    }


    // =========================================================================
    // =                               Tutorial                             =
    // =========================================================================


    /**
     * Obtiene todos los tutoriales.
     * @param key Token de autorización.
     * @return Lista de tutoriales.
     */
    public Call<List<TutorialModel>> getTutorials(String key) {
        return apiInterface.getTutorials(key);
    }

    /**
     * Elimina un tutorial por su ID.
     * @param authHeader Token de autorización.
     * @param id ID del tutorial.
     * @return Llamada con valor booleano de éxito.
     */
    public Call<Boolean> deleteTutorial(String authHeader, int id) {
        return apiInterface.deleteTutorial(authHeader, id);
    }

    /**
     * Crea un nuevo tutorial.
     * @param authHeader Token de autorización.
     * @param tutorial Objeto del nuevo tutorial.
     * @return Tutorial creado.
     */
    public Call<TutorialModel> createTutorial(String authHeader, TutorialModel tutorial) {
        return apiInterface.createTutorial(authHeader, tutorial);
    }

    /**
     * Edita un tutorial existente.
     * @param authHeader Token de autorización.
     * @param id ID del tutorial.
     * @param tutorial Objeto del tutorial actualizado.
     * @return Tutorial actualizado.
     */
    public Call<TutorialModel> editTutorial(String authHeader, int id, TutorialModel tutorial) {
        return apiInterface.editTutorial(authHeader, id, tutorial);
    }


}
