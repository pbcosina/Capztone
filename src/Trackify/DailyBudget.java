package Trackify;

import java.util.ArrayList;
import java.util.List;

public class DailyBudget {
    private double totalBudget;
    private List<Expense> expenses;

    public DailyBudget(double totalBudget) {
        this.totalBudget = totalBudget;
        this.expenses = new ArrayList<>();
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void addBudget(double additionalBudget) {
        totalBudget += additionalBudget;
    }

    public double calculateRemainingBudget() {
        double totalExpenses = 0;
        for (Expense expense : expenses) {
            totalExpenses += expense.getAmount();
        }
        return totalBudget - totalExpenses;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}