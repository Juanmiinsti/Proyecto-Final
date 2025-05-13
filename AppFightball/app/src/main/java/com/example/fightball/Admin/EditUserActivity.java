package com.example.fightball.Admin;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.LoginActivity;
import com.example.fightball.Models.RoleModel;
import com.example.fightball.Models.UserModel;
import com.example.fightball.Models.UserRolesModel;
import com.example.fightball.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {
    private int userPosition;
    private UserModel editedUser;
    private TextView userlabel;
    private CheckBox checkBoxAdmin;
    private CheckBox checkBoxMod;

    private CheckBox checkBoxPlayer;


    private ArrayList<Integer>beforeEditRoles=new ArrayList<>();

    private Button editarbtn;
    private Button cancelar;

    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        config();
    }

    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);


        userlabel = findViewById(R.id.edituserLabel);
        checkBoxAdmin = findViewById(R.id.adminCheckBox);
        checkBoxMod = findViewById(R.id.modCheckBox);
        checkBoxPlayer = findViewById(R.id.playerCheckBox);

        editarbtn = findViewById(R.id.editarButton);
        cancelar = findViewById(R.id.cancelEditButton);

        userPosition = getIntent().getIntExtra("pos", 0);
        editedUser = AdminMainActivity.users.get(userPosition);
        userlabel.setText(editedUser.getName());

        setCheckBox();

        setbuttonListeners();


    }

    private void setCheckBox() {

        Call<List<Integer>> selectActivity = retroFitBuilder.callApi().rolesIdsByname(
                AdminMainActivity.users.get(userPosition).getName(),
                sp.getString("key", "")
        );

        selectActivity.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        if (response.body().get(i) == 1) {
                            checkBoxPlayer.setChecked(true);
                            beforeEditRoles.add(1);

                        }
                        if (response.body().get(i) == 2) {
                            checkBoxMod.setChecked(true);
                            beforeEditRoles.add(2);

                        }
                        if (response.body().get(i) == 3) {
                            checkBoxAdmin.setChecked(true);
                            beforeEditRoles.add(3);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                System.out.println("error getting roles");
            }
        });

    }

    private void setbuttonListeners() {
        cancelar.setOnClickListener(e -> {
            finish();
        });

        editarbtn.setOnClickListener(e -> {
            try {
                editUserRoleCall();
            } catch (Exception a) {
                System.out.println("error in edituserRoleCall " + a.getMessage());
            }
        });
    }

    private void editUserRoleCall() {
        List<RoleModel> roles = new ArrayList<>();
        if (checkBoxAdmin.isChecked() & !beforeEditRoles.contains(1)) {
            roles.add(new RoleModel(3, "ADMIN"));
        }
        if (checkBoxPlayer.isChecked() & !beforeEditRoles.contains(2)) {
            roles.add(new RoleModel(2, "MOD"));
        }
        if (checkBoxAdmin.isChecked() & !beforeEditRoles.contains(3)) {
            roles.add(new RoleModel(1, "PLAYER"));
        }

        int iduser = AdminMainActivity.users.get(userPosition).getId();
        String password = AdminMainActivity.users.get(userPosition).getPassword();
        String username = AdminMainActivity.users.get(userPosition).getName();

        UserRolesModel user = new UserRolesModel(iduser, username, password, roles);


        Call<UserRolesModel> editUsercall = retroFitBuilder.callApi().editUser(sp.getString("key", ""), iduser, user);

        editUsercall.enqueue(new Callback<UserRolesModel>() {
            @Override
            public void onResponse(Call<UserRolesModel> call, Response<UserRolesModel> response) {
                System.out.println("exito");
                finish();
            }

            @Override
            public void onFailure(Call<UserRolesModel> call, Throwable t) {
                System.out.println("fallo");
            }
        });


    }
}