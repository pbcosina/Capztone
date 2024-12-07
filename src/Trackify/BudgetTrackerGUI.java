package Trackify;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

public class BudgetTrackerGUI extends JFrame {
    private JTextField budgetField, categoryField, expenseField;
    private JButton addExpenseButton, viewExpensesButton, resetButton, remainingBudgetButton; // Step 1: Declare Remaining Budget Button
    private MonthlyBudget monthlyBudget;
    private boolean budgetSet = false;

    public BudgetTrackerGUI() {
        // Set a modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        // Styled components
        budgetField = createStyledTextField();
        categoryField = createStyledTextField();
        expenseField = createStyledTextField();

        addExpenseButton = createStyledButton("Add Expense", Color.pink);
        viewExpensesButton = createStyledButton("View Expenses", Color.pink);
        resetButton = createStyledButton("Reset", Color.pink); // Step 2: Initialize Reset Button
        remainingBudgetButton = createStyledButton("See Remaining Budget", Color.pink); // Step 3: Initialize Remaining Budget Button
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(10);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(Color.PINK); // Set text color to pink
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.pink, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.pink); // Changed to pink for better contrast
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }

    private void setupLayout() {
        // Use a more sophisticated layout
        setTitle("Trackify");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with better spacing
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add components with GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(createLabel("Total Budget:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(budgetField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(createLabel("Expense Category:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(createLabel("Expense Amount:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(expenseField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        mainPanel.add(addExpenseButton, gbc); // Add Expense Button

        gbc.gridy = 4;
        mainPanel.add(viewExpensesButton, gbc); // View Expenses Button

        gbc.gridy = 5;
        mainPanel.add(resetButton, gbc); // Step 4: Add Reset Button

        gbc.gridy = 6;
        mainPanel.add(remainingBudgetButton, gbc); // Step 5: Add Remaining Budget Button

        // Add main panel to frame
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.PINK); // Set text color to pink
        return label;
    }

    private void setupListeners() {
        // Add expense button listener (same as previous implementation)
        addExpenseButton.addActionListener(e -> {
            try {
                // If budget hasn't been set, set the budget
                if (!budgetSet) {
                    double budget = Double.parseDouble(budgetField.getText());
                    monthlyBudget = new MonthlyBudget(budget);
                    budgetField.setEditable(false);
                    budgetSet = true;
                }

                // Proceed with adding expense
                String category = categoryField.getText();
                double expenseAmount = Double.parseDouble(expenseField.getText());

                Expense expense = new Expense(category, expenseAmount, new Date());
                monthlyBudget.addExpense(expense);

                // show remaining budget
                double remainingBudget = monthlyBudget.calculateRemainingBudget();
                JOptionPane.showMessageDialog(this,
                        "Expense Added!\nRemaining Budget: ₱" + String.format("%.2f", remainingBudget),
                        "Expense Recorded",
                        JOptionPane.INFORMATION_MESSAGE);

                // Clear category and expense fields
                categoryField.setText("");
                expenseField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid input. Please enter valid numbers.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (BudgetException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Budget Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        // view expenses button listener
        viewExpensesButton.addActionListener(e -> {
            if (!budgetSet) {
                JOptionPane.showMessageDialog(this,
                        "Please set your budget first!",
                        "Budget Not Set",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (monthlyBudget.getExpenses().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No expenses to display. Add some expenses first!",
                        "No Expenses",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // create table model for expenses
            String[] columnNames = {"Date", "Category", "Amount"};
            Object[][] data = new Object[monthlyBudget.getExpenses().size()][3];

            for (int i = 0; i < monthlyBudget.getExpenses().size(); i++) {
                Expense expense = monthlyBudget.getExpenses().get(i);
                data[i][0] = expense.getDate().toString();
                data[i][1] = expense.getCategory();
                data[i][2] = "₱" + String.format("%.2f", expense.getAmount());
            }

            // create and show expenses table
            JTable expensesTable = new JTable(new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });

            // Style the table
            expensesTable.setFont(new Font("Arial", Font.PLAIN, 12));
            expensesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
            expensesTable.setRowHeight(25);

            JScrollPane scrollPane = new JScrollPane(expensesTable);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Expenses",
                    JOptionPane.PLAIN_MESSAGE
            );
        });

        // Step 3: Add action listener for reset button
        resetButton.addActionListener(e -> {
            budgetSet = false; // Reset flag
            monthlyBudget = null; // Clear monthly budget

            // Clear fields
            budgetField.setText("");
            categoryField.setText("");
            expenseField.setText("");
            budgetField.setEditable(true); // Make budget field editable again

            JOptionPane.showMessageDialog(this,
                    "All data has been reset.",
                    "Reset Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Step 5: Add action listener for remaining budget button
        remainingBudgetButton.addActionListener(e -> {
            if (!budgetSet) {
                JOptionPane.showMessageDialog(this,
                        "Please set your budget first!",
                        "Budget Not Set",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            double remainingBudget = monthlyBudget.calculateRemainingBudget();
            JOptionPane.showMessageDialog(this,
                    "Remaining Budget: ₱" + String.format("%.2f", remainingBudget),
                    "Remaining Budget",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
