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

        setSize(600, 400); // Width: 600, Height: 400
        setLocationRelativeTo(null); // Center the frame on the screen

        ttrackify.setPreferredSize(new Dimension(600, 450));
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
                    monthlyBudget = new MonthlyBudget(budget);
                    budgetField.setEditable(false);
                    budgetSet = true;
                }

                // checks if the remaining budget is already zero
                if (monthlyBudget.calculateRemainingBudget() <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "justiiiinnnn, san tayo nagpunta nung monday",
                            "No Budget Left",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String category = categoryField.getText();
                double expenseAmount = Double.parseDouble(expenseField.getText());

                Expense expense = new Expense(category, expenseAmount, new Date());
                monthlyBudget.addExpense(expense);

                double remainingBudget = monthlyBudget.calculateRemainingBudget();

                // if remaining budget is zero after adding this expense
                if (remainingBudget <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "justiiiinnnn, san tayo nagpunta nung monday",
                            "Budget Exhausted",
                            JOptionPane.WARNING_MESSAGE);
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

            // create table model for expenses
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