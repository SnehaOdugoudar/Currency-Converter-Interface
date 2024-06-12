package org.example.service;

import org.example.filehandler.IFileHandler;
import org.example.model.TransactionData;
import org.example.util.FileHandlerFactory;
import org.example.util.FileTypeUtil;

import java.io.IOException;

public class FileProcessor {
    public void processFile(String inputFileName, String outputFileName) throws IOException {
//        System.out.println("This is here 1");
        IFileHandler fileHandler = FileHandlerFactory.getFileHandler(FileTypeUtil.determineFileType(inputFileName));
//        System.out.println("This is here");

        if (fileHandler == null) {
            throw new IOException(STR."Unsupported file type for file: \{inputFileName}");
        }
        TransactionData data = fileHandler.readData(inputFileName);
//        System.out.println("This is here 2");

        CurrencyConverter.convert(data);
//        System.out.println("This is here 4");
        fileHandler.writeData(data, outputFileName);
    }
}
