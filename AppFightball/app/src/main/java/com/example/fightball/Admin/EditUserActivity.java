package com.example.fightball.Admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fightball.API.RetroFitBuilder;
import com.example.fightball.Models.RoleModel;
import com.example.fightball.Models.UserModel;
import com.example.fightball.Models.UserRolesModel;
import com.example.fightball.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity to edit the roles assigned to a user.
 * Allows toggling roles (Admin, Moderator, Player) via checkboxes.
 * Updates the roles via API call on save.
 */
public class EditUserActivity extends AppCompatActivity {

    // Position of the user in the global user list
    private int userPosition;

    // User object currently being edited
    private UserModel editedUser;

    // UI elements
    private TextView userlabel;
    private CheckBox checkBoxAdmin;
    private CheckBox checkBoxMod;
    private CheckBox checkBoxPlayer;
    private Button editarbtn;
    private Button cancelar;

    // List to keep track of roles assigned before editing to detect changes
    private ArrayList<Integer> beforeEditRoles = new ArrayList<>();

    // Retrofit instance for API calls
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    // SharedPreferences to retrieve stored user key for API authorization
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge UI for better immersive experience
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_edit_user);

        // Add window insets listener to apply system bar padding dynamically
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize and configure UI and logic
        config();
    }

    /**
     * Initializes UI elements and sets initial state.
     * Loads user data and configures checkboxes and button listeners.
     */
    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        // Initialize UI components from layout
        userlabel = findViewById(R.id.edituserLabel);
        checkBoxAdmin = findViewById(R.id.adminCheckBox);
        checkBoxMod = findViewById(R.id.modCheckBox);
        checkBoxPlayer = findViewById(R.id.playerCheckBox);

        editarbtn = findViewById(R.id.editarButton);
        cancelar = findViewById(R.id.cancelEditButton);

        // Retrieve the user's position/index from intent extras
        userPosition = getIntent().getIntExtra("pos", 0);

        // Load user object from global user list using position
        editedUser = AdminMainActivity.users.get(userPosition);

        // Set username label on screen
        userlabel.setText(editedUser.getName());

        // Initialize checkboxes based on current roles from backend
        setCheckBox();

        // Setup listeners for buttons
        setbuttonListeners();
    }

    /**
     * Retrieves current role IDs of the user from API and sets the corresponding checkboxes.
     * Also stores the original roles in beforeEditRoles for comparison on update.
     */
    private void setCheckBox() {
        Call<List<Integer>> selectActivity = retroFitBuilder.callApi().rolesIdsByname(
                AdminMainActivity.users.get(userPosition).getName(),
                sp.getString("key", "")
        );

        selectActivity.enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if (response.body() != null) {
                    // Iterate through each role ID received from API
                    for (int i = 0; i < response.body().size(); i++) {
                        int roleId = response.body().get(i);
                        // Map role IDs to checkboxes and mark checked accordingly
                        if (roleId == 1) {
                            checkBoxPlayer.setChecked(true);
                            beforeEditRoles.add(1);
                        }
                        if (roleId == 2) {
                            checkBoxMod.setChecked(true);
                            beforeEditRoles.add(2);
                        }
                        if (roleId == 3) {
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

    /**
     * Sets the onClick listeners for the Cancel and Edit buttons.
     * Cancel closes the activity.
     * Edit triggers the update of user roles.
     */
    private void setbuttonListeners() {
        // Cancel button simply closes the activity
        cancelar.setOnClickListener(e -> finish());

        // Edit button triggers user roles update via API
        editarbtn.setOnClickListener(e -> {
            try {
                editUserRoleCall();
            } catch (Exception a) {
                System.out.println("error in editUserRoleCall " + a.getMessage());
            }
        });
    }

    /**
     * Prepares the updated list of roles based on checkbox states.
     * Sends PUT request to update the user with new roles.
     */
    private void editUserRoleCall() {
        List<RoleModel> roles = new ArrayList<>();

        // Add roles if checked and were not previously assigned (to avoid duplicates)
        if (checkBoxAdmin.isChecked() && !beforeEditRoles.contains(3)) {
            roles.add(new RoleModel(3, "ADMIN"));
        }
        if (checkBoxMod.isChecked() && !beforeEditRoles.contains(2)) {
            roles.add(new RoleModel(2, "MOD"));
        }
        if (checkBoxPlayer.isChecked() && !beforeEditRoles.contains(1)) {
            roles.add(new RoleModel(1, "PLAYER"));
        }

        // Retrieve user's existing info for the update request
        int iduser = AdminMainActivity.users.get(userPosition).getId();
        String password = AdminMainActivity.users.get(userPosition).getPassword();
        String username = AdminMainActivity.users.get(userPosition).getName();

        // Create user model with updated roles
        UserRolesModel user = new UserRolesModel(iduser, username, password, roles);

        // Make API call to update user roles, passing auth key and user ID
        Call<UserRolesModel> editUsercall = retroFitBuilder.callApi().editUser(sp.getString("key", ""), iduser, user);

        editUsercall.enqueue(new Callback<UserRolesModel>() {
            @Override
            public void onResponse(Call<UserRolesModel> call, Response<UserRolesModel> response) {
                System.out.println("exito"); // Success
                finish(); // Close activity after successful update
            }

            @Override
            public void onFailure(Call<UserRolesModel> call, Throwable t) {
                System.out.println("fallo"); // Failure
            }
        });
    }
}
