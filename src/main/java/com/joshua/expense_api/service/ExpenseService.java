package com.joshua.expense_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.joshua.expense_api.dto.ExpenseRequestDTO;
import com.joshua.expense_api.dto.ExpenseResponseDTO;
import com.joshua.expense_api.dto.PagedResponseDTO;
import com.joshua.expense_api.entity.Expense;
import com.joshua.expense_api.exception.ResourceNotFoundException;
import com.joshua.expense_api.repository.ExpenseRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service //Para marcar que é um service, onde fica a lógica de negócio
public class ExpenseService {

    private static final Logger log = LoggerFactory.getLogger(ExpenseService.class);
    private final ExpenseRepository repository;

    public ExpenseService (ExpenseRepository repository){
        this.repository = repository;
    }

    public ExpenseResponseDTO createExpense(ExpenseRequestDTO dto){

        log.info("A criar nova despesa com título='{}', categoria='{}'", dto.getTitle(), dto.getCategory());

        Expense expense = new Expense();
        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setCategory(dto.getCategory());
        expense.setExpenseDate(dto.getExpenseDate());
        expense.setDescription(dto.getDescription());
        expense.setCreatedAt(LocalDateTime.now());

        Expense saved = repository.save(expense);

        log.info("Despesa criada com id='{}'", saved.getId());

        return toResponseDTO(saved);
    }

    public List<ExpenseResponseDTO> findPartialTitle(String title) {
        List<Expense> expenses = repository.findExpensesByPartialTitle(title);
        return expenses.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PagedResponseDTO<ExpenseResponseDTO> filterByDate(LocalDate data_inicial, LocalDate data_fim, Pageable pageable){
        log.debug("Filtrando despesas entre {} e {}. Página='{}'", data_inicial, data_fim, pageable.getPageNumber());

        Page<Expense> pageResult = repository.findExpensesByExpenseDateBetween(data_inicial, data_fim, pageable);

        return getExpenseResponseDTOPagedResponseDTO(pageResult);
    }

    public PagedResponseDTO<ExpenseResponseDTO> findByMinMaxValue(double min_value, double max_value, Pageable pageable){
        log.debug("Filtrando valores entre {} e  {}. Página= '{}' ", min_value, max_value, pageable.getPageNumber());
        Page<Expense> pageResult = repository.findExpensesByAmountBetween(min_value, max_value, pageable);
        return getExpenseResponseDTOPagedResponseDTO(pageResult);
    }

    public PagedResponseDTO<ExpenseResponseDTO> listOrderByDate(Pageable pageable){
        Page<Expense> pageResult = repository.findAllByOrderByCreatedAtAsc(pageable);
        return getExpenseResponseDTOPagedResponseDTO(pageResult);
    }

    public PagedResponseDTO<ExpenseResponseDTO> listar(String category, Pageable pageable) {
        log.debug("A listar despesas. Categoia='{}', página='{}', tamanho='{}', ordenação:'{}' ",
                category, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<Expense> pageResult; //Já contém toda essa informação de forma automática.
        // Então meio que so criamos o PagedResponse para organizar os dados no formato ou nome que queremos,
        // para não ter de expor a entidade expense diretamente
        // E também porque o Page do Spring não é amigável para retornar como JSON

        if (category != null && !category.isBlank()) {
            pageResult = repository.findByCategoryIgnoreCase(category, pageable);
        } else {
            pageResult = repository.findAll(pageable);
        }

        return getExpenseResponseDTOPagedResponseDTO(pageResult);
    }

    public ExpenseResponseDTO findById(Long id) {
        log.debug("A procurar despesa com id='{}'", id);
        Expense foundIdUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despensa com id: " + id + " não encontrada"));

        return toResponseDTO(foundIdUser);
    }
    

    public ExpenseResponseDTO update(Long id, ExpenseRequestDTO dto){
        log.info("A atualizar despesa do id='{}'", id);

        Expense expense = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id not found"));

        if(dto.getTitle() != null){
            expense.setTitle(dto.getTitle());
        }

        if(dto.getCategory() != null){
            expense.setCategory(dto.getCategory());
        }

        if(dto.getAmount() != null){
            expense.setAmount(dto.getAmount());
        }

        if(dto.getExpenseDate() != null){
            expense.setExpenseDate(dto.getExpenseDate());
        }

        if(dto.getDescription() != null){
            expense.setDescription(dto.getDescription());
        }

        log.info("Despesa do id:'{}' atualizada com sucesso", id);
        return toResponseDTO(repository.save(expense));
    }

    public void delete(Long id){
        log.warn("Pedido para apagar despesa com id={}", id);
        Expense userFinded = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id not found"));

        repository.delete(userFinded);
        log.info("Despesa do id='{}' com sucesso", id);
    }

    private ExpenseResponseDTO toResponseDTO(Expense expense) {
        ExpenseResponseDTO dto = new ExpenseResponseDTO();
        dto.setId(expense.getId());
        dto.setTitle(expense.getTitle());
        dto.setAmount(expense.getAmount());
        dto.setCategory(expense.getCategory());
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setDescription(expense.getDescription());
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    private PagedResponseDTO<ExpenseResponseDTO> getExpenseResponseDTOPagedResponseDTO(Page<Expense> pageResult) {
        PagedResponseDTO<ExpenseResponseDTO> response = new PagedResponseDTO<>();

        response.setContent(pageResult.getContent()
                .stream()
                .map(this::toResponseDTO)
                .toList()
        );

        response.setPage(pageResult.getNumber());
        response.setSize(pageResult.getSize());
        response.setTotalElements(pageResult.getTotalElements());
        response.setTotalPages(pageResult.getTotalPages());
        response.setLast(pageResult.isLast());

        return response;
    }
}
