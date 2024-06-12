package org.example.service;

import org.example.filehandler.IFileHandler;
import org.example.model.TransactionData;
import org.example.model.TransactionData.Transaction;
import org.example.util.FileHandlerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

class FileProcessorTest {
    private FileProcessor fileProcessor;
    private IFileHandler fileHandler;

    @BeforeEach
    void setUp() {
        fileProcessor = new FileProcessor();
        fileHandler = mock(IFileHandler.class);
    }

    @Test
    void testProcessFile() throws IOException {
        String inputFileName = "input.csv";
        String outputFileName = "output.csv";

        Transaction transaction1 = new Transaction();
        transaction1.setAmount(100.0);
        transaction1.setCurrency("USD");
        transaction1.setTargetCurrency("EUR");
        transaction1.setConvertedAmount(94);
        transaction1.setStatus("Success");

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(200.0);
        transaction2.setCurrency("GBP");
        transaction2.setTargetCurrency("INR");
        transaction2.setConvertedAmount(20796);
        transaction2.setStatus("Success");

        TransactionData transactionData = new TransactionData();
        transactionData.setTransactions(List.of(transaction1, transaction2));

        when(fileHandler.readData(inputFileName)).thenReturn(transactionData);

        doNothing().when(fileHandler).writeData(any(), eq(outputFileName));

        // Mocking the factory
        mockStatic(FileHandlerFactory.class);
        when(FileHandlerFactory.getFileHandler(any())).thenReturn(fileHandler);

        fileProcessor.processFile(inputFileName, outputFileName);

        verify(fileHandler).readData(inputFileName);
        verify(fileHandler).writeData(transactionData, outputFileName);
    }
}
