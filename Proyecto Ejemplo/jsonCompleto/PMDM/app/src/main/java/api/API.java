package api;

import org.json.JSONObject;
import api.UtilREST.*;

// Proporciona métodos para interactuar con el API RESTful.
public class API {

    private static final String BASE_URL = "http://jsonplaceholder.typicode.com/";

    // Solicitud GET a la URL de la API para obtener todos los posts.
    // Usa un UtilREST.OnResponseListener como parámetro para manejar la respuesta de la solicitud.
    public static void getPosts(UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(UtilREST.QueryType.GET, BASE_URL + "posts", listener);
    }

    // Similar al método anterior, usa también el id para obtener un post en concreto.
    public static void getPost(int id, UtilREST.OnResponseListener listener) {
        if (id < 0) {
            throw new IllegalArgumentException("ID de post no válido");
        }
        UtilREST.runQuery(QueryType.GET, BASE_URL + "posts/" + id, listener);
    }

    // Solicitud POST a la URL de la API para crear un nuevo post.
    // Usa un objeto JSONObject que representa el post que se creará, así como un UtilREST.OnResponseListener para manejar la respuesta.
    public static void postPost(JSONObject post, UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(QueryType.POST, BASE_URL + "posts", post.toString(), listener);
    }

    // Solicitud PUT a la URL de la API para actualizar un post existente.
    // Usa un ID de post (id), un objeto JSONObject que contiene los datos actualizados del post, y un UtilREST.OnResponseListener para manejar la respuesta.
    public static void putPost(int id, JSONObject post, UtilREST.OnResponseListener listener) {
        if (id < 0) {
            throw new IllegalArgumentException("ID de post no válido");
        }
        UtilREST.runQuery(QueryType.PUT, BASE_URL + "posts/" + id, post.toString(), listener);
    }

    // Solicitud DELETE a la URL de la API para eliminar un post existente.
    // Usa el id del post a eliminar y un UtilREST.OnResponseListener para manejar la respuesta.
    public static void deletePost(int id, UtilREST.OnResponseListener listener) {
        if (id < 0) {
            throw new IllegalArgumentException("ID de post no válido");
        }
        UtilREST.runQuery(QueryType.DELETE, BASE_URL + "posts/" + id, listener);
    }
}
