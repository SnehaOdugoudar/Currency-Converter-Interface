package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TransactionData {
    private List<Transaction> transactions = new ArrayList<>(); // Initialize the list

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public static class Transaction {

        private double Amount;
        private String OriginalCurrency;
        private String TargetCurrency;
        private double ConvertedAmount;
        private String Status;
        private String error;

        // Getters and setters...

        public double getAmount() {
            return Amount;
        }

        public void setAmount(Double Amount) {

            this.Amount = Amount;
//            System.out.println("This is here in setamount");
        }

        public String getCurrency() {
            return OriginalCurrency;
        }

        public void setCurrency(String OriginalCurrency) {
            this.OriginalCurrency = OriginalCurrency;
        }

        public String getTargetCurrency() {
            return TargetCurrency;
        }

        public void setTargetCurrency(String TargetCurrency) {
            this.TargetCurrency = TargetCurrency;
        }

        public double getConvertedAmount() {


            return ConvertedAmount;
        }

        public void setConvertedAmount(double ConvertedAmount) {
            this.ConvertedAmount = ConvertedAmount;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}


