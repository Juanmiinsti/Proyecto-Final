package gui;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import gui.Models.CharacterModel;
import gui.Models.ItemModel;
import gui.Models.MatchModel;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * InfoViewerApp is a Swing-based Java GUI that allows users to:
 * - View information about Characters, Matches, and Items.
 * - Export this information in various formats (TXT, PDF, Word).
 * - Simulate FTP operations (Save/Load).
 * - Switch between languages (English/Spanish).
 *
 * The UI is built using CardLayout to allow panel navigation.
 */
public class InfoViewerApp extends JFrame {

    // Core UI components and layout
    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel cardsPanel = new JPanel(cardLayout);
    private static final JTextArea displayArea = new JTextArea();
    private static final JFileChooser fileChooser = new JFileChooser();
    private static final JComboBox<String> dataTypeSelector = new JComboBox<>(new String[]{"Characters", "Matches", "Objects"});
    private static final JComboBox<String> exportFormatSelector = new JComboBox<>(new String[]{"TXT", "PDF", "Word"});

    // Selected type for export
    private static String typeselected;
    private static Locale currentLocale = Locale.ENGLISH;

    // ResourceBundle used for language translation
    private static final ResourceBundle traductor = ResourceBundle.getBundle("messages", currentLocale);

    /**
     * Constructor sets up the frame and initializes all UI components.
     */
    public InfoViewerApp() {
        setTitle("WELCOME " + DataSource.userName);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center on screen

        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        displayArea.setEditable(false);

        // Create and register views
        JPanel homePanel = createHomePanel();
        JPanel exportPanel = createExportPanel();
        JPanel ftpPanel = createFTPPanel();

        cardsPanel.add(homePanel, "HOME");
        cardsPanel.add(exportPanel, "EXPORT");
        cardsPanel.add(ftpPanel, "FTP");

        add(cardsPanel);
        setVisible(true);
    }

    /**
     * Launch the application.
     */
    public static void iniciarPrograma() {
        new InfoViewerApp();
    }

    /**
     * Creates the main "HOME" screen with navigation buttons and data display area.
     * @return JPanel for the HOME screen.
     */
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Buttons for navigation and data viewing
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

        // Action listeners for each button
        usersButton.addActionListener(e -> showInfo(getCharacters()));
        matchesButton.addActionListener(e -> showInfo(getSampleMatches()));
        objectsButton.addActionListener(e -> showInfo(getSampleObjects()));
        exportButton.addActionListener(e -> cardLayout.show(cardsPanel, "EXPORT"));
        ftpButton.addActionListener(e -> cardLayout.show(cardsPanel, "FTP"));

        panel.add(topButtons, BorderLayout.NORTH);
        panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the EXPORT panel where users select data type and export format.
     * @return JPanel for the EXPORT screen.
     */
    private JPanel createExportPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));

        JButton backButton = new JButton(tr("button.back"));
        JButton exportBtn = new JButton(tr("button.export"));

        panel.add(new JLabel(tr("label.selectData")));
        panel.add(dataTypeSelector);
        panel.add(new JLabel(tr("label.selectData"))); // Possibly redundant
        panel.add(exportFormatSelector);
        panel.add(exportBtn);
        panel.add(backButton);

        exportBtn.addActionListener(e -> exportData());
        backButton.addActionListener(e -> cardLayout.show(cardsPanel, "HOME"));

        return panel;
    }

    /**
     * Creates the FTP panel for save/load simulations.
     * @return JPanel for the FTP screen.
     */
    private JPanel createFTPPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));

        JButton backButton = new JButton(tr("button.back"));
        JButton saveButton = new JButton(tr("button.saveFTP"));
        JButton loadButton = new JButton(tr("button.loadFTP"));

        backButton.addActionListener(e -> cardLayout.show(cardsPanel, "HOME"));

        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(backButton);

        return panel;
    }

    /**
     * Displays a list of data strings in the display area.
     * @param data List of text entries to be shown.
     */
    private void showInfo(List<String> data) {
        StringBuilder sb = new StringBuilder();
        for (String item : data) {
            sb.append(item).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    /**
     * Handles the export operation based on selected format (TXT, PDF, Word).
     */
    private void exportData() {
        typeselected = (String) dataTypeSelector.getSelectedItem();
        String format = (String) exportFormatSelector.getSelectedItem();
        String content = displayArea.getText();

        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data to export ❗");
            return;
        }

        fileChooser.setSelectedFile(new File("output." + format.toLowerCase()));
        int option = fileChooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            switch (format) {
                case "TXT":
                    try (PrintWriter out = new PrintWriter(file)) {
                        out.write(content);
                        JOptionPane.showMessageDialog(this, "✔️ Exported as " + format);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case "PDF":
                    exportToPDF(file);
                    break;

                case "Word":
                    exportToWord(file);
                    break;
            }
        }
    }

    /**
     * Exports data to a PDF file using iText.
     */
    private void exportToPDF(File file) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            document.add(new Paragraph("Exported: " + typeselected));
            document.add(new Paragraph(" ")); // Add spacing

            switch (typeselected) {
                case "Characters":
                    for (CharacterModel c : DataSource.characters) {
                        document.add(new Paragraph("ID: " + c.getId()));
                        document.add(new Paragraph("Name: " + c.getName()));
                        document.add(new Paragraph("Health: " + c.getMax_health()));
                        document.add(new Paragraph("Stamina: " + c.getMax_stamina()));
                        document.add(new Paragraph("Damage: " + c.getDamage()));
                        document.add(new Paragraph(" "));
                    }
                    break;

                case "Items":
                    for (ItemModel i : DataSource.items) {
                        document.add(new Paragraph("ID: " + i.getId()));
                        document.add(new Paragraph("Name: " + i.getName()));
                        document.add(new Paragraph("Description: " + i.getDescription()));
                        document.add(new Paragraph(" "));
                    }
                    break;

                case "Matches":
                    for (MatchModel m : DataSource.matches) {
                        document.add(new Paragraph("ID: " + m.getId()));
                        document.add(new Paragraph("Date: " + m.getDate()));
                        document.add(new Paragraph("Length: " + m.getLength()));
                        document.add(new Paragraph("Winner Char ID: " + m.getCharWinnerId()));
                        document.add(new Paragraph("Loser Char ID: " + m.getCharLoserId()));
                        document.add(new Paragraph("Winner User ID: " + m.getUserWinnerId()));
                        document.add(new Paragraph("Loser User ID: " + m.getUserLoserId()));
                        document.add(new Paragraph(" "));
                    }
                    break;

                default:
                    document.add(new Paragraph("❗ Invalid type selected"));
            }

            document.close();
            JOptionPane.showMessageDialog(this, "✔️ Exported as PDF");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Failed to export as PDF");
        }
    }

    /**
     * Exports data to a Microsoft Word (.docx) file using Apache POI.
     */
    private void exportToWord(File file) {
        try (XWPFDocument document = new XWPFDocument()) {

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun runTitle = title.createRun();
            runTitle.setText("Exported: " + typeselected);
            runTitle.setBold(true);
            runTitle.setFontSize(16);

            document.createParagraph().createRun().addBreak();

            switch (typeselected) {
                case "Characters":
                    for (CharacterModel c : DataSource.characters) {
                        createLine(document, "ID: " + c.getId());
                        createLine(document, "Name: " + c.getName());
                        createLine(document, "Health: " + c.getMax_health());
                        createLine(document, "Stamina: " + c.getMax_stamina());
                        createLine(document, "Damage: " + c.getDamage());
                        createEmptyLine(document);
                    }
                    break;

                case "Items":
                    for (ItemModel i : DataSource.items) {
                        createLine(document, "ID: " + i.getId());
                        createLine(document, "Name: " + i.getName());
                        createLine(document, "Description: " + i.getDescription());
                        createEmptyLine(document);
                    }
                    break;

                case "Matches":
                    for (MatchModel m : DataSource.matches) {
                        createLine(document, "ID: " + m.getId());
                        createLine(document, "Date: " + m.getDate());
                        createLine(document, "Length: " + m.getLength());
                        createLine(document, "Winner Char ID: " + m.getCharWinnerId());
                        createLine(document, "Loser Char ID: " + m.getCharLoserId());
                        createLine(document, "Winner User ID: " + m.getUserWinnerId());
                        createLine(document, "Loser User ID: " + m.getUserLoserId());
                        createEmptyLine(document);
                    }
                    break;

                default:
                    createLine(document, "❗ Invalid type selected");
            }

            try (FileOutputStream out = new FileOutputStream(file)) {
                document.write(out);
            }

            JOptionPane.showMessageDialog(this, "✔️ Exported as Word");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Failed to export as Word");
        }
    }

    // Helper method: creates a single line in the Word document
    private void createLine(XWPFDocument doc, String text) {
        XWPFParagraph paragraph = doc.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(text);
    }

    // Helper method: adds an empty line to Word document
    private void createEmptyLine(XWPFDocument doc) {
        createLine(doc, " ");
    }

    /**
     * Translation helper method that retrieves the localized string.
     * @param key Key from the ResourceBundle.
     * @return Translated string.
     */
    private String tr(String key) {
        return traductor.getString(key);
    }

    /**
     * Returns a list of characters formatted as strings.
     */
    private List<String> getCharacters() {
        typeselected = "Characters";
        List<String> list = new ArrayList<>();
        for (CharacterModel c : DataSource.characters) {
            list.add(c.toString());
        }
        return list;
    }

    /**
     * Returns a list of match entries formatted as strings.
     */
    private List<String> getSampleMatches() {
        typeselected = "Matches";
        List<String> list = new ArrayList<>();
        for (MatchModel c : DataSource.matches) {
            list.add(c.toString());
        }
        return list;
    }

    /**
     * Returns a list of items formatted as strings, prefixed with an icon.
     */
    private List<String> getSampleObjects() {
        typeselected = "Items";
        List<String> list = new ArrayList<>();
        for (ItemModel c : DataSource.items) {
            list.add("🎁" + c.toString());
        }
        return list;
    }

}
