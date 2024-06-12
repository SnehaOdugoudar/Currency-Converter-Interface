package org.example.filehandler;

import org.example.model.TransactionData;
import org.example.model.TransactionData.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XMLFileHandlerTest {
    private static final String TEST_XML_FILE = "test_transactions.xml";
    private XMLFileHandler xmlFileHandler;

    @BeforeEach
    void setUp() {
        xmlFileHandler = new XMLFileHandler();
    }

    @Test
    void testReadData() throws IOException {
        String xmlContent = "<TransactionData>\n" +
                "  <transaction>\n" +
                "    <Amount>100</Amount>\n" +
                "    <OriginalCurrency>USD</OriginalCurrency>\n" +
                "    <TargetCurrency>EUR</TargetCurrency>\n" +
                "  </transaction>\n" +
                "  <transaction>\n" +
                "    <Amount>200</Amount>\n" +
                "    <OriginalCurrency>GBP</OriginalCurrency>\n" +
                "    <TargetCurrency>INR</TargetCurrency>\n" +
                "  </transaction>\n" +
                "</TransactionData>";
        TestUtils.createTestFile(TEST_XML_FILE, xmlContent);

        TransactionData data = xmlFileHandler.readData(TEST_XML_FILE);

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

        xmlFileHandler.writeData(data, TEST_XML_FILE);

        File file = new File(TEST_XML_FILE);
        assertTrue(file.exists());}}