package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class InfoViewerApp extends JFrame {

    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel cardsPanel = new JPanel(cardLayout);
    private static final JTextArea displayArea = new JTextArea();
    private static final JFileChooser fileChooser = new JFileChooser();
    private static final JComboBox<String> languageSelector = new JComboBox<>(new String[]{"English", "Spanish"});
    private static final JComboBox<String> dataTypeSelector = new JComboBox<>(new String[]{"Users", "Matches", "Objects"});
    private static final JComboBox<String> exportFormatSelector = new JComboBox<>(new String[]{"TXT", "PDF", "Word"});

    private static Locale currentLocale = Locale.ENGLISH;

    public InfoViewerApp() {
        setTitle("Info Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        displayArea.setEditable(false);

        JPanel homePanel = createHomePanel();
        JPanel exportPanel = createExportPanel();
        JPanel ftpPanel = createFTPPanel();

        cardsPanel.add(homePanel, "HOME");
        cardsPanel.add(exportPanel, "EXPORT");
        cardsPanel.add(ftpPanel, "FTP");

        add(cardsPanel);
        setVisible(true);
    }
    public static void iniciarPrograma(){
        new InfoViewerApp();
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton usersButton = new JButton(tr("button.showUsers"));
        JButton matchesButton = new JButton(tr("button.showMatches"));
        JButton objectsButton = new JButton(tr("button.showObjects"));
        JButton exportButton = new JButton(tr("button.export"));
        JButton ftpButton = new JButton(tr("button.ftp"));

        JPanel topButtons = new JPanel(new GridLayout(1, 5));
        topButtons.add(usersButton);
        topButtons.add(matchesButton);
        topButtons.add(objectsButton);
        topButtons.add(exportButton);
        topButtons.add(ftpButton);

        usersButton.addActionListener(e -> showInfo(getSampleUsers()));
        matchesButton.addActionListener(e -> showInfo(getSampleMatches()));
        objectsButton.addActionListener(e -> showInfo(getSampleObjects()));
        exportButton.addActionListener(e -> cardLayout.show(cardsPanel, "EXPORT"));
        ftpButton.addActionListener(e -> cardLayout.show(cardsPanel, "FTP"));

        panel.add(topButtons, BorderLayout.NORTH);
        panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        panel.add(languageSelector, BorderLayout.SOUTH);

        languageSelector.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (languageSelector.getSelectedItem()=="Spanish"){
                    traductor.setLocale(1);
                }else {
                    traductor.setLocale(2);
                }
                updateLanguage();
            }
        });

        return panel;
    }

    private JPanel createExportPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));

        JButton backButton = new JButton(tr("button.back"));
        JButton exportBtn = new JButton(tr("button.export"));

        panel.add(new JLabel(tr("label.selectData")));
        panel.add(dataTypeSelector);
        panel.add(exportFormatSelector);
        panel.add(exportBtn);
        panel.add(backButton);

        exportBtn.addActionListener(e -> exportData());
        backButton.addActionListener(e -> cardLayout.show(cardsPanel, "HOME"));

        return panel;
    }

    private JPanel createFTPPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));

        JButton backButton = new JButton(tr("button.back"));
        JButton saveButton = new JButton(tr("button.saveFTP"));
        JButton loadButton = new JButton(tr("button.loadFTP"));

        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(backButton);

        saveButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "🛠️ Simulación de guardado en FTP"));
        loadButton.addActionListener(e -> {
            // Simula cargar y mostrar datos
            displayArea.setText("📦 Datos cargados desde servidor FTP (simulado)");
            cardLayout.show(cardsPanel, "HOME");
        });
        backButton.addActionListener(e -> cardLayout.show(cardsPanel, "HOME"));

        return panel;
    }

    private void showInfo(List<String> data) {
        StringBuilder sb = new StringBuilder();
        for (String item : data) {
            sb.append(item).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    private void exportData() {
        String selectedData = (String) dataTypeSelector.getSelectedItem();
        String format = (String) exportFormatSelector.getSelectedItem();

        String content = displayArea.getText();
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay información que exportar ❗");
            return;
        }

        fileChooser.setSelectedFile(new File("output." + format.toLowerCase()));
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter out = new PrintWriter(file)) {
                out.write(content);
                JOptionPane.showMessageDialog(this, "✔️ Exportado como " + format);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateLanguage() {


        // Panel principal - HOME
        for (Component c : ((JPanel) cardsPanel.getComponent(0)).getComponents()) {
            if (c instanceof JButton button) {
                String text = switch (button.getText()) {
                    case "Show Users", "Mostrar Usuarios" -> traductor.getString("button.showUsers");
                    case "Show Matches", "Mostrar Partidas" -> traductor.getString("button.showMatches");
                    case "Show Objects", "Mostrar Objetos" -> traductor.getString("button.showObjects");
                    case "Export", "Imprimir" -> traductor.getString("button.export");
                    case "FTP Options", "Guardar/Cargar" -> traductor.getString("button.ftp");
                    default -> button.getText();
                };
                button.setText(text);
            }
        }

        // Panel EXPORT
        JPanel exportPanel = (JPanel) cardsPanel.getComponent(1);
        ((JLabel) exportPanel.getComponent(0)).setText(traductor.getString("label.selectData"));
        ((JButton) exportPanel.getComponent(3)).setText(traductor.getString("button.export")); // export button
        ((JButton) exportPanel.getComponent(4)).setText(traductor.getString("button.back"));   // back button

        // Panel FTP
        JPanel ftpPanel = (JPanel) cardsPanel.getComponent(2);
        ((JButton) ftpPanel.getComponent(0)).setText(traductor.getString("button.saveFTP"));
        ((JButton) ftpPanel.getComponent(1)).setText(traductor.getString("button.loadFTP"));
        ((JButton) ftpPanel.getComponent(2)).setText(traductor.getString("button.back"));

        SwingUtilities.updateComponentTreeUI(this);
    }


    private String tr(String key) {
        return traductor.getString(key);
    }

    // Simulaciones de datos
    private List<String> getSampleUsers() {
        return List.of("👤 User: Ana", "👤 User: Juan", "👤 User: María");
    }

    private List<String> getSampleMatches() {
        return List.of("⚔️ Match: #001", "⚔️ Match: #002", "⚔️ Match: #003");
    }

    private List<String> getSampleObjects() {

        return List.of("🎁 Object: Sword", "🎁 Object: Shield", "🎁 Object: Potion");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InfoViewerApp::new);
    }
}
