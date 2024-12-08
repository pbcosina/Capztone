package Trackify;

import java.io.*;

public class FileHandler {
    private static final String BUDGET_FILE = "budget_data.txt";

    // saves budget data to file
    public static void saveBudgetData(Budget budget) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BUDGET_FILE))) {
            writer.write(Double.toString(budget.getTotalBudget()));
            writer.newLine();
            // additional logic to save expenses
        }
    }

    // may load budget data from file
    public static Budget loadBudgetData() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(BUDGET_FILE))) {
            double totalBudget = Double.parseDouble(reader.readLine());
            // additional logic to load expenses
            return null;
        }
    }
}