package Trackify;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class IntroScreen extends JFrame {
    private JPanel panel; // root panel linked to .form
    private JLabel titleLabel;
    private JButton startButton;

    public IntroScreen() {
        setContentPane(panel);
        setTitle("Welcome to Trackify");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // attach event listeners
        startButton.addActionListener(this::startProgram);
        setVisible(true);
    }

    private void startProgram(ActionEvent e) {
        dispose();
        SwingUtilities.invokeLater(BudgetTrackerGUI::new); //
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(IntroScreen::new);
    }
}