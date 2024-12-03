package Trackify;

import java.util.*;

public class MonthlyBudget extends Budget {
    public MonthlyBudget(double totalBudget) {
        this.totalBudget = totalBudget;
        this.expenses = new ArrayList<>();
    }

    @Override
    public double calculateRemainingBudget() {
        double totalExpenses = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
        return totalBudget - totalExpenses;
    }
}