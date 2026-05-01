package com.joshua.expense_api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

//Até agora estávamos a devolver a entity, funciona, mas vamos separar melhor.
public class ExpenseResponseDTO {
    private Long id;
    private String title;
    private Double amount;
    private String category;
    private String description;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDate getExpenseDate() { return expenseDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setDescription(String description){this.description = description;}
    public void setExpenseDate(LocalDate expenseDate){this.expenseDate = expenseDate;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt = createdAt;}
    public void setId(Long id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setAmount(Double amount) {this.amount = amount;}
    public void setCategory(String category) {this.category = category;}
}
