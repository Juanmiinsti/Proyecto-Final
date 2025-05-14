package com.example.fightball.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Adapters.AdminItemAdapter;
import com.example.fightball.Models.ItemModelAdmin;
import com.example.fightball.Models.UserModel;
import com.example.fightball.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminItemsActivity extends AppCompatActivity {
    public static AdminItemAdapter adapter;
    ListView itemsVisual;
    Button create;
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
        registerForContextMenu(itemsVisual);

    }

    private void config() {

        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        listofItems();
        create=findViewById(R.id.crearItemButton);

        create.setOnClickListener(e->{
            Intent intentEdit =new Intent(this,EditItemActivity.class);
            intentEdit.putExtra("type",2);
            startActivity(intentEdit);

        });
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
            ItemModelAdmin selectedItem = (ItemModelAdmin) itemsVisual.getItemAtPosition(info.position);
            if (selectedItem == null) {
                Log.e("ContextMenu", "Usuario seleccionado es nulo");
                return super.onContextItemSelected(item);
            }
            if (item.getItemId()==R.id.IdEliminar){
                deleteCall(selectedItem.getId(),info.position);

            }else {
                Intent intentEdit =new Intent(this,EditItemActivity.class);
                intentEdit.putExtra("type",1);
                intentEdit.putExtra("pos",info.position);
                startActivity(intentEdit);

            }
        }catch (Exception e){

        }
        return super.onContextItemSelected(item);
    }

    private void deleteCall(int id,int pos) {
        Call<Boolean>deleteItem=retroFitBuilder.callApi().deleteItem(sp.getString("key",""),id);
        deleteItem.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code()==200){
                    AdminMainActivity.items.remove(pos);
                    adapter.notifyDataSetChanged();
                    System.out.println("cambios notificados");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("error al intentar Eliminar");

            }
        });

    }

    private void listofItems(){
        adapter=new AdminItemAdapter(this,R.layout.admin_item_layout, AdminMainActivity.items);
        itemsVisual =findViewById(R.id.adminItemsLists);
        itemsVisual.setAdapter(adapter);
    }
}