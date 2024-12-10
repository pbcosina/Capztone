package Trackify;

import java.util.*;

public abstract class Budget {
    protected double totalBudget;
    protected List<Expense> expenses;

    // abstract method to calculate remaining budget
    public abstract double calculateRemainingBudget();

    // concrete method to add expense
    public void addExpense(Expense expense) throws BudgetException {
        if (expense.getAmount() > totalBudget) {
            throw new BudgetException("Expense exceeds total budget!");
        }
        expenses.add(expense);
    }

    // getter for expenses list
    public List<Expense> getExpenses() {
        return new ArrayList<>(expenses); // Return a copy to prevent direct modification
    }

    // getters and setters
    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double budget) {
        this.totalBudget = budget;
    }
}