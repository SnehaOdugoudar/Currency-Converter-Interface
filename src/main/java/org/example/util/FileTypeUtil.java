package org.example.util;

public class FileTypeUtil {
    public enum FileType {
        CSV, JSON, XML, UNKNOWN
    }

    public static FileType determineFileType(String fileName) {
        if (fileName.endsWith(".json")) {
            return FileType.JSON;
        } else if (fileName.endsWith(".csv")) {
            return FileType.CSV;
        } else if (fileName.endsWith(".xml")) {
            return FileType.XML;
        } else {
            return FileType.UNKNOWN;
        }
    }
}
