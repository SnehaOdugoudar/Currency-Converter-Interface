package org.example.filehandler;

import org.example.model.TransactionData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XMLFileHandler implements IFileHandler {

    @Override
    public TransactionData readData(String fileName) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            Element root = doc.getDocumentElement();

            TransactionData transactionData = new TransactionData();
            NodeList transactionList = root.getElementsByTagName("transaction");

            for (int i = 0; i < transactionList.getLength(); i++) {
                Element transactionElement = (Element) transactionList.item(i);
                double amount = Double.parseDouble(transactionElement.getElementsByTagName("Amount").item(0).getTextContent());


//                int amount = Integer.parseInt(transactionElement.getElementsByTagName("amount").item(0).getTextContent());
                String currency = transactionElement.getElementsByTagName("OriginalCurrency").item(0).getTextContent();
                String targetCurrency = transactionElement.getElementsByTagName("TargetCurrency").item(0).getTextContent();

                // Create new Transaction object and add it directly to TransactionData
                TransactionData.Transaction transaction = new TransactionData.Transaction();
                transaction.setAmount(amount);
                transaction.setCurrency(currency);
                transaction.setTargetCurrency(targetCurrency);
                transactionData.getTransactions().add(transaction);
            }

            return transactionData;
        } catch (ParserConfigurationException | org.xml.sax.SAXException e) {
            e.printStackTrace();
            throw new IOException("Error reading XML file: " + e.getMessage());
        }
    }


    @Override
    public void writeData(TransactionData data, String fileName) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("TransactionData");
            doc.appendChild(root);

            for (TransactionData.Transaction transaction : data.getTransactions()) {
                Element transactionElement = doc.createElement("transaction");

                Element amountElement = doc.createElement("Amount");
                amountElement.appendChild(doc.createTextNode(String.valueOf(transaction.getAmount())));
                transactionElement.appendChild(amountElement);

                Element currencyElement = doc.createElement("OriginalCurrency");
                currencyElement.appendChild(doc.createTextNode(transaction.getCurrency()));
                transactionElement.appendChild(currencyElement);

                Element TargetCurrencyElement = doc.createElement("TargetCurrency");
                TargetCurrencyElement.appendChild(doc.createTextNode(transaction.getTargetCurrency()));
                transactionElement.appendChild(TargetCurrencyElement);



                Element convertedAmountElement = doc.createElement("ConvertedAmount");
                String convertedAmount = transaction.getStatus().equalsIgnoreCase("failed") ? "" : (String.valueOf(transaction.getConvertedAmount()));

                convertedAmountElement.appendChild(doc.createTextNode(convertedAmount));
                transactionElement.appendChild(convertedAmountElement);



                Element statusElement = doc.createElement("Status");
                statusElement.appendChild(doc.createTextNode(transaction.getStatus()));
                transactionElement.appendChild(statusElement);

                Element ErrorElement = doc.createElement("Error");
                ErrorElement.appendChild(doc.createTextNode(transaction.getError()));
                transactionElement.appendChild(ErrorElement);






                root.appendChild(transactionElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            throw new IOException("Error writing XML file: " + e.getMessage());
        }
    }
}
