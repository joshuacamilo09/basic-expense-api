package com.joshua.expense_api.dto;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseRequestDTO {

    @NotBlank
    private String title;
    @Positive
    private Double amount;
    private String description;
    private String category;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;

    public String getDescription(){return description;}
    public LocalDate getExpenseDate(){return expenseDate;}
    public String getTitle(){return title;}
    public Double getAmount(){return amount;}
    public String getCategory(){return category;}
}
