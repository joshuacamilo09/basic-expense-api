package com.joshua.expense_api.controller;

import com.joshua.expense_api.dto.ExpenseRequestDTO;
import com.joshua.expense_api.dto.ExpenseResponseDTO;
import com.joshua.expense_api.dto.PagedResponseDTO;
import com.joshua.expense_api.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//Controller - só recebe e responde

@RestController // Confirma que é uma API Rest
@RequestMapping("api/expenses") //Aqui definimos o prefixo da Rota
@Tag(name = "Expenses", description = "Operações sobre despesas")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service){
        this.service = service;
    }

    @GetMapping("/ping") //definimos o tipo de method que usamos, endpoint get.
    @Operation(summary = "Health check simples da API")
    public String ping(){
        return "pong";
    }
    //ResponseEntity serve para ter total controle das repostas HTTP, isso me deixa controlar status code, headers e o body

    @PostMapping("create")
    @Operation(summary = "Criar uma nova despesa")
    public ResponseEntity<ExpenseResponseDTO> create(@Valid @RequestBody ExpenseRequestDTO dto){
        ExpenseResponseDTO created = service.createExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/readAll")
    @Operation(summary = "Listas todas as despesas com paginação, filtro e ordenação")
    public ResponseEntity<PagedResponseDTO<ExpenseResponseDTO>> listar(@RequestParam(required = false) String category, Pageable pageable) {
        return ResponseEntity.ok(service.listar(category, pageable));

    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma despesa através do id")
    public ResponseEntity<ExpenseResponseDTO> listById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Atualizar uma despesa existente")
    public ResponseEntity<ExpenseResponseDTO> updateById(@PathVariable Long id, @Valid @RequestBody ExpenseRequestDTO dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "Apagar uma despesa através do ID")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/listOrderByDate")
    @Operation(summary = "Listar ordenadamente por datas")
    public ResponseEntity<PagedResponseDTO<ExpenseResponseDTO>> listOrderByDate(Pageable pageable){
        return ResponseEntity.ok( service.listOrderByDate(pageable));
    }

    @GetMapping("/findByMinMaxValue")
    @Operation(summary = "Encontrar valores entre o valor mínimo escolhido e valor máximo escolhido")
    public ResponseEntity<PagedResponseDTO<ExpenseResponseDTO>> findByMinMaxValue(@RequestParam double min_value, @RequestParam double max_value, Pageable pageable){
        return ResponseEntity.ok(service.findByMinMaxValue(min_value, max_value, pageable));
    }

    @GetMapping("/filterByDate")
    @Operation(summary = "Filtra as expenses entre as datas dadas")
    public ResponseEntity<PagedResponseDTO<ExpenseResponseDTO>> filterByDate(@RequestParam LocalDate data_inicial, @RequestParam LocalDate data_fim, Pageable pageable){
        return ResponseEntity.ok(service.filterByDate(data_inicial, data_fim, pageable));
    }

    @GetMapping("/findPartialTitle")
    @Operation(summary = "Encontrar títulos de expense mas com o nome do título parcial pra facilitar busca")
    public ResponseEntity<List<ExpenseResponseDTO>> findTitleByPartialTitle(@RequestParam String title){
        return ResponseEntity.ok(service.findPartialTitle(title));
    }
}

/*
    O spring doc complementa a documentação automática com as anotações do ecossistema openapi/swagger
*/
