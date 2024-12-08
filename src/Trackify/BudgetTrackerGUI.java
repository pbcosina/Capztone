package Trackify;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

public class BudgetTrackerGUI extends JFrame {
    private JTextField budgetField, categoryField, expenseField;
    private JButton addExpenseButton, viewExpensesButton, resetButton, remainingBudgetButton;
    private MonthlyBudget monthlyBudget;
    private boolean budgetSet = false;

    public BudgetTrackerGUI() {
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
        budgetField = createStyledTextField();
        categoryField = createStyledTextField();
        expenseField = createStyledTextField();

        addExpenseButton = createStyledButton("Add Expense", Color.pink);
        viewExpensesButton = createStyledButton("View Expenses", Color.pink);
        resetButton = createStyledButton("Reset", Color.pink);
        remainingBudgetButton = createStyledButton("See Remaining Budget", Color.pink);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(10);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(Color.PINK);
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
        button.setForeground(Color.pink);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }

    private void setupLayout() {
        setTitle("Trackify");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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
        mainPanel.add(addExpenseButton, gbc);

        gbc.gridy = 4;
        mainPanel.add(viewExpensesButton, gbc);

        gbc.gridy = 5;
        mainPanel.add(remainingBudgetButton, gbc);

        gbc.gridy = 6;
        mainPanel.add(resetButton, gbc);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.PINK);
        return label;
    }

    private void setupListeners() {
        addExpenseButton.addActionListener(e -> {
            try {
                if (!budgetSet) {
                    double budget = Double.parseDouble(budgetField.getText());
                    if (budget <= 0) {
                        JOptionPane.showMessageDialog(this,
                                "Budget must be greater than 0 to proceed.",
                                "Invalid Budget",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    monthlyBudget = new MonthlyBudget(budget);
                    budgetField.setEditable(false);
                    budgetSet = true;
                }

                double remainingBudget = monthlyBudget.calculateRemainingBudget();
                if (remainingBudget <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "No remaining budget available. You cannot add more expenses.",
                            "Budget Exceeded",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String category = categoryField.getText();
                double expenseAmount = Double.parseDouble(expenseField.getText());

                if (expenseAmount <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Expense amount must be greater than 0.",
                            "Invalid Expense Amount",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (expenseAmount > remainingBudget) {
                    JOptionPane.showMessageDialog(this,
                            "Expense exceeds the remaining budget. Please adjust the amount.",
                            "Insufficient Budget",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Expense expense = new Expense(category, expenseAmount, new Date());
                monthlyBudget.addExpense(expense);

                remainingBudget = monthlyBudget.calculateRemainingBudget();
                if (remainingBudget == 0) {
                    JOptionPane.showMessageDialog(this,
                            "Your budget has been fully exhausted.",
                            "Budget Exhausted",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Clear the total budget field and reset the state
                    budgetField.setText("");
                    budgetField.setEditable(true);
                    budgetSet = false;
                    monthlyBudget = null;
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Expense Added!\nRemaining Budget: ₱" + String.format("%.2f", remainingBudget),
                            "Expense Recorded",
                            JOptionPane.INFORMATION_MESSAGE);
                }

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

            String[] columnNames = {"Date", "Category", "Amount"};
            Object[][] data = new Object[monthlyBudget.getExpenses().size()][3];

            for (int i = 0; i < monthlyBudget.getExpenses().size(); i++) {
                Expense expense = monthlyBudget.getExpenses().get(i);
                data[i][0] = expense.getDate().toString();
                data[i][1] = expense.getCategory();
                data[i][2] = "₱" + String.format("%.2f", expense.getAmount());
            }

            JTable expensesTable = new JTable(new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });

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

        resetButton.addActionListener(e -> {
            budgetSet = false;
            monthlyBudget = null;

            budgetField.setText("");
            categoryField.setText("");
            expenseField.setText("");
            budgetField.setEditable(true);

            JOptionPane.showMessageDialog(this,
                    "All data has been reset.",
                    "Reset Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        });

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
