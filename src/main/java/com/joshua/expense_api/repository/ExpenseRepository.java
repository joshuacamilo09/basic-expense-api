package com.joshua.expense_api.repository;

import com.joshua.expense_api.dto.ExpenseResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.joshua.expense_api.entity.Expense;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    /*
        JpaRepository já te dá de forma nativa:
        - save()
        - findAll()
        - findById()
        - delete()
    */
    Page<Expense> findByCategoryIgnoreCase(String category, Pageable pageable);
    /*
        Este method é um query method do Spring Data JPA. O framework cria a query automaticamente a partir do nome do method.
        O retorno Page<Expense> com parâmetro Pageable é a forma padrão de ativar paginação.
    */

    Page<Expense> findExpensesByExpenseDateBetween(LocalDate expenseDateAfter, LocalDate expenseDateBefore,
                                                   Pageable pageable);

    Page<Expense> findExpensesByAmountBetween(Double amountAfter, Double amountBefore, Pageable pageable);

    Page<Expense> findAllByOrderByCreatedAtAsc(Pageable pageable);

    @Query("SELECT e FROM Expense e WHERE e.title LIKE %:title%")
    List<Expense> findExpensesByPartialTitle(@Param("title") String title);
}
