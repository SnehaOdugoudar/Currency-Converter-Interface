package org.example.filehandler;

import org.example.model.TransactionData;
import org.example.model.TransactionData.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVFileHandlerTest {
    private static final String TEST_CSV_FILE = "test_transactions.csv";
    private CSVFileHandler csvFileHandler;

    @BeforeEach
    void setUp() {
        csvFileHandler = new CSVFileHandler();
    }

    @Test
    void testReadData() throws IOException {
        String csvContent = """
                Amount,OriginalCurrency,TargetCurrency
                100,USD,EUR
                200,GBP,INR
                """;
        TestUtils.createTestFile(TEST_CSV_FILE, csvContent);

        TransactionData data = csvFileHandler.readData(TEST_CSV_FILE);

        assertNotNull(data);
        List<Transaction> transactions = data.getTransactions();
        assertEquals(2, transactions.size());
        assertEquals(100, transactions.get(0).getAmount());
        assertEquals("USD", transactions.get(0).getCurrency());
        assertEquals("EUR", transactions.get(0).getTargetCurrency());
    }

    @Test
    void testWriteData() throws IOException {
        Transaction transaction1 = new Transaction();
        transaction1.setAmount(100.0);
        transaction1.setCurrency("USD");
        transaction1.setTargetCurrency("EUR");
        transaction1.setConvertedAmount(94);
        transaction1.setStatus("Success");
        transaction1.setError("");

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(200.0);
        transaction2.setCurrency("GBP");
        transaction2.setTargetCurrency("INR");
        transaction2.setConvertedAmount(20796);
        transaction2.setStatus("Success");
        transaction2.setError("");

        TransactionData data = new TransactionData();
        data.setTransactions(List.of(transaction1, transaction2));

        csvFileHandler.writeData(data, TEST_CSV_FILE);

        File file = new File(TEST_CSV_FILE);
        assertTrue(file.exists());

        TransactionData resultData = csvFileHandler.readData(TEST_CSV_FILE);
        assertNotNull(resultData);
        assertEquals(2, resultData.getTransactions().size());
    }
}
