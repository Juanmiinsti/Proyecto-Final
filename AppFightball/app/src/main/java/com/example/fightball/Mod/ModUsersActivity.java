package com.example.fightball.Mod;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.Adapters.MatchAdapter;
import com.example.fightball.Adapters.UserAdapter;
import com.example.fightball.Models.RoleModel;
import com.example.fightball.Models.UserModel;
import com.example.fightball.R;

public class ModUsersActivity extends AppCompatActivity {
    public static UserAdapter adapter;
    ListView userVisual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mod_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }

    private void config() {
        listofUsers();
        registerForContextMenu(userVisual);

    }

    private void listofUsers() {
        adapter = new UserAdapter(this, R.layout.usermod_item, ModMainActivity.users);
        userVisual = findViewById(R.id.usersModList);
        userVisual.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contextual, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) {
            return super.onContextItemSelected(item);
        }
        try {
            UserModel selectedUser = (UserModel) userVisual.getItemAtPosition(info.position);
            if (selectedUser == null) {
                Log.e("ContextMenu", "Usuario seleccionado es nulo");
                return super.onContextItemSelected(item);
            }

            int id = item.getItemId();

            if (id == R.id.idBusqueda) {
                Log.d("ContextMenu", "Iniciando búsqueda para: " + selectedUser.getName());
                String query = selectedUser.getName().trim();

                if (!query.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, query);
                    // Verifica si hay aplicaciones que puedan manejar el intent
                    PackageManager pm = getPackageManager();
                    if (intent.resolveActivity(pm) != null) {
                        startActivity(intent);
                        System.out.println("normal");
                    } else {
                        // Alternativa si no hay navegador
                        System.out.println("manualmente");
                        String searchUrl = "https://www.google.com/search?q=" + Uri.encode(query);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl));
                        startActivity(browserIntent);
                    }
                } else {
                    Toast.makeText(this, "El nombre de usuario está vacío", Toast.LENGTH_SHORT).show();
                }


            } else if (id == R.id.idEnviarCorreo) {
                Log.d("ContextMenu", "Preparando correo para: " + selectedUser.getName());

                // Crear el Intent para enviar correo
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                // Configurar el tipo de contenido (para correo electrónico)
                emailIntent.setType("message/rfc822"); // Tipo MIME para correo

                // Asunto del correo
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contacto desde la app");

                // Cuerpo del mensaje
                String emailBody = "Hola " + selectedUser.getName() + ",\n\n";
                emailBody += "Este es un mensaje enviado desde mi aplicación.\n\n";
                emailBody += "Saludos!";

                emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

                // Verificar si hay alguna app que pueda manejar el envío de correos
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(emailIntent, "Enviar correo con..."));
                } else {
                    Toast.makeText(this, "No hay aplicaciones de correo instaladas", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        } catch (Exception e) {
            Log.e("ContextMenu", "Error en menú contextual", e);
        }

        return super.onContextItemSelected(item);
    }
}