package org.example;

import org.example.service.FileProcessor;

import static java.lang.StringTemplate.STR;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: Main <input file> <output file>");
            System.exit(1);
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        FileProcessor processor = new FileProcessor();
        try {
            processor.processFile(inputFileName, outputFileName);
            System.out.println("Conversion completed successfully.");
        } catch (Exception e) {
            System.err.println("An error occurred during file processing: " + e.getMessage());

//            System.err.println(STR."An error occurred during file processing: \{e.getMessage()}");
//            e.printStackTrace();
        }
    }
}
