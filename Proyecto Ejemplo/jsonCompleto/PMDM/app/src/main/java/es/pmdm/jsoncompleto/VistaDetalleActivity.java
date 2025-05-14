package es.pmdm.jsoncompleto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import api.API;
import api.PostModel;
import api.UtilJSONParser;
import api.UtilREST;

public class VistaDetalleActivity extends AppCompatActivity {

    private final static int CODIGO_RESPUESTA_ACTIVIDAD_DETALLE = 1;
    private TextView txtId;
    private TextView txtUserId;
    private TextView txtTitle;
    private TextView txtBody;
    private int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_detalle);

        txtId = (TextView)findViewById(R.id.tvId);
        txtUserId = (TextView)findViewById(R.id.tvUserId);
        txtTitle = (TextView)findViewById(R.id.tvTitle);
        txtBody = (TextView)findViewById(R.id.tvBody);

        Intent intent = getIntent();
        if(intent == null) {
            returnResult("Error: no se ha especificado ningún post", RESULT_CANCELED);
            return;
        }
        id = intent.getIntExtra("POST_ID", -1);
        Log.d(getClass().getName(), "Id: " + id);
        if(id == -1) {
            returnResult("Error: post incorrecto", RESULT_CANCELED);
            return;
        }

        // Listener Editar
        findViewById(R.id.btnEditar).setOnClickListener(v -> {
            Intent intent2 = new Intent(VistaDetalleActivity.this, VistaEditarActivity.class);
            intent2.putExtra("POST_ID", id);
            startActivityForResult(intent2, CODIGO_RESPUESTA_ACTIVIDAD_DETALLE);
        });

        // Listener Eliminar
        findViewById(R.id.btnEliminar).setOnClickListener( v -> {
            eliminarPost(id);
        });

        // Descarga datos post concreto y actualizo la interfaz
        actualizarInterfaz();
    }

    private void actualizarInterfaz() {
        API.getPost(id, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                PostModel post = UtilJSONParser.parsePost(r.content);
                txtId.setText(String.valueOf(post.getId()));
                txtUserId.setText(String.valueOf(post.getUserId()));
                txtTitle.setText(post.getTitle());
                txtBody.setText(post.getBody());
                Toast.makeText(VistaDetalleActivity.this, "Codigo Respuesta: " + r.responseCode, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(UtilREST.Response r) {
                Toast.makeText(VistaDetalleActivity.this, "Error al descargar el contenido", Toast.LENGTH_SHORT).show();
                returnResult("Error al descargar los datos", RESULT_CANCELED);
            }
        });
    }

    private void returnResult(String str, int resultCode) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        setResult(resultCode);
        finish();
    }

    private void eliminarPost(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar")
                .setMessage("¿Desea eliminar este post?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        API.deletePost(id, new UtilREST.OnResponseListener() {
                            @Override
                            public void onSuccess(UtilREST.Response r) {
                                returnResult("El post se ha eliminado", RESULT_OK);  // si se elimina correctamente, vuelvo a la pantalla con el listado de posts.
                            }
                            @Override
                            public void onError(UtilREST.Response r) {
                                Toast.makeText(VistaDetalleActivity.this, "Error al eliminar el post", Toast.LENGTH_SHORT).show();  // Si falla, permanezco en la pantalla detalle.
                            }
                        });
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}