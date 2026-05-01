package com.joshua.expense_api.entity;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity //Indica que é uma tabela no banco
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Double amount;
    private String category;
    private String description;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;

    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}

    public LocalDate getExpenseDate(){return expenseDate;}
    public void setExpenseDate(LocalDate expenseDate){this.expenseDate = expenseDate;}

    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt = createdAt;}

    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}

    public void setAmount(Double amount){
        this.amount = amount;
    }
    public Double getAmount(){return amount;}

    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }

    public String getTitle(){return title;}
    public void setTitle(String title){
        this.title = title;
    }

}
