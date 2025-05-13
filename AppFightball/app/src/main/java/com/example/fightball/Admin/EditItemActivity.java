package com.example.fightball.Admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Models.ItemModelAdmin;
import com.example.fightball.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Objects;

public class EditItemActivity extends AppCompatActivity {

    RadioButton rbBad;
    RadioButton rbGood;
    EditText name;
    EditText quantity;


    Button btnsave;

    Button btCancel;
    private int position;
    ItemModelAdmin selectedItem;

    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }

    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        position=getIntent().getIntExtra("pos",0);

        name=findViewById(R.id.inputItemName);
        quantity=findViewById(R.id.inputItemQuantity);

        btnsave=findViewById(R.id.saveItemEdit);
        btCancel=findViewById(R.id.cancelItemEdit);

        rbBad=findViewById(R.id.radioButtonBadItem);
        rbGood=findViewById(R.id.radioButtonGoodItem);

        btCancel.setOnClickListener(e ->{
            finish();
        });

       int aux= getIntent().getIntExtra("type",0);
       if (aux ==2){
       configforPost();
       } else if (aux==1) {
        configforPut();
       }else {
           System.out.println("ocurrio algo inesperado");

       }
    }

    private void configforPut() {
        selectedItem=AdminMainActivity.items.get(position);

        name.setText(selectedItem.getName());
        quantity.setText(String.valueOf(selectedItem.getQuantity()));
        if (Objects.equals(selectedItem.getDescription(), "Health")){
            rbGood.toggle();
        }else {
            rbBad.toggle();
        }
        putListeners();

    }

    private void putListeners() {
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!name.getText().toString().isEmpty() &&Integer.parseInt(quantity.getText().toString())!=0)
                        editCall();
                    else {
                        Toast.makeText(EditItemActivity.this,"insert name",Toast.LENGTH_SHORT).show();

                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(EditItemActivity.this,"invalid number",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void configforPost() {
        postListeners();
        rbGood.toggle();
}

    private void postListeners() {
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                if (!name.getText().toString().isEmpty() &&Integer.parseInt(quantity.getText().toString())!=0)
                    postcall();
                else {
                    Toast.makeText(EditItemActivity.this,"insert name",Toast.LENGTH_SHORT).show();

                }
            } catch (NumberFormatException e) {
                    Toast.makeText(EditItemActivity.this,"invalid number",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void postcall() {
        String description;
        String nameaux=name.getText().toString();
        if (rbBad.isChecked()){
            description="Damage";
        }else{
            description="Health";
        }
        int quantityaux=Integer.parseInt(quantity.getText().toString());
        ItemModelAdmin createdItem =new ItemModelAdmin(0,nameaux,description,quantityaux);

        Call<ItemModelAdmin>createItemModel=retroFitBuilder.callApi().createItem(sp.getString("key",""), createdItem);
        createItemModel.enqueue(new Callback<ItemModelAdmin>() {
            @Override
            public void onResponse(Call<ItemModelAdmin> call, Response<ItemModelAdmin> response) {
                System.out.println("se creo exitosamente ");
                AdminMainActivity.items.add(response.body());
                AdminItemsActivity.adapter.notifyDataSetChanged();
                finish();

            }

            @Override
            public void onFailure(Call<ItemModelAdmin> call, Throwable t) {

            }
        });
    }

    private void editCall(){
        int idaux=selectedItem.getId();
        String description;
        String nameaux=name.getText().toString();
        if (rbBad.isChecked()){
            description="Damage";
        }else{
            description="Health";
        }
        int quantityaux=Integer.parseInt(quantity.getText().toString());
        ItemModelAdmin editedItem=new ItemModelAdmin(idaux,nameaux,description,quantityaux);
        Call<ItemModelAdmin> editcall=retroFitBuilder.callApi().editItem(sp.getString("key",""),selectedItem.getId(),editedItem);

        editcall.enqueue(new Callback<ItemModelAdmin>() {
            @Override
            public void onResponse(Call<ItemModelAdmin> call, Response<ItemModelAdmin> response) {
                System.out.println("usuario editado");
                AdminMainActivity.items.set(position,editedItem);
                AdminItemsActivity.adapter.notifyDataSetChanged();
                finish();
            }
            @Override
            public void onFailure(Call<ItemModelAdmin> call, Throwable t) {
                System.out.println("usuario no editado");

            }
        });

    }


}