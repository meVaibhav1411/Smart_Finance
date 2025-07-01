package com.example.demo.DTO;

public class TransactionDTO {
    private String type;
    private double amount;
    private String description;
    private String formattedDate;

    public TransactionDTO(String type, double amount, String description, String formattedDate) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.formattedDate = formattedDate;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getFormattedDate() {
        return formattedDate;
    }
}
