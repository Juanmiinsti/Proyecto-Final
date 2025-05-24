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

import com.example.fightball.Adapters.UserAdapter;
import com.example.fightball.Models.UserModel;
import com.example.fightball.R;

/**
 * Activity that allows moderators to view a list of users and interact with them via a contextual menu.
 * Moderators can search for a user online or send them an email directly from the app.
 */
public class ModUsersActivity extends AppCompatActivity {

    // Static adapter to display user information in a ListView
    public static UserAdapter adapter;

    // ListView to display the list of users
    ListView userVisual;

    /**
     * Called when the activity is created. Initializes layout and logic.
     * @param savedInstanceState Bundle with saved instance state if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mod_users);

        // Set padding to avoid system UI overlays (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configure the user list and context menu
        config();
    }

    /**
     * Initializes the ListView with users and registers it for a context menu.
     */
    private void config() {
        listofUsers();
        registerForContextMenu(userVisual);
    }

    /**
     * Loads the list of users into the ListView using the custom adapter.
     * The list of users is obtained from ModMainActivity.
     */
    private void listofUsers() {
        adapter = new UserAdapter(this, R.layout.usermod_item, ModMainActivity.users);
        userVisual = findViewById(R.id.usersModList);
        userVisual.setAdapter(adapter);
    }

    /**
     * Inflates the context menu when a user item is long-pressed.
     * @param menu The context menu to build.
     * @param v The view for which the context menu is being built.
     * @param menuInfo Extra information about the item for which the context menu should be shown.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contextual, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * Handles item selection in the context menu.
     * Supports user name search and email sending.
     * @param item The selected menu item.
     * @return true if the item is handled, otherwise the super method is called.
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) {
            return super.onContextItemSelected(item);
        }

        try {
            UserModel selectedUser = (UserModel) userVisual.getItemAtPosition(info.position);
            if (selectedUser == null) {
                Log.e("ContextMenu", "Selected user is null");
                return super.onContextItemSelected(item);
            }

            int id = item.getItemId();

            // Handle web search option
            if (id == R.id.IdBuscar) {
                Log.d("ContextMenu", "Starting search for: " + selectedUser.getName());
                String query = selectedUser.getName().trim();

                if (!query.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, query);

                    PackageManager pm = getPackageManager();
                    if (intent.resolveActivity(pm) != null) {
                        startActivity(intent);
                        System.out.println("Opened using web search");
                    } else {
                        // Fallback: open browser with search URL
                        System.out.println("Fallback to manual browser search");
                        String searchUrl = "https://www.google.com/search?q=" + Uri.encode(query);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl));
                        startActivity(browserIntent);
                    }
                } else {
                    Toast.makeText(this, "Username is empty", Toast.LENGTH_SHORT).show();
                }

                // Handle email sending option
            } else if (id == R.id.idEnviarCorreo) {
                Log.d("ContextMenu", "Preparing email to: " + selectedUser.getName());

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822"); // MIME type for email

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact from the app");

                String emailBody = "Hello " + selectedUser.getName() + ",\n\n";
                emailBody += "This is a message sent from my app.\n\n";
                emailBody += "Regards!";
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(emailIntent, "Send email using..."));
                } else {
                    Toast.makeText(this, "No email apps are installed", Toast.LENGTH_SHORT).show();
                }

                return true;
            }

        } catch (Exception e) {
            Log.e("ContextMenu", "Error handling context menu", e);
        }

        return super.onContextItemSelected(item);
    }
}
