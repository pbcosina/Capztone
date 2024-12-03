package Trackify;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class BudgetTrackerGUI extends JFrame {
    private JTextField budgetField, categoryField, expenseField;
    private JButton addExpenseButton, viewExpensesButton;
    private MonthlyBudget monthlyBudget;

    public BudgetTrackerGUI() {
        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        // Initialize Swing components
        budgetField = new JTextField(10);
        categoryField = new JTextField(10);
        expenseField = new JTextField(10);

        addExpenseButton = new JButton("Add Expense");
        viewExpensesButton = new JButton("View Expenses");
    }

    private void setupLayout() {
        // Set up GUI layout
        setTitle("Budget Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Total Budget:"));
        add(budgetField);
        add(new JLabel("Expense Category:"));
        add(categoryField);
        add(new JLabel("Expense Amount:"));
        add(expenseField);
        add(addExpenseButton);
        add(viewExpensesButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupListeners() {
        // Add expense button listener
        addExpenseButton.addActionListener(e -> {
            try {
                double budget = Double.parseDouble(budgetField.getText());
                if (monthlyBudget == null) {
                    monthlyBudget = new MonthlyBudget(budget);
                }

                String category = categoryField.getText();
                double expenseAmount = Double.parseDouble(expenseField.getText());

                Expense expense = new Expense(category, expenseAmount, new Date());
                monthlyBudget.addExpense(expense);

                // Show remaining budget
                double remainingBudget = monthlyBudget.calculateRemainingBudget();
                JOptionPane.showMessageDialog(this,
                        "Expense Added!\nRemaining Budget: $" + String.format("%.2f", remainingBudget));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid input. Please enter valid numbers.");
            } catch (BudgetException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage());
            }
        });

        // View expenses button listener
        viewExpensesButton.addActionListener(e -> {
            if (monthlyBudget == null || monthlyBudget.getExpenses().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No expenses to display. Add some expenses first!");
                return;
            }

            // Create table model for expenses
            String[] columnNames = {"Date", "Category", "Amount"};
            Object[][] data = new Object[monthlyBudget.getExpenses().size()][3];

            for (int i = 0; i < monthlyBudget.getExpenses().size(); i++) {
                Expense expense = monthlyBudget.getExpenses().get(i);
                data[i][0] = expense.getDate().toString();
                data[i][1] = expense.getCategory();
                data[i][2] = "$" + String.format("%.2f", expense.getAmount());
            }

            // Create and show expenses table
            JTable expensesTable = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(expensesTable);
            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Expenses",
                    JOptionPane.PLAIN_MESSAGE
            );
        });
    }
}