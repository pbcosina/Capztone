package Trackify;

import java.util.*;

public abstract class Budget {
    protected double totalBudget;
    protected List<Expense> expenses;

    public abstract double calculateRemainingBudget();

    public void addExpense(Expense expense) throws BudgetException {
        if (expense.getAmount() > totalBudget) {
            throw new BudgetException("Expense exceeds total budget!");
        }
        expenses.add(expense);
    }

    public List<Expense> getExpenses() {
        return new ArrayList<>(expenses);
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double budget) {
        this.totalBudget = budget;
    }
}