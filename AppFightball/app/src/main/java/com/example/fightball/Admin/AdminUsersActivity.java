package com.example.fightball.Admin;



import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Adapters.UserAdapterAdmin;
import com.example.fightball.Models.UserModel;
import com.example.fightball.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUsersActivity extends AppCompatActivity {
    public static UserAdapterAdmin adapter;
    ListView userVisual;
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    SharedPreferences sp;
    static String canalId="777";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }
    private void config() {
        listofUsers();
        createNotificationChannel();
        registerForContextMenu(userVisual);
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

    }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contextual_admin, menu);
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

            if (id == R.id.IdEliminar){
                System.out.println("eliminando");
                Call<Boolean> deleteUser =retroFitBuilder.callApi().deleteUser(sp.getString("key",""),selectedUser.getId());
                deleteUser.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.code()==200){
                            AdminMainActivity.users.remove(info.position);
                            adapter.notifyDataSetChanged();
                            System.out.println("cambios notificados");

                            userDeletedNotification(selectedUser);

                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        System.out.println(t.getMessage());

                    }
                });
            }else {
                Intent intentEditU= new Intent(this, EditUserActivity.class);
                intentEditU.putExtra("pos",info.position);
                startActivity(intentEditU);
            }
        } catch (Exception e) {
            System.out.println("error general " +e.getMessage());
        }
        return true;
    }

    private void listofUsers() {
        adapter = new UserAdapterAdmin(this, R.layout.user_admin_item, AdminMainActivity.users);
        userVisual = findViewById(R.id.adminUsersList);
        userVisual.setAdapter(adapter);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "nombre";
            String description = "descripcion";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(canalId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void userDeletedNotification(UserModel selectedUser){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(AdminUsersActivity.this,canalId);
        builder.setContentTitle("Usuario Eliminado");
        builder.setContentText(selectedUser.getName());
        builder.setSmallIcon(android.R.drawable.alert_dark_frame);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // A partir de dispositivos que usen API 26 o superior (Android Oreo)
            NotificationChannel canal = new NotificationChannel(canalId, "Canal", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(canal); // Crear/Añadir mi canal en el sistema de notificaciones
        }


        Notification notificacion =  builder.build();
        notificationManager.notify(Integer.parseInt((canalId)),notificacion);
    }



}