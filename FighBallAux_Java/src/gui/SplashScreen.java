package gui;

import java.awt.*;
import javax.swing.*;

public class SplashScreen extends JDialog {
    private JProgressBar barraProgreso;

    public SplashScreen() {
        setLocation(500, 300);
        setSize(700, 500);
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        JLabel lblImagen = new JLabel();
        ImageIcon originalIcon = new ImageIcon("src/img/logofightball2.png");
        Image imagenOriginal = originalIcon.getImage();
        Image imagenEscalada = imagenOriginal.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        lblImagen.setIcon(new ImageIcon(imagenEscalada));        contentPane.add(lblImagen, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new GridLayout(2, 1));
        barraProgreso = new JProgressBar();
        barraProgreso.setStringPainted(true);
        panelInferior.add(barraProgreso);

        JLabel lblSoft = new JLabel("FIGHBTALL APP");
        lblSoft.setHorizontalAlignment(SwingConstants.CENTER);
        panelInferior.add(lblSoft);

        contentPane.add(panelInferior, BorderLayout.SOUTH);
        setResizable(false);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void iniciarBarraCarga() throws InterruptedException {
        for (int i = 0; i <= 100; i++) {
            Thread.sleep(20);
            actualizarBarraProgreso(i);
        }
    }

    private void actualizarBarraProgreso(int valor) {
        SwingUtilities.invokeLater(() -> barraProgreso.setValue(valor));
    }

    public static void mostrarSplashYContinuar(Runnable despuesDelSplash) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                SplashScreen splash = new SplashScreen();
                splash.iniciarBarraCarga();
                splash.dispose();
                return null;
            }

            @Override
            protected void done() {
                despuesDelSplash.run();
            }
        };
        worker.execute();
    }
}
