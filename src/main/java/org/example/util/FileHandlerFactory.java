package org.example.util;

import org.example.filehandler.CSVFileHandler;
import org.example.filehandler.IFileHandler;
import org.example.filehandler.JSONFileHandler;
import org.example.filehandler.XMLFileHandler;

public class FileHandlerFactory {
//    public static IFileHandler getFileHandler(FileTypeUtil.FileType fileType) {
//        System.out.println(fileType);
//        return switch (fileType) {
//            case CSV -> new CSVFileHandler();
//            case JSON -> new JSONFileHandler();
//            case XML -> new XMLFileHandler();
//            default -> throw new IllegalArgumentException("Unsupported file type: " + fileType);
//        };
//    }

    public static IFileHandler getFileHandler(FileTypeUtil.FileType fileType) {
        System.out.println(fileType);
        switch (fileType) {
            case CSV:
                return new CSVFileHandler();
            case JSON:
                return new JSONFileHandler();
            case XML:
                return new XMLFileHandler();
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }

}
