package Trackify;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class BudgetTrackerGUI extends JFrame {
    private JTextField budgetField;
    private JTextField categoryField;
    private JTextField expenseField;
    private JButton addExpenseButton;
    private JButton viewExpensesButton;
    private JButton resetButton;
    private JButton remainingBudgetButton;
    private JPanel ttrackify;
    private JLabel amount;
    private JLabel category;
    private JLabel budget;

    private MonthlyBudget monthlyBudget;
    private boolean budgetSet = false;

    public BudgetTrackerGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentPane(ttrackify);
        setTitle("Trackify");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(600, 400);
        setLocationRelativeTo(null);

        ttrackify.setPreferredSize(new Dimension(600, 450));
        setResizable(false);
        budgetField.setPreferredSize(new Dimension(250, 40));
        categoryField.setPreferredSize(new Dimension(250, 40));
        expenseField.setPreferredSize(new Dimension(250, 40));

        pack();
        setVisible(true);

        setupListeners();
    }

    private void setupListeners() {
        addExpenseButton.addActionListener(e -> {
            try {
                if (!budgetSet) {
                    double budget = Double.parseDouble(budgetField.getText());
                    if (budget <= 0) {
                        JOptionPane.showMessageDialog(this,
                                "Budget must be greater than 0.",
                                "Invalid Budget",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    monthlyBudget = new MonthlyBudget(budget);
                    budgetField.setEditable(false);
                    budgetSet = true;
                }

                // Check if the remaining budget is zero
                if (monthlyBudget.calculateRemainingBudget() <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "No remaining budget available. Budget will be reset.",
                            "Budget Exhausted",
                            JOptionPane.INFORMATION_MESSAGE);
                    resetBudget();  // Automatically reset if budget is exhausted
                    return;
                }

                String category = categoryField.getText();
                double expenseAmount = Double.parseDouble(expenseField.getText());

                if (expenseAmount <= 0) {
                    throw new BudgetException("Expense amount must be greater than 0.");
                }

                if (expenseAmount > monthlyBudget.calculateRemainingBudget()) {
                    JOptionPane.showMessageDialog(this,
                            "Expense exceeds the remaining budget. Please adjust the amount.",
                            "Exceeds Budget",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Expense expense = new Expense(category, expenseAmount, new Date());
                monthlyBudget.addExpense(expense);

                double remainingBudget = monthlyBudget.calculateRemainingBudget();

                if (remainingBudget == 0) {
                    JOptionPane.showMessageDialog(this,
                            "Budget fully exhausted. Budget will be reset.",
                            "Budget Exhausted",
                            JOptionPane.INFORMATION_MESSAGE);
                    resetBudget();  // Automatically reset if budget reaches zero
                } else if (remainingBudget < 10) {
                    int option = JOptionPane.showConfirmDialog(this,
                            "Your remaining budget is below ₱10. Would you like to add more budget?",
                            "Low Budget Warning",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (option == JOptionPane.YES_OPTION) {
                        String additionalBudgetInput = JOptionPane.showInputDialog(this,
                                "Enter the additional budget amount:",
                                "Add Budget",
                                JOptionPane.QUESTION_MESSAGE);

                        try {
                            double additionalBudget = Double.parseDouble(additionalBudgetInput);
                            if (additionalBudget > 0) {
                                monthlyBudget.addBudget(additionalBudget);
                                // Update the budgetField with the new total budget
                                budgetField.setText(String.format("%.2f", monthlyBudget.getTotalBudget()));
                                JOptionPane.showMessageDialog(this,
                                        "Budget successfully increased! Remaining Budget: ₱" +
                                                String.format("%.2f", monthlyBudget.calculateRemainingBudget()),
                                        "Budget Updated",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(this,
                                        "Additional budget must be greater than 0.",
                                        "Invalid Input",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this,
                                    "Invalid input. Please enter a valid number.",
                                    "Input Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
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

            JTable expensesTable = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(expensesTable);

            JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "Expenses",
                    JOptionPane.PLAIN_MESSAGE);
        });

        resetButton.addActionListener(e -> resetBudget());

        remainingBudgetButton.addActionListener(e -> {
            if (!budgetSet) {
                JOptionPane.showMessageDialog(this,
                        "Please set your budget first!",
                        "Budget Not Set",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            double remainingBudget = monthlyBudget.calculateRemainingBudget();
            int option = JOptionPane.showConfirmDialog(this,
                    "Remaining Budget: ₱" + String.format("%.2f", remainingBudget) +
                            "\nWould you like to add more to your budget?",
                    "Remaining Budget",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                String additionalBudgetInput = JOptionPane.showInputDialog(this,
                        "Enter the additional budget amount:",
                        "Add Budget",
                        JOptionPane.QUESTION_MESSAGE);

                try {
                    double additionalBudget = Double.parseDouble(additionalBudgetInput);
                    if (additionalBudget > 0) {
                        monthlyBudget.addBudget(additionalBudget);
                        // Update the budgetField with the new total budget
                        budgetField.setText(String.format("%.2f", monthlyBudget.getTotalBudget()));
                        JOptionPane.showMessageDialog(this,
                                "Budget successfully increased! Remaining Budget: ₱" +
                                        String.format("%.2f", monthlyBudget.calculateRemainingBudget()),
                                "Budget Updated",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Additional budget must be greater than 0.",
                                "Invalid Input",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid input. Please enter a valid number.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Remaining Budget: ₱" + String.format("%.2f", remainingBudget),
                        "Remaining Budget",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void resetBudget() {
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
    }
}