package Trackify;

import java.util.Date;

public class Expense {
    private String category;
    private double amount;
    private Date date;

    // Constructor
    public Expense(String category, double amount, Date date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    // Getters
    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }
}