package org.example.service;

import org.example.model.TransactionData;
import org.example.model.TransactionData.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyConverterTest {
    private TransactionData transactionData;

    @BeforeEach
    void setUp() {
        Transaction transaction1 = new Transaction();
        transaction1.setAmount(100.0);
        transaction1.setCurrency("USD");
        transaction1.setTargetCurrency("EUR");
        transaction1.setStatus("Success");

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(200.0);
        transaction2.setCurrency("GBP");
        transaction2.setTargetCurrency("INR");
        transaction2.setStatus("Success");

        transactionData = new TransactionData();
        transactionData.setTransactions(List.of(transaction1, transaction2));
    }

    @Test
    void testConvert() {
        CurrencyConverter.convert(transactionData);

        List<Transaction> transactions = transactionData.getTransactions();
        assertEquals(94, transactions.get(0).getConvertedAmount(), 0.01);
        assertEquals(20796, transactions.get(1).getConvertedAmount(), 0.01);
    }

    @Test
    void testInvalidCurrency() {
        Transaction transaction = new Transaction();
        transaction.setAmount(300.0);
        transaction.setCurrency("XYZ");
        transaction.setTargetCurrency("EUR");
        transactionData.setTransactions(List.of(transaction));

        CurrencyConverter.convert(transactionData);

        Transaction result = transactionData.getTransactions().get(0);
        assertEquals("Failed", result.getStatus());
        assertEquals("Invalid original currency code.", result.getError());
    }

    @Test
    void testInvalidTargetCurrency() {
        Transaction transaction = new Transaction();
        transaction.setAmount(300.0);
        transaction.setCurrency("USD");
        transaction.setTargetCurrency("XYZ");
        transactionData.setTransactions(List.of(transaction));

        CurrencyConverter.convert(transactionData);

        Transaction result = transactionData.getTransactions().get(0);
        assertEquals("Failed", result.getStatus());
        assertEquals("Invalid target currency code.", result.getError());
    }

    @Test
    void testContainsNumbersInOriginalCurrency() {
        Transaction transaction = new Transaction();
        transaction.setAmount(100.0);
        transaction.setCurrency("USD1");
        transaction.setTargetCurrency("EUR");
        transactionData.setTransactions(List.of(transaction));

        CurrencyConverter.convert(transactionData);

        Transaction result = transactionData.getTransactions().get(0);
        assertEquals("Failed", result.getStatus());
        assertEquals("Invalid original currency should not contain numbers.", result.getError());
    }

    @Test
    void testContainsNumbersInTargetCurrency() {
        Transaction transaction = new Transaction();
        transaction.setAmount(100.0);
        transaction.setCurrency("USD");
        transaction.setTargetCurrency("EUR1");
        transactionData.setTransactions(List.of(transaction));

        CurrencyConverter.convert(transactionData);

        Transaction result = transactionData.getTransactions().get(0);
        assertEquals("Failed", result.getStatus());
        assertEquals("Invalid target currency should not contain numbers.", result.getError());
    }

    @Test
    void testConversionRateNotAvailable() {
        Transaction transaction = new Transaction();
        transaction.setAmount(100.0);
        transaction.setCurrency("USD");
        transaction.setTargetCurrency("BTC"); // Currency not in the graph
        transactionData.setTransactions(List.of(transaction));

        CurrencyConverter.convert(transactionData);

        Transaction result = transactionData.getTransactions().get(0);
        assertEquals("Failed", result.getStatus());
        assertEquals("Invalid target currency code.", result.getError());
    }
}
