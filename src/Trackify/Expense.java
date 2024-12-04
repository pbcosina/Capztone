package Trackify;

import java.util.Date;

public class Expense {
    private String category;
    private double amount;
    private Date date;

    // constructor
    public Expense(String category, double amount, Date date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    // getters
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