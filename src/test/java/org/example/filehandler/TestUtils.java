package org.example.filehandler;

import java.io.FileWriter;
import java.io.IOException;

public class TestUtils {
    public static void createTestFile(String fileName, String content) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        }
    }
}
