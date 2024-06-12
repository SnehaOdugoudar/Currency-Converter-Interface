package org.example.filehandler;

import org.example.model.TransactionData;
import org.example.model.TransactionData.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JSONFileHandlerTest {
    private static final String TEST_JSON_FILE = "test_transactions.json";
    private JSONFileHandler jsonFileHandler;

    @BeforeEach
    void setUp() {
        jsonFileHandler = new JSONFileHandler();
    }

    @Test
    void testReadData() throws IOException {
        String jsonContent = "{\n" +
                "  \"transactions\": [\n" +
                "    {\n" +
                "      \"Amount\": 100,\n" +
                "      \"OriginalCurrency\": \"USD\",\n" +
                "      \"TargetCurrency\": \"EUR\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"Amount\": 200,\n" +
                "      \"OriginalCurrency\": \"GBP\",\n" +
                "      \"TargetCurrency\": \"INR\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        TestUtils.createTestFile(TEST_JSON_FILE, jsonContent);

        TransactionData data = jsonFileHandler.readData(TEST_JSON_FILE);

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

        jsonFileHandler.writeData(data, TEST_JSON_FILE);

        File file = new File(TEST_JSON_FILE);
        assertTrue(file.exists());

        TransactionData resultData = jsonFileHandler.readData(TEST_JSON_FILE);
        assertNotNull(resultData);
        assertEquals(2, resultData.getTransactions().size());
    }
}
