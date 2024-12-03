package Trackify;

import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String BUDGET_FILE = "budget_data.txt";

    // Save budget data to file
    public static void saveBudgetData(Budget budget) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BUDGET_FILE))) {
            writer.write(Double.toString(budget.getTotalBudget()));
            writer.newLine();
            // Additional logic to save expenses
        }
    }

    // Load budget data from file
    public static Budget loadBudgetData() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(BUDGET_FILE))) {
            double totalBudget = Double.parseDouble(reader.readLine());
            // Additional logic to load expenses
            return null; // Placeholder
        }
    }
}