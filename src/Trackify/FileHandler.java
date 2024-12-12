package Trackify;

import java.io.*;

public class  FileHandler {
    private static final String BUDGET_FILE = "budget_data.txt";

    public static void saveBudgetData(Budget budget) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BUDGET_FILE))) {
            writer.write(Double.toString(budget.getTotalBudget()));
            writer.newLine();
        }
    }

    public static Budget loadBudgetData() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(BUDGET_FILE))) {
            double totalBudget = Double.parseDouble(reader.readLine());
            return null;
        }
    }
}