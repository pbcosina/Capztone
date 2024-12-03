package Trackify;

import javax.swing.*;

public class BudgetTrackerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new BudgetTrackerGUI();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Error starting application: " + e.getMessage());
            }
        });
    }
}