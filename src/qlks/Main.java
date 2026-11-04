package qlks;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import qlks.view.DangNhapView;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }
        SwingUtilities.invokeLater(() -> new DangNhapView().setVisible(true));
    }
}
