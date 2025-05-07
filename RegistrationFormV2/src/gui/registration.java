package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class registration extends JFrame {

    public static void iniciarPrograma() {
        registration a = new registration();
    }

    static CardLayout cardLayoutd = new CardLayout();
    static Container general = new Container();

    static ImageIcon img2 = new ImageIcon("src/img/form.png");
    static ImageIcon img = new ImageIcon("src/img/icon.png");

    static File spainlist = new File("files/spain.txt");
    static File usalist = new File("files/usa.txt");
    public static File fileResult;
    public static String results;
    public static JTextArea JtextFinalResults = new JTextArea("");
    public static JTextArea confirmResults = new JTextArea();

    static JPanel panelLogin=new JPanel();

    static JPanel panel1 = new JPanel();
    static JPanel panel2 = new JPanel();
    static JPanel panel3 = new JPanel();
    static JPanel panel4 = new JPanel();
    static JPanel panel5 = new JPanel();

    static JLabel panelLoginJlabelIdiomas = new JLabel(traductor.getString("login.idiomas"));
    static JComboBox<String> panelLoginIdiomas = new JComboBox<>();

    static JLabel panelLoginUserLabel = new JLabel(traductor.getString("login.enterUsername"));
    static JLabel panelLoginPasswordLabel = new JLabel(traductor.getString("login.enterPassword"));
    static JTextField panelLoginUsername = new JTextField();
    static JPasswordField panelLoginPassword = new JPasswordField();
    static JButton panelLoginCancelButton = new JButton(traductor.getString("login.cancel"));
    static JButton panelLoginLoginButton = new JButton(traductor.getString("login.login"));

    static JLabel panelLabel1img = new JLabel();
    static JPanel panel1aux = new JPanel();
    static JButton panel1NextButton = new JButton(traductor.getString("panel1.next"));
    static JTextArea panel1text = new JTextArea(traductor.getString("panel1.welcomeText"));

    static JLabel panel2nameLabel = new JLabel(traductor.getString("panel2.name"), SwingConstants.CENTER);
    static JLabel panel2emailLabel = new JLabel(traductor.getString("panel2.email"), SwingConstants.CENTER);
    static JLabel panel2passwordLabel = new JLabel(traductor.getString("panel2.password"), SwingConstants.CENTER);
    static JButton panel2backbuttom = new JButton(traductor.getString("panel2.goBack"));
    static JButton panel2next = new JButton(traductor.getString("panel2.next"));
    static JTextField panel2nameField = new JTextField();
    static JTextField panel2emailField = new JTextField();
    static JPasswordField panel2passwordField = new JPasswordField();

    static CardLayout panel3StatesProvincesCardLayout = new CardLayout();
    static JPanel panel3panelaux = new JPanel();
    static JButton panel3nextbutton = new JButton(traductor.getString("panel3.next"));
    static JButton panel3Backbutton = new JButton(traductor.getString("panel3.goBack"));
    static Container panel3StatesAndProvinces = new Container();
    static JComboBox<Pais> panel3CountrySelector = new JComboBox<>();
    static JComboBox<String> panel3spainProvinces = new JComboBox<>();
    static JComboBox<String> panel3usaStates = new JComboBox<>();

    static JPanel pan4panelaux = new JPanel();
    static JButton panel4nextButton = new JButton(traductor.getString("panel4.next"));
    static JButton panel4openJchooser = new JButton(traductor.getString("panel4.selectPath"));
    static JLabel panel4seeResults = new JLabel(traductor.getString("panel4.yourInfo"), SwingConstants.CENTER);
    static JLabel panel4check = new JLabel(traductor.getString("panel4.exportFile"), SwingConstants.CENTER);
    static JButton panel4GobackButton = new JButton(traductor.getString("panel4.goBack"));
    static JCheckBox panel4fileCheck = new JCheckBox();
    static JFileChooser panel4choosePath = new JFileChooser();

    static JButton panel5finishButton = new JButton(traductor.getString("panel5.finish"));



    public registration() {
        initGeneral();

        configPanelLogin();
        configPanel1();
        configPanel2();
        configPanel3();
        configPanel4();
        configPanel5();


        enterFunction();
        atajoDeTecladoParaExaminarRuta();

        createPaneLogin();
        createPanel1();
        createPanel2();
        createPanel3();
        createPanel4();
        createPanel5();



        add(general);
        setIconImage(img.getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.red);
        setLocation(500, 300);
        setSize(700, 500);
        setVisible(true);

    }

    static void initGeneral() {
        general.setLayout(cardLayoutd);
        general.add("0",panelLogin);
        general.add("1", panel1);
        general.add("2", panel2);
        general.add("3", panel3);
        general.add("4", panel4);
        general.add("5", panel5);
    }
    void configPanelLogin(){
        panelLogin.setLayout(new GridLayout(4,2));
        panelLoginLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            if (Objects.equals(panelLoginPassword.getText(), "admin") & Objects.equals(panelLoginUsername.getText(), "admin")){

                cardLayoutd.next(general);
            }else {
                JOptionPane.showMessageDialog(null,"Datos Erroneos");
            }
            }
        });

        panelLoginCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
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

    static void configPanel1() {
        panelLabel1img.setIcon(new ImageIcon(img2.getImage().getScaledInstance(350, 200, Image.SCALE_SMOOTH)));
        panel1.setBackground(Color.lightGray);
        panel1.setLayout(new GridLayout(2, 1));
        panel1aux.setLayout(new GridLayout(1, 2));

        panel1text.setBackground(Color.lightGray);
        panel1text.setEditable(false);
        panel1text.setFont(panel1text.getFont().deriveFont(24f));

        panel1NextButton.setFont(panel1NextButton.getFont().deriveFont(30f));
        panel1NextButton.addActionListener(e -> {
            cardLayoutd.next(general);
            results = "";
        });
    }

    static void configPanel2() {
        panel2.setBackground(Color.lightGray);
        panel2nameLabel.setFont(panel2nameLabel.getFont().deriveFont(25f));
        panel2emailLabel.setFont(panel2emailLabel.getFont().deriveFont(25f));
        panel2emailField.setToolTipText("You must fill in the field with a valid email (example1@ej.es");
        panel2passwordField.setToolTipText("The password must have between 8 and 16 characters and include\n" +
                "at least one digit, one uppercase letter, one lowercase letter, and one special character.");
        panel2passwordLabel.setFont(panel2passwordLabel.getFont().deriveFont(25f));
        panel2backbuttom.setFont(panel2backbuttom.getFont().deriveFont(20f));
        panel2next.setFont(panel2next.getFont().deriveFont(20f));

        panel2nameField.setFont(panel2nameField.getFont().deriveFont(20f));
        panel2emailField.setFont(panel2emailField.getFont().deriveFont(20f));
        panel2passwordField.setFont(panel2passwordLabel.getFont().deriveFont(20f));

        panel2backbuttom.addActionListener(e -> cardLayoutd.previous(general));

        panel2.setLayout(new GridLayout(4, 2));

        panel2next.addActionListener(e -> {
            nextPanel2();
        });
    }



    static void configPanel3() {
        panel3CountrySelector.addItem(new Pais("Spain", "spain"));
        panel3CountrySelector.addItem(new Pais("Usa", "usa"));
        panel3CountrySelector.setRenderer(new ComboBoxRenderer());
        panel3CountrySelector.setSelectedIndex(0);

        panel3StatesAndProvinces.setLayout(panel3StatesProvincesCardLayout);
        panel3.setBackground(Color.lightGray);
        panel3.setLayout(new GridLayout(2, 2));
        panel3panelaux.setBackground(Color.lightGray);
        panel3panelaux.setLayout(new GridLayout(1, 1));
        panel3panelaux.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel3nextbutton.setFont(panel3nextbutton.getFont().deriveFont(25f));
        panel3Backbutton.setFont(panel3Backbutton.getFont().deriveFont(25f));

        panel3CountrySelector.addActionListener(e -> {
            Pais aux = (Pais) panel3CountrySelector.getSelectedItem();
            if (aux.getNombre().equalsIgnoreCase("spain")) {
                panel3StatesProvincesCardLayout.first(panel3StatesAndProvinces);
            } else {
                panel3StatesProvincesCardLayout.last(panel3StatesAndProvinces);
            }

        });

        panel3Backbutton.addActionListener(e -> cardLayoutd.previous(general));

        panel3nextbutton.addActionListener(e -> {
            nextPanel3();


            ;
        });
        panel3spainProvinces.setFont(panel3spainProvinces.getFont().deriveFont(20f));
        panel3usaStates.setFont(panel3usaStates.getFont().deriveFont(20f));

        try (Scanner sc = new Scanner(spainlist);
             Scanner sc2 = new Scanner(usalist)) {
            while (sc.hasNext()) {
                panel3spainProvinces.addItem(sc.nextLine());
            }
            while (sc2.hasNext()) {
                panel3usaStates.addItem(sc2.nextLine());
            }
        } catch (FileNotFoundException a) {
            System.out.println("a");
        }

    }

    static void configPanel4() {

        panel4openJchooser.addActionListener(e -> {
            panel4choosePath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int seleccion = panel4choosePath.showSaveDialog(null);
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                panel4openJchooser.setText(panel4choosePath.getSelectedFile().getPath());
            } else {
                panel4openJchooser.setText("no se selecciono nada");
            }
        });
        panel4GobackButton.addActionListener(e -> {
            if (results.contains("Your Country")) {
                results = results.substring(0, results.indexOf("Your Country"));
            }
            cardLayoutd.previous(general);

        });

        panel4nextButton.addActionListener(e -> {
            nextPanel4();
        });
    }

    static void configPanel5() {
        confirmResults.setFont(confirmResults.getFont().deriveFont(20f));
        panel5.setLayout(new GridLayout(2, 1));
        panel5finishButton.addActionListener(e -> System.exit(0));

    }

    //------------------------------------------------------------------------//

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
    //------------------------------------------------------------------------//
    static void createPanel1() {
        panel1aux.add(panel1text);
        panel1aux.add(panelLabel1img);

        panel1.add(panel1aux);
        panel1.add(panel1NextButton);
    }

    //------------------------------------------------------------------------//
    static void createPanel2() {
        panel2.add(panel2nameLabel);
        panel2.add(panel2nameField);

        panel2.add(panel2emailLabel);
        panel2.add(panel2emailField);

        panel2.add(panel2passwordLabel);
        panel2.add(panel2passwordField);

        panel2.add(panel2backbuttom);
        panel2.add(panel2next);
    }


    //------------------------------------------------------------------------//

    static void createPanel3() {
        panel3panelaux.add(panel3CountrySelector);
        panel3StatesAndProvinces.add(panel3spainProvinces);
        panel3StatesAndProvinces.add(panel3usaStates);

        panel3.add(panel3panelaux);
        panel3.add(panel3StatesAndProvinces);
        panel3.add(panel3Backbutton);
        panel3.add(panel3nextbutton);

    }

    //------------------------------------------------------------------------//
    static void createPanel4() {
        panel4.setLayout(new GridLayout(3, 2));
        pan4panelaux.setLayout(new GridLayout(1, 2));


        pan4panelaux.add(panel4fileCheck);
        pan4panelaux.add(panel4openJchooser);

        panel4.add(panel4seeResults);
        panel4.add(JtextFinalResults);
        panel4.add(panel4check);
        panel4.add(pan4panelaux);
        panel4.add(panel4GobackButton);
        panel4.add(panel4nextButton);
    }
    //------------------------------------------------------------------------//

    static void createPanel5() {
        panel5.add(confirmResults);
        panel5.add(panel5finishButton);
    }


    static void atajoDeTecladoParaExaminarRuta() {
        Action aux = new AbstractAction("Explorar") {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel4choosePath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int seleccion = panel4choosePath.showSaveDialog(null);
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    panel4openJchooser.setText(panel4choosePath.getSelectedFile().getPath());
                } else {
                    panel4openJchooser.setText("no se selecciono nada");
                }

            }
        };
        String key = "Explorar";

        panel4openJchooser.setAction(aux);

        panel4openJchooser.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK), key);
        panel4openJchooser.getActionMap().put(key, aux);



    }

    static void enterFunction() {

        Action aux = new AbstractAction("Siguiente") {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayoutd.next(general);
            }
        };
        Action aux2 = new AbstractAction("Siguiente") {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextPanel2();
            }
        };
        Action aux3 = new AbstractAction("Siguiente") {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextPanel3();
            }
        };
        Action aux4 = new AbstractAction("Siguiente") {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextPanel4();
            }
        };

        Action aux5 = new AbstractAction("Siguiente") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        Action aux6 = new AbstractAction("Siguiente") {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (Objects.equals(panelLoginPassword.getText(), "admin") & Objects.equals(panelLoginUsername.getText(), "admin")){
                    cardLayoutd.next(general);
                }else {
                    JOptionPane.showMessageDialog(null,"Datos Erroneos");
                }

            }
        };

        panel1NextButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Siguiente");
        panel1NextButton.getActionMap().put("Siguiente", aux);


        panel2next.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Siguiente");
        panel2next.getActionMap().put("Siguiente", aux2);


        panel3nextbutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Siguiente");
        panel3nextbutton.getActionMap().put("Siguiente", aux3);

        panel4nextButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Siguiente");
        panel4nextButton.getActionMap().put("Siguiente", aux4);

        panel5finishButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Siguiente");
        panel5finishButton.getActionMap().put("Siguiente", aux5);

        panelLoginLoginButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Siguiente");
        panelLoginLoginButton.getActionMap().put("Siguiente", aux6);


    }

    private static void nextPanel4() {
        if (panel4fileCheck.isSelected()) {
            if (panel4choosePath.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(null, "There is no path selected");
            } else {
                JOptionPane.showMessageDialog(null, "Archivo guardado");
                fileResult = new File(panel4choosePath.getSelectedFile().getPath() + "/user-data.txt");
                confirmResults.setText("You can check your file on: \n" + fileResult.getPath());
                createFile(results);
                cardLayoutd.next(general);

            }

        } else {
            confirmResults.setText("You have not created any file");
            cardLayoutd.next(general);
        }
    }

    private static void nextPanel3() {
        cardLayoutd.next(general);
        Pais aux = (Pais) panel3CountrySelector.getSelectedItem();
        results += "Your Country is: ";
        if (aux.getNombre().equalsIgnoreCase("spain")) {
            JtextFinalResults.setText(results);
            results += aux.getNombre() + " " + panel3spainProvinces.getSelectedItem();
            JtextFinalResults.setText(results);

        } else {
            results += aux.getNombre() + " " + panel3usaStates.getSelectedItem();
            JtextFinalResults.setText(results);
        }
    }

    private static void nextPanel2() {
        if (validPassword(panel2passwordField.getText()) && validEmail(panel2emailField.getText())) {

            results = "Your Name is : " + panel2nameField.getText() + "\n" +
                    "Your Email is : " + panel2emailField.getText() + "\n" +
                    "Your Password is : " + panel2passwordField.getText() + "\n";
            JtextFinalResults.setText(results);
            cardLayoutd.next(general);

        } else {
            if (!validPassword(panel2passwordField.getText())) {
                JOptionPane.showMessageDialog(null, "Contraseña invalida");
            }
            if (!validEmail(panel2emailField.getText())) {
                JOptionPane.showMessageDialog(null, "Email invalido");

            }
        }


    }

    //------------------------------------------------------------------------//

    static void createFile(String aux) {
        try (FileWriter fl = new FileWriter(fileResult)) {
            fileResult.createNewFile();
            fl.write(aux);
        } catch (FileNotFoundException a) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void updateLanguage() {
        panelLoginUserLabel.setText(traductor.getString("login.enterUsername"));
        panelLoginPasswordLabel.setText(traductor.getString("login.enterPassword"));
        panelLoginCancelButton.setText(traductor.getString("login.cancel"));
        panelLoginLoginButton.setText(traductor.getString("login.login"));
        panelLoginJlabelIdiomas.setText(traductor.getString("login.idiomas"));

        panel1NextButton.setText(traductor.getString("panel1.next"));
        panel1text.setText(traductor.getString("panel1.welcomeText"));

        panel2nameLabel.setText(traductor.getString("panel2.name"));
        panel2emailLabel.setText(traductor.getString("panel2.email"));
        panel2passwordLabel.setText(traductor.getString("panel2.password"));
        panel2backbuttom.setText(traductor.getString("panel2.goBack"));
        panel2next.setText(traductor.getString("panel2.next"));

        panel3nextbutton.setText(traductor.getString("panel3.next"));
        panel3Backbutton.setText(traductor.getString("panel3.goBack"));

        panel4nextButton.setText(traductor.getString("panel4.next"));
        panel4openJchooser.setText(traductor.getString("panel4.selectPath"));
        panel4seeResults.setText(traductor.getString("panel4.yourInfo"));
        panel4check.setText(traductor.getString("panel4.exportFile"));
        panel4GobackButton.setText(traductor.getString("panel4.goBack"));

        panel5finishButton.setText(traductor.getString("panel5.finish"));

        SwingUtilities.updateComponentTreeUI(this);
    }


    //-----------------------------------------------------------------------------------------------------//

    static boolean succefulLogin(){
        return false;
    }

    static boolean validEmail(String aux) {
        if (!aux.endsWith("@email.com") && !aux.endsWith("@hotmail.com") && !aux.endsWith("@gmail.com")) {
            return false;
        } else {
            return true;
        }
    }

    static boolean validPassword(String aux) {
        if (aux.length() < 8 | aux.length() > 16) {
            return false;
        } else if (!uppercases(aux)) {
            return false;
        } else if (!lowercases(aux)) {
            return false;
        } else if (!digits(aux)) {
            return false;
        } else if (alfanumeric(aux)) {
            return false;
        }
        return true;
    }

    static boolean uppercases(String aux) {
        for (int i = 0; i < aux.length(); i++) {
            if (Character.isUpperCase(aux.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    static boolean lowercases(String aux) {
        for (int i = 0; i < aux.length(); i++) {
            if (Character.isLowerCase(aux.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    static boolean digits(String aux) {
        for (int i = 0; i < aux.length(); i++) {
            if (Character.isDigit(aux.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    static boolean alfanumeric(String aux) {
        for (int i = 0; i < aux.length(); i++) {
            if (!Character.isLetter(aux.charAt(i)) && !Character.isDigit(aux.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
