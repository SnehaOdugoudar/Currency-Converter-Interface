package org.example.util;

import org.example.filehandler.CSVFileHandler;
import org.example.filehandler.IFileHandler;
import org.example.filehandler.JSONFileHandler;
import org.example.filehandler.XMLFileHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerFactoryTest {




    @Test
    void testDetermineFileType() {
        assertEquals(FileTypeUtil.FileType.CSV, FileTypeUtil.determineFileType("transactions.csv"));
        assertEquals(FileTypeUtil.FileType.JSON, FileTypeUtil.determineFileType("transactions.json"));
        assertEquals(FileTypeUtil.FileType.XML, FileTypeUtil.determineFileType("transactions.xml"));
        assertEquals(FileTypeUtil.FileType.UNKNOWN, FileTypeUtil.determineFileType("transactions.txt"));
    }
    @Test
    void testUnsupportedFileType() {
        IllegalArgumentException exception = new IllegalArgumentException("Unsupported file type: UNKNOWN");
        assertEquals("Unsupported file type: UNKNOWN", exception.getMessage());
    }
}
