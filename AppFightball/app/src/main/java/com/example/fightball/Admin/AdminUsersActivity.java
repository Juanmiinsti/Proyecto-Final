package com.example.fightball.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Adapters.UserAdapterAdmin;
import com.example.fightball.Models.MatchModel;
import com.example.fightball.Models.UserModel;
import com.example.fightball.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUsersActivity extends AppCompatActivity {
    public static UserAdapterAdmin adapter;
    ListView userVisual;
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    SharedPreferences sp;

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
                Call<UserModel> deleteUser =retroFitBuilder.callApi().deleteUser(sp.getString("key",""),AdminMainActivity.users.get(info.position));
                deleteUser.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        AdminMainActivity.users.remove(info.position);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            }else {
                Intent intentEditU= new Intent(this, EditUserActivity.class);
                intentEditU.putExtra("pos",info.position);
                startActivity(intentEditU);
            }
        } catch (Exception e) {

        }
        return true;
    }

    private void listofUsers() {
        adapter = new UserAdapterAdmin(this, R.layout.user_admin_item, AdminMainActivity.users);
        userVisual = findViewById(R.id.adminUsersList);
        userVisual.setAdapter(adapter);
    }

}