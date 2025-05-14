package es.pmdm.jsoncompleto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import api.*;

public class MainActivity extends AppCompatActivity {

    private ListView lstPosts;
    private final static int CODIGO_RESPUESTA_ACTIVIDAD_DETALLE = 1;
    private ArrayAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstPosts = findViewById(R.id.lstPosts);

        // Creamos el adaptador usando un ArrayAdapter (de momento vacío) y el layout simple_list_item_2
        List<PostModel> list = new ArrayList<>();
        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                PostModel model = (PostModel)getItem(position);
                text1.setText(cortarCadenaMaximo(model.getTitle(), 30));
                text2.setText(cortarCadenaMaximo(model.getBody().replace("\n", " "), 45));
                return view;
            }
        };
        lstPosts.setAdapter(adaptador);

        // Listener pulsación
        lstPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostModel model = (PostModel) adaptador.getItem(position);
                Log.d(getClass().getName(), model.toString());

                Intent intent = new Intent(MainActivity.this, VistaDetalleActivity.class);
                intent.putExtra("POST_ID", model.getId());
                startActivityForResult(intent, CODIGO_RESPUESTA_ACTIVIDAD_DETALLE);
            }
        });

        // Iniciamos la descarga de los posts
        descargarPosts();
    }
    private String cortarCadenaMaximo(String str, int max) {
        if(str.length() > max) {
            str = str.substring(0, max) + "...";
        }
        return str;
    }
    private void descargarPosts() {
        API.getPosts(new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    List<PostModel> list = UtilJSONParser.parseArrayPosts(r.content);
                    Log.d(getClass().getName(), "Posts descargados: " + list.size());
                    adaptador.clear();
                    adaptador.addAll(list);
                    adaptador.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Codigo Respuesta: " + r.responseCode, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error al analizar el contenido JSON", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(UtilREST.Response r) {
                Toast.makeText(MainActivity.this, "Error al descargar el contenido " + r.exception, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_RESPUESTA_ACTIVIDAD_DETALLE && resultCode == RESULT_OK) {
            // Volvemos a descargar los posts para actualizar con los cambios...
            descargarPosts();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        // Asociamos el menú mediante el fichero de recurso
        getMenuInflater().inflate(R.menu.menu_superior, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem elemento) {
        super.onOptionsItemSelected(elemento);

        if(elemento.getItemId() == R.id.anadir) {
            agregarPostAleatorio();
            return true;
        }
        return false;
    }

    private void agregarPostAleatorio() {
        new AlertDialog.Builder(this)
                .setTitle("Agregar")
                .setMessage("¿Desea crear un nuevo post?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        JSONObject jsonPost = UtilJSONParser.createPost(Integer.parseInt("500"), Integer.parseInt("2"), "Prueba", "Prueba de nuevo post.....");
                        API.postPost(jsonPost, new UtilREST.OnResponseListener() {
                            @Override
                            public void onSuccess(UtilREST.Response r) {
                                Toast.makeText(MainActivity.this, "Post Creado. Codigo Respuesta: " + r.responseCode, Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onError(UtilREST.Response r) {
                                Toast.makeText(MainActivity.this, "Error al crear el post", Toast.LENGTH_SHORT).show();  // Si falla, permanezco en la pantalla detalle.
                            }
                        });
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}