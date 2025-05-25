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

/**
 * Activity to create or edit an Item in the admin panel of FightBall app.
 *
 * This activity supports two modes of operation:
 * - POST mode: creating a new item
 * - PUT mode: editing an existing item
 *
 * The mode is determined by an intent extra "type" (1 = PUT/edit, 2 = POST/create).
 *
 * The UI contains input fields for the item's name and quantity,
 * as well as radio buttons to specify the item's type (Good or Bad).
 *
 * The item type affects the description attribute: "Health" for Good, "Damage" for Bad.
 *
 * Changes are sent to the backend via Retrofit API calls.
 */
public class EditItemActivity extends AppCompatActivity {

    // UI elements
    RadioButton rbBad;           // Radio button for marking the item as a bad/damage item
    RadioButton rbGood;          // Radio button for marking the item as a good/health item
    EditText name;               // Input for the item's name
    EditText quantity;           // Input for the item's quantity

    Button btnsave;              // Save button to confirm changes
    Button btCancel;             // Cancel button to discard changes and close the activity

    // Position of the item in the list (used when editing)
    private int position;

    // Currently selected item (for editing)
    ItemModelAdmin selectedItem;

    // Retrofit instance to make API calls
    RetroFitBuilder retroFitBuilder = RetroFitBuilder.getInstance();

    // SharedPreferences to retrieve the stored API key
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display mode for modern UI experience
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_edit_item);

        // Adjust view padding according to system window insets (status bar, navigation bar, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configure UI and logic
        config();
    }

    /**
     * Initializes UI components, retrieves intent extras,
     * and sets up the activity depending on whether editing or creating a new item.
     */
    private void config() {
        sp = getSharedPreferences("FightBall", MODE_PRIVATE);

        position = getIntent().getIntExtra("pos", 0);

        name = findViewById(R.id.inputItemName);
        quantity = findViewById(R.id.inputItemQuantity);

        btnsave = findViewById(R.id.saveItemEdit);
        btCancel = findViewById(R.id.cancelItemEdit);

        rbBad = findViewById(R.id.radioButtonBadItem);
        rbGood = findViewById(R.id.radioButtonGoodItem);

        // Cancel button closes the activity without saving
        btCancel.setOnClickListener(e -> finish());

        // Get mode: 1 for editing (PUT), 2 for creating (POST)
        int aux = getIntent().getIntExtra("type", 0);
        if (aux == 2) {
            configforPost();
        } else if (aux == 1) {
            configforPut();
        } else {
            System.out.println("Unexpected type value");
        }
    }

    /**
     * Configures the UI and listeners for editing an existing item (PUT).
     * Fills the input fields with the current item data.
     */
    private void configforPut() {
        selectedItem = AdminMainActivity.items.get(position);

        name.setText(selectedItem.getName());
        quantity.setText(String.valueOf(selectedItem.getQuantity()));

        // Set radio button according to item's description
        if (Objects.equals(selectedItem.getDescription(), "Health")) {
            rbGood.toggle();
        } else {
            rbBad.toggle();
        }

        putListeners();
    }

    /**
     * Attaches listener to the save button for PUT (edit) mode.
     * Validates inputs and calls API to update the item.
     */
    private void putListeners() {
        btnsave.setOnClickListener(v -> {
            try {
                if (!name.getText().toString().isEmpty() && Integer.parseInt(quantity.getText().toString()) != 0) {
                    editCall();
                } else {
                    Toast.makeText(EditItemActivity.this, "Insert name", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(EditItemActivity.this, "Invalid number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Configures the UI and listeners for creating a new item (POST).
     * By default, selects the "Good" radio button.
     */
    private void configforPost() {
        postListeners();
        rbGood.toggle();  // Default to "Good" item
    }

    /**
     * Attaches listener to the save button for POST (create) mode.
     * Validates inputs and calls API to create the new item.
     */
    private void postListeners() {
        btnsave.setOnClickListener(v -> {
            try {
                if (!name.getText().toString().isEmpty() && Integer.parseInt(quantity.getText().toString()) != 0) {
                    postcall();
                } else {
                    Toast.makeText(EditItemActivity.this, "Insert name", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(EditItemActivity.this, "Invalid number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Makes a Retrofit POST call to create a new item in the backend.
     * On success, adds the new item to the list and refreshes the UI.
     */
    private void postcall() {
        String description;
        String nameaux = name.getText().toString();

        // Set description based on selected radio button
        if (rbBad.isChecked()) {
            description = "Damage";
        } else {
            description = "Health";
        }

        int quantityaux = Integer.parseInt(quantity.getText().toString());

        ItemModelAdmin createdItem = new ItemModelAdmin(0, nameaux, description, quantityaux);

        Call<ItemModelAdmin> createItemModel = retroFitBuilder.callApi().createItem(sp.getString("key", ""), createdItem);
        createItemModel.enqueue(new Callback<ItemModelAdmin>() {
            @Override
            public void onResponse(Call<ItemModelAdmin> call, Response<ItemModelAdmin> response) {
                System.out.println("Item created successfully");
                AdminMainActivity.items.add(response.body());
                AdminItemsActivity.adapter.notifyDataSetChanged();
                finish();
            }

            @Override
            public void onFailure(Call<ItemModelAdmin> call, Throwable t) {
                System.out.println("Failed to create item");
            }
        });
    }

    /**
     * Makes a Retrofit PUT call to update an existing item in the backend.
     * On success, updates the item in the list and refreshes the UI.
     */
    private void editCall() {
        int idaux = selectedItem.getId();
        String description;
        String nameaux = name.getText().toString();

        // Set description based on selected radio button
        if (rbBad.isChecked()) {
            description = "Damage";
        } else {
            description = "Health";
        }

        int quantityaux = Integer.parseInt(quantity.getText().toString());

        ItemModelAdmin editedItem = new ItemModelAdmin(idaux, nameaux, description, quantityaux);

        Call<ItemModelAdmin> editcall = retroFitBuilder.callApi().editItem(sp.getString("key", ""), selectedItem.getId(), editedItem);

        editcall.enqueue(new Callback<ItemModelAdmin>() {
            @Override
            public void onResponse(Call<ItemModelAdmin> call, Response<ItemModelAdmin> response) {
                System.out.println("Item edited successfully");
                AdminMainActivity.items.set(position, editedItem);
                AdminItemsActivity.adapter.notifyDataSetChanged();
                finish();
            }

            @Override
            public void onFailure(Call<ItemModelAdmin> call, Throwable t) {
                System.out.println("Failed to edit item");
            }
        });
    }
}
