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

    private DailyBudget DailyBudget;
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
                    String budgetInput = budgetField.getText().trim();
                    if (budgetInput.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Set budget first!",
                                "Budget Not Set",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    try {
                        double budget = Double.parseDouble(budgetInput);
                        DailyBudget = new DailyBudget(budget);
                        budgetField.setEditable(false);
                        budgetSet = true;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this,
                                "Invalid budget input. Please enter a valid number.",
                                "Input Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (DailyBudget.calculateRemainingBudget() <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "You have no budget left.",
                            "Budget Exhausted",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // if empty category
                String category = categoryField.getText().trim();
                if (category.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Input required: Please enter a category.",
                            "Category Missing",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // if empty expense
                String expenseInput = expenseField.getText().trim();
                if (expenseInput.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Input required: Please enter an expense amount.",
                            "Expense Missing",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double expenseAmount = Double.parseDouble(expenseInput);

                if (DailyBudget.calculateRemainingBudget() < expenseAmount) {
                    JOptionPane.showMessageDialog(this,
                            "Expense exceeds remaining budget.",
                            "Exceeds Budget",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Expense expense = new Expense(category, expenseAmount, new Date());
                DailyBudget.addExpense(expense);

                double remainingBudget = DailyBudget.calculateRemainingBudget();
                JOptionPane.showMessageDialog(this,
                        "Expense Added!\nRemaining Budget: ₱" + String.format("%.2f", remainingBudget),
                        "Expense Recorded",
                        JOptionPane.INFORMATION_MESSAGE);

                categoryField.setText("");
                expenseField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid input. Please enter valid numbers.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
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



            String[] columnNames = {"Date", "Category", "Amount"};
            Object[][] data = new Object[DailyBudget.getExpenses().size()][3];

            for (int i = 0; i < DailyBudget.getExpenses().size(); i++) {
                Expense expense = DailyBudget.getExpenses().get(i);
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
            DailyBudget = null;

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

            double remainingBudget = DailyBudget.calculateRemainingBudget();
            JOptionPane.showMessageDialog(this,
                    "Remaining Budget: ₱" + String.format("%.2f", remainingBudget),
                    "Remaining Budget",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}