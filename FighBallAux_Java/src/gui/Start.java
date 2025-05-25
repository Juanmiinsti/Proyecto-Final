package gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.sun.tools.javac.Main;

import java.io.FileInputStream;

public class Start {
    public static void main(String[] args) {
        try {
            IntelliJTheme.setup(new FileInputStream("src/themes/Chicken.theme.json"));

        }catch (Exception e){

        }
        new traductor();
        new DataSource();
        LoginForm loginForm = new LoginForm();

    }
}
