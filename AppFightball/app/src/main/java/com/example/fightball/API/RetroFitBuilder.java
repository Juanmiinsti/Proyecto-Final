package com.example.fightball.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase RetroFitBuilder
 * Implementa un patrón Singleton para gestionar la creación y configuración
 * de la instancia Retrofit utilizada para las llamadas a la API REST.
 *
 * Esta clase se encarga de construir el cliente Retrofit con un convertidor Gson
 * adaptado a la API y proporciona acceso a la interfaz ApiInterface.
 */
public class RetroFitBuilder {
    // Instancia única de RetroFitBuilder (Singleton)
    private static RetroFitBuilder instance = null;

    // Constructor de Retrofit.Builder para configurar Retrofit
    private Retrofit.Builder builder;

    // Instancia de la interfaz API generada por Retrofit
    private ApiInterface apiInterface;

    /**
     * Constructor privado para evitar instanciación externa (Singleton).
     */
    private RetroFitBuilder() {
    }

    /**
     * Método estático para obtener la instancia única de RetroFitBuilder.
     * Crea la instancia si aún no existe.
     *
     * @return Instancia única de RetroFitBuilder.
     */
    public static RetroFitBuilder getInstance() {
        if (instance == null) {
            instance = new RetroFitBuilder();
        }
        return instance;
    }

    /**
     * Configura y construye la instancia Retrofit con la URL base proporcionada.
     * Utiliza un convertidor Gson leniente para adaptarse al formato JSON de la API.
     *
     * @param url URL base para las llamadas a la API REST.
     */
    public void build(String url) {
        Gson gson = new GsonBuilder()
                .setLenient() // Ajusta al formato JSON que devuelve la API (más flexible)
                .create();

        builder = new Retrofit.Builder();
        builder.baseUrl(url);
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    /**
     * Devuelve una instancia de ApiCalls, que utiliza la interfaz ApiInterface
     * para realizar las llamadas a la API.
     *
     * @return Objeto ApiCalls configurado para hacer peticiones HTTP.
     */
    public ApiCalls callApi() {
        return new ApiCalls(apiInterface);
    }
}
