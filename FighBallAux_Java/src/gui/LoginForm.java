package gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gui.Models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class LoginForm extends JFrame {

    static JPanel panelLogin=new JPanel();

    static CardLayout cardLayoutd = new CardLayout();
    static Container general = new Container();


    static JLabel panelLoginJlabelIdiomas = new JLabel(traductor.getString("login.idiomas"));
    static JComboBox<String> panelLoginIdiomas = new JComboBox<>();

    static JLabel panelLoginUserLabel = new JLabel(traductor.getString("login.enterUsername"));
    static JLabel panelLoginPasswordLabel = new JLabel(traductor.getString("login.enterPassword"));
    static JTextField panelLoginUsername = new JTextField();
    static JPasswordField panelLoginPassword = new JPasswordField();
    static JButton panelLoginCancelButton = new JButton(traductor.getString("login.cancel"));
    static JButton panelLoginLoginButton = new JButton(traductor.getString("login.login"));


    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
    }

    public LoginForm() {

        initGeneral();
        configPanelLogin();
        createPaneLogin();

        add(general);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.red);
        setLocation(500, 300);
        setSize(700, 500);
        setVisible(true);
    }
    static void initGeneral() {
        general.setLayout(cardLayoutd);
        general.add("0",panelLogin);
    }

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

    void configPanelLogin(){
        panelLogin.setLayout(new GridLayout(4,2));
        panelLoginLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (login(panelLoginUsername.getText(),panelLoginPassword.getText())) {

                    dispose();
                    //pantalla de carga
                    SplashScreen.mostrarSplashYContinuar(() -> {
                        InfoViewerApp.iniciarPrograma();
                    });
                    doRequests();
                }else {
                    JOptionPane.showMessageDialog(null,"Datos Erroneos");
                }
            }
        });

        panelLoginCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //System.exit(0);

            }
        });



        panelLoginIdiomas.addItem("Spanish");
        panelLoginIdiomas.addItem("English");

        panelLoginIdiomas.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (panelLoginIdiomas.getSelectedItem()=="Spanish"){
                    traductor.setLocale(1);
                }else {
                    traductor.setLocale(2);
                }
                updateLanguage();

            }
        });

    }


    private void updateLanguage() {
        panelLoginUserLabel.setText(traductor.getString("login.enterUsername"));
        panelLoginPasswordLabel.setText(traductor.getString("login.enterPassword"));
        panelLoginCancelButton.setText(traductor.getString("login.cancel"));
        panelLoginLoginButton.setText(traductor.getString("login.login"));
        panelLoginJlabelIdiomas.setText(traductor.getString("login.idiomas"));
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void doRequests(){
    getCharacters();
    getItems();
    getMatches();
    }

    private void getCharacters(){
        try {
            Gson gson = new Gson();

            HttpRequest postLogin = HttpRequest.newBuilder()
                    .uri(new URI(DataSource.url + "api/characters"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", DataSource.key)
                    .GET()
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(postLogin, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                java.lang.reflect.Type characterListType = new TypeToken<List<CharacterModel>>() {}.getType();
                List<CharacterModel> characters = gson.fromJson(response.body(), characterListType);
                DataSource.characters = new ArrayList<>(characters);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void getItems() {
        try {
            Gson gson = new Gson();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(DataSource.url + "api/items"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", DataSource.key)
                    .GET()
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                java.lang.reflect.Type itemListType = new TypeToken<List<ItemModel>>() {}.getType();
                List<ItemModel> items = gson.fromJson(response.body(), itemListType);
                DataSource.items = new ArrayList<>(items);
            }
        } catch (Exception e) {
            System.out.println("Error en getItems: " + e.getMessage());
        }
    }
    private void getMatches() {
        try {
            Gson gson = new Gson();
            String userId = String.valueOf(DataSource.userId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(DataSource.url + "api/matches/total/" + DataSource.userName))
                    .header("Content-Type", "application/json")
                    .header("Authorization", DataSource.key)
                    .GET()
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                java.lang.reflect.Type matchListType = new TypeToken<List<MatchModel>>() {}.getType();
                List<MatchModel> matches = gson.fromJson(response.body(), matchListType);
                DataSource.matches = new ArrayList<>(matches);
            }
        } catch (Exception e) {
            System.out.println("Error en getMatches: " + e.getMessage());
        }
    }


    private boolean login(String username, String password) {
        try {
        LoginModel user =new LoginModel(username,password);
        Gson gson=new Gson();
        String json=gson.toJson(user);

        HttpRequest postLogin= HttpRequest.newBuilder()
                .uri(new URI(DataSource.url+"auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();

        HttpClient httpClient=HttpClient.newHttpClient();
        System.out.println( HttpRequest.BodyPublishers.ofString(json));

            HttpResponse<String> response= httpClient.send(postLogin, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()==200){
                DataSource.key= "Bearer "+response.body();
                DataSource.userName=panelLoginUsername.getText();
                getUser();
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void getUser(){
        try {
            UserModel user=new UserModel(0,"","");
            Gson gson=new Gson();
            String json=gson.toJson(user);

            HttpRequest getUser = HttpRequest.newBuilder()
                    .uri(new URI(DataSource.url+"api/userByName/"+DataSource.userName))
                    .header("Content-Type", "application/json")
                    .header("Authorization", DataSource.key)
                    .GET()
                    .build();


            HttpClient httpClient=HttpClient.newHttpClient();
            System.out.println( HttpRequest.BodyPublishers.ofString(json));

            HttpResponse<String> response= httpClient.send(getUser, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()==200){
                user=gson.fromJson(response.body(), UserModel.class);

                System.out.println(user.toString());
                DataSource.userId=user.getId();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }




}


