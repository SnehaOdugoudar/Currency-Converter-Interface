package org.example.service;

import org.example.model.TransactionData;
import org.example.model.TransactionData.Transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CurrencyConverter {
    // Graph structure to represent the exchange rates
    private static final Map<String, Map<String, Double>> exchangeGraph = new HashMap<>();

    static {
        // Initialize graph edges with exchange rates
        addEdge("USD", "EUR", 0.94);
        addEdge("EUR", "GBP", 0.86);
        addEdge("GBP", "INR", 103.98);
        addEdge("AUD", "CAD", 0.89);
        addEdge("CAD", "USD", 0.73);
        addEdge("CHF", "AUD", 1.69);
        addEdge("USD", "CHF", 0.91);
        addEdge("JPY", "USD", 0.0065);
        // ... Add other rates as needed
    }

    // Add bidirectional edge in the graph
    private static void addEdge(String from, String to, double rate) {
        exchangeGraph.computeIfAbsent(from, k -> new HashMap<>()).put(to, rate);
        exchangeGraph.computeIfAbsent(to, k -> new HashMap<>()).put(from, 1.0 / rate);
    }

    public static void convert(TransactionData data) {
        for (Transaction transaction : data.getTransactions()) {
            if (containsNumbers(transaction.getCurrency())) {
                transaction.setError("Invalid original currency should not contain numbers.");
                transaction.setStatus("Failed");
                continue;
            }
            if (containsNumbers(transaction.getTargetCurrency())) {
                transaction.setError("Invalid target currency should not contain numbers.");
                transaction.setStatus("Failed");
                continue;
            }
            if (!exchangeGraph.containsKey(transaction.getCurrency())) {
                transaction.setError("Invalid original currency code.");
                transaction.setStatus("Failed");
                continue;
            }
            if (!exchangeGraph.containsKey(transaction.getTargetCurrency())) {
                transaction.setError("Invalid target currency code.");
                transaction.setStatus("Failed");
                continue;
            }

            double convertedAmount = convertCurrency(transaction.getCurrency(), transaction.getTargetCurrency(), transaction.getAmount());
            if (convertedAmount != -1) {
                transaction.setConvertedAmount(convertedAmount);
                transaction.setStatus("Success");
            } else {
                transaction.setError("Conversion rate not available.");
                transaction.setStatus("Failed");
            }
        }
    }

    private static boolean containsNumbers(String str) {
        return str.matches(".*\\d.*");
    }

    private static double convertCurrency(String originalCurrency, String targetCurrency, double amount) {
        Set<String> visited = new HashSet<>();
        return dfs(originalCurrency, targetCurrency, amount, visited);
    }

    private static double dfs(String originalCurrency, String targetCurrency, double amount, Set<String> visited) {
        if (visited.contains(originalCurrency)) {
            return -1; // Avoid loops
        }
        if (originalCurrency.equals(targetCurrency)) {
            return amount;
        }
        visited.add(originalCurrency);
        Map<String, Double> neighbours = exchangeGraph.getOrDefault(originalCurrency, new HashMap<>());
        for (Map.Entry<String, Double> entry : neighbours.entrySet()) {
            if (!visited.contains(entry.getKey())) {
                double convertedAmount = dfs(entry.getKey(), targetCurrency, amount * entry.getValue(), visited);
                if (convertedAmount != -1) {
                    return convertedAmount;
                }
            }
        }
        visited.remove(originalCurrency);
        return -1; // No conversion path found
    }
}
