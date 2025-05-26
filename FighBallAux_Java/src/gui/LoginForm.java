package gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gui.Models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * LoginForm represents the login GUI window.
 * It handles:
 * - User authentication via REST API.
 * - Language switching (Spanish / English).
 * - Retrieval of characters, items, and matches upon successful login.
 */
public class LoginForm extends JFrame {

    // Main login panel
    static JPanel panelLogin = new JPanel();

    // CardLayout to manage views if needed
    static CardLayout cardLayoutd = new CardLayout();
    static Container general = new Container();

    // UI components for login
    static JLabel panelLoginJlabelIdiomas = new JLabel(traductor.getString("login.idiomas"));
    static JComboBox<String> panelLoginIdiomas = new JComboBox<>();

    static JLabel panelLoginUserLabel = new JLabel(traductor.getString("login.enterUsername"));
    static JLabel panelLoginPasswordLabel = new JLabel(traductor.getString("login.enterPassword"));
    static JTextField panelLoginUsername = new JTextField();
    static JPasswordField panelLoginPassword = new JPasswordField();
    static JButton panelLoginCancelButton = new JButton(traductor.getString("login.cancel"));
    static JButton panelLoginLoginButton = new JButton(traductor.getString("login.login"));

    /**
     * Entry point of the program that launches the login form.
     */
    public static void main(String[] args) {
        new LoginForm();
    }

    /**
     * Constructor for initializing the login window UI and events.
     */
    public LoginForm() {
        initGeneral();       // Setup card layout
        configPanelLogin();  // Configure login panel actions
        createPaneLogin();   // Add components to login panel

        add(general);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(500, 300);
        setSize(700, 500);
        setVisible(true);
    }

    /**
     * Initializes the main container with CardLayout.
     */
    static void initGeneral() {
        general.setLayout(cardLayoutd);
        general.add("0", panelLogin);
    }

    /**
     * Adds login UI components to the panel.
     */
    static void createPaneLogin() {
        panelLogin.add(panelLoginJlabelIdiomas);
        panelLogin.add(panelLoginIdiomas);
        panelLogin.add(panelLoginUserLabel);
        panelLogin.add(panelLoginUsername);
        panelLogin.add(panelLoginPasswordLabel);
        panelLogin.add(panelLoginPassword);
        panelLogin.add(panelLoginCancelButton);
        panelLogin.add(panelLoginLoginButton);
    }

    /**
     * Configures button actions and language switch behavior.
     */
    void configPanelLogin() {
        panelLogin.setLayout(new GridLayout(4, 2));

        // Login button action
        panelLoginLoginButton.addActionListener(e -> {
            if (login(panelLoginUsername.getText(), panelLoginPassword.getText())) {
                dispose(); // Close login window

                // Show splash screen and then launch main app
                SplashScreen.mostrarSplashYContinuar(() -> InfoViewerApp.iniciarPrograma());
                doRequests(); // Load data
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials ❌");
            }
        });

        // Cancel button can be customized to close app or clear fields
        panelLoginCancelButton.addActionListener(e -> {
            // Example: clear inputs or exit app
            // System.exit(0);
        });

        // Language selection dropdown
        panelLoginIdiomas.addItem("Spanish");
        panelLoginIdiomas.addItem("English");

        panelLoginIdiomas.addItemListener(e -> {
            if (panelLoginIdiomas.getSelectedItem().equals("Spanish")) {
                traductor.setLocale(1);
            } else {
                traductor.setLocale(2);
            }
            updateLanguage(); // Refresh labels
        });
    }

    /**
     * Updates UI texts to reflect the selected language.
     */
    private void updateLanguage() {
        panelLoginUserLabel.setText(traductor.getString("login.enterUsername"));
        panelLoginPasswordLabel.setText(traductor.getString("login.enterPassword"));
        panelLoginCancelButton.setText(traductor.getString("login.cancel"));
        panelLoginLoginButton.setText(traductor.getString("login.login"));
        panelLoginJlabelIdiomas.setText(traductor.getString("login.idiomas"));
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Fetches all necessary data from backend after login.
     */
    private void doRequests() {
        getCharacters();
        getItems();
        getMatches();
    }

    /**
     * Fetches characters from the API and stores them in DataSource.
     */
    private void getCharacters() {
        try {
            Gson gson = new Gson();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(DataSource.url + "api/characters"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", DataSource.key)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                java.lang.reflect.Type type = new TypeToken<List<CharacterModel>>() {}.getType();
                List<CharacterModel> characters = gson.fromJson(response.body(), type);
                DataSource.characters = new ArrayList<>(characters);
            }
        } catch (Exception e) {
            System.out.println("Error in getCharacters: " + e.getMessage());
        }
    }

    /**
     * Fetches items from the API and stores them in DataSource.
     */
    private void getItems() {
        try {
            Gson gson = new Gson();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(DataSource.url + "api/items"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", DataSource.key)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                java.lang.reflect.Type type = new TypeToken<List<ItemModel>>() {}.getType();
                List<ItemModel> items = gson.fromJson(response.body(), type);
                DataSource.items = new ArrayList<>(items);
            }
        } catch (Exception e) {
            System.out.println("Error in getItems: " + e.getMessage());
        }
    }

    /**
     * Fetches match history of the current user and stores them in DataSource.
     */
    private void getMatches() {
        try {
            Gson gson = new Gson();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(DataSource.url + "api/matches/total/" + DataSource.userName))
                    .header("Content-Type", "application/json")
                    .header("Authorization", DataSource.key)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                java.lang.reflect.Type type = new TypeToken<List<MatchModel>>() {}.getType();
                List<MatchModel> matches = gson.fromJson(response.body(), type);
                DataSource.matches = new ArrayList<>(matches);
            }
        } catch (Exception e) {
            System.out.println("Error in getMatches: " + e.getMessage());
        }
    }

    /**
     * Sends login credentials to the server and stores the auth token on success.
     * @param username user input username
     * @param password user input password
     * @return true if login is successful, false otherwise
     */
    private boolean login(String username, String password) {
        try {
            LoginModel user = new LoginModel(username, password);
            Gson gson = new Gson();
            String json = gson.toJson(user);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(DataSource.url + "auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                DataSource.key = "Bearer " + response.body();
                DataSource.userName = username;
                getUser(); // Fetch user details
                return true;
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Fetches user details using username and stores the user ID.
     */
    private void getUser() {
        try {
            Gson gson = new Gson();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(DataSource.url + "api/userByName/" + DataSource.userName))
                    .header("Content-Type", "application/json")
                    .header("Authorization", DataSource.key)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                UserModel user = gson.fromJson(response.body(), UserModel.class);
                DataSource.userId = user.getId();
                System.out.println("User loaded: " + user);
            }
        } catch (Exception e) {
            System.out.println("Error in getUser: " + e.getMessage());
        }
    }
}
