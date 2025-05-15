package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ComboBoxRenderer extends JLabel implements ListCellRenderer<Pais> {
    public ComboBoxRenderer() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Pais value, int index, boolean isSelected, boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        if (value != null)
        {
            String nombreImagen = value.getNombreImagen();
            ImageIcon originalIcon = new ImageIcon("src/img/"+nombreImagen+".png");
            Image image = originalIcon.getImage().getScaledInstance(40, 30, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(image);

            setIcon(scaledIcon);
            setText(value.getNombre());
        }
        else
        {
            setIcon(null);
            setText("");
        }

        return this;
    }
}
