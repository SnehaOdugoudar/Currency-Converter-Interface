package org.example.filehandler;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.example.model.TransactionData;
import org.example.model.TransactionData.Transaction;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVFileHandler implements IFileHandler {

    @Override
    public TransactionData readData(String fileName) throws IOException {
        List<Transaction> transactions = new ArrayList<>();

        try (FileReader reader = new FileReader(fileName);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withHeader("Amount", "OriginalCurrency", "TargetCurrency")
                     .withFirstRecordAsHeader())) {
            for (CSVRecord csvRecord : csvParser) {
                Transaction transaction = new Transaction();
                transaction.setAmount(Double.parseDouble(csvRecord.get("Amount")));
                transaction.setCurrency(csvRecord.get("OriginalCurrency"));
                transaction.setTargetCurrency(csvRecord.get("TargetCurrency"));
                transactions.add(transaction);
            }
        }

        TransactionData transactionData = new TransactionData();
        transactionData.setTransactions(transactions);
        return transactionData;
    }

    @Override
    public void writeData(TransactionData data, String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("Amount", "OriginalCurrency", "TargetCurrency", "ConvertedAmount", "Status", "Error"))) {

            for (Transaction transaction : data.getTransactions()) {
                String convertedAmount = (transaction.getStatus().equalsIgnoreCase("failed")) ? "" : String.valueOf(transaction.getConvertedAmount());
                csvPrinter.printRecord(
                        transaction.getAmount(),
                        transaction.getCurrency(),
                        transaction.getTargetCurrency(),
                        convertedAmount,
                        transaction.getStatus(),
                        transaction.getError()
                );
            }
            csvPrinter.flush();
        }
    }
}


//package org.example.filehandler;
//
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv.CSVPrinter;
//import org.apache.commons.csv.CSVRecord;
//import org.example.model.TransactionData;
//import org.example.model.TransactionData.Transaction;
//
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CSVFileHandler implements IFileHandler {
//
//    @Override
//    public TransactionData readData(String fileName) throws IOException {
//        List<Transaction> transactions = new ArrayList<>();
//
//        try (FileReader reader = new FileReader(fileName);
//             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
//            for (CSVRecord csvRecord : csvParser) {
//                Transaction transaction = new Transaction();
//                transaction.setAmount(Double.parseDouble(csvRecord.get("Amount")));
//                transaction.setCurrency(csvRecord.get("OriginalCurrency"));
//                transaction.setTargetCurrency(csvRecord.get("TargetCurrency"));
//                transactions.add(transaction);
//            }
//        }
//
//        TransactionData transactionData = new TransactionData();
//        transactionData.setTransactions(transactions);
//        return transactionData;
//    }
//
//    @Override
//    public void writeData(TransactionData data, String fileName) throws IOException {
//        try (FileWriter writer = new FileWriter(fileName);
//             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
//                     .withHeader("Amount", "Currency", "TargetCurrency", "ConvertedAmount", "Status", "Error"))) {
//
//            for (Transaction transaction : data.getTransactions()) {
//                String convertedAmount = (transaction.getStatus().equalsIgnoreCase("failed")) ? "" : String.valueOf(transaction.getConvertedAmount());
//                csvPrinter.printRecord(
//                        transaction.getAmount(),
//                        transaction.getCurrency(),
//                        transaction.getTargetCurrency(),
//
//                        convertedAmount,
//                        transaction.getStatus(),
//                        transaction.getError()
//                );
//            }
//            csvPrinter.flush();
//        }
//    }
//}
