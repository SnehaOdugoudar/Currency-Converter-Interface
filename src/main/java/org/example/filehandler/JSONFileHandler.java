package org.example.filehandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.model.TransactionData;
import org.example.model.TransactionData.Transaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JSONFileHandler implements IFileHandler {

    private final ObjectMapper objectMapper;

    public JSONFileHandler() {
        objectMapper = new ObjectMapper();
        // Enable pretty printing with ordered keys
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    @Override
    public TransactionData readData(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(fileName)));
        ObjectNode jsonObject = (ObjectNode) objectMapper.readTree(content);
        ArrayNode transactionsArray = (ArrayNode) jsonObject.get("transactions");

        List<Transaction> transactions = new ArrayList<>();
        transactionsArray.forEach(item -> {
            ObjectNode transactionObject = (ObjectNode) item;
            Transaction transaction = new Transaction();
            transaction.setAmount(transactionObject.get("Amount").asDouble());
            transaction.setCurrency(transactionObject.get("OriginalCurrency").asText());
            transaction.setTargetCurrency(transactionObject.get("TargetCurrency").asText());
            transactions.add(transaction);
        });

        TransactionData transactionData = new TransactionData();
        transactionData.setTransactions(transactions);
        return transactionData;
    }

    @Override
    public void writeData(TransactionData data, String fileName) throws IOException {
        ObjectNode root = objectMapper.createObjectNode();
        ArrayNode transactionsArray = root.putArray("transactions");
        for (Transaction transaction : data.getTransactions()) {
            ObjectNode transactionObject = transactionsArray.addObject();
            transactionObject.put("Amount", transaction.getAmount());
            transactionObject.put("OriginalCurrency", transaction.getCurrency());
            transactionObject.put("TargetCurrency", transaction.getTargetCurrency());
            if(transaction.getStatus().equals("Failed")){
//                System.out.println("I am entering if loop");
                transactionObject.put("ConvertedAmount", "");
            }
            else {
                transactionObject.put("ConvertedAmount", transaction.getConvertedAmount());
            }
            transactionObject.put("Status", transaction.getStatus());
            transactionObject.put("Error", transaction.getError());

        }

        objectMapper.writeValue(new File(fileName), root);
    }
}

