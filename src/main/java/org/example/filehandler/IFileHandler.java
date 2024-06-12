package org.example.filehandler;

import org.example.model.TransactionData;
import java.io.IOException;
//import java.io.IOException;

public interface IFileHandler {
    TransactionData readData(String fileName) throws IOException;
    void writeData(TransactionData data, String fileName) throws IOException;
}
