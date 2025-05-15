package gui;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class traductor {
    private static Locale locale;
    private static ResourceBundle bundle;

    public traductor() {
        locale = Locale.forLanguageTag("es");
        bundle = ResourceBundle.getBundle("messages", locale);
    }
    public static String getString(String key) {
        bundle = ResourceBundle.getBundle("messages", locale);
        try {
            return bundle.getString(key);
        }catch (MissingResourceException e) {

        } catch (Exception e) {
            System.out.println("key not found eeeeee");
        }
        return "key not found";
    }
    public static void setLocale(int aux){
        if (aux==1){
            locale = Locale.forLanguageTag("es");
            bundle = ResourceBundle.getBundle("messages", locale);
        }else if (aux==2){
            locale = Locale.forLanguageTag("en");
            bundle = ResourceBundle.getBundle("messages", locale);
        }
    }
}
