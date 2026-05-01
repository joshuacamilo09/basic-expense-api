# O que é uma API Rest ? 
- é um conjunto de endpoints HTTP que manipulam recursos


## Métodos HTTP

- GET = buscar dados
- POST = criar 
- PUT = atualizar
- DELETE = remover


### Exemplo real 

GET    /api/expenses      → listar despesas
POST   /api/expenses      → criar despesa
GET    /api/expenses/{id} → buscar uma
DELETE /api/expenses/{id} → apagar

# Spring Boot — Arquitetura em Camadas

Guia de referência rápida sobre o papel de cada camada num projeto Spring Boot.

---

## Visão Geral

```
Request HTTP
     │
     ▼
┌─────────────┐
│  Controller │  ← recebe e responde requisições HTTP
└──────┬──────┘
       │ chama
       ▼
┌─────────────┐
│   Service   │  ← lógica de negócio e validações
└──────┬──────┘
       │ chama
       ▼
┌─────────────┐
│ Repository  │  ← comunicação com o banco de dados
└──────┬──────┘
       │ acede
       ▼
┌─────────────┐
│   Entity    │  ← representa a tabela no banco
└─────────────┘

(DTO viaja entre Controller ↔ Service para filtrar dados)
```

---

## Camadas

### Controller
**Responsabilidade:** Receber requisições HTTP e devolver respostas.

- Define as rotas (`@GetMapping`, `@PostMapping`, etc.)
- Não contém lógica de negócio — apenas delega para o Service
- Recebe e devolve **DTOs**, nunca Entities diretamente

```java
@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService service;

    @GetMapping
    public List<ExpenseResponseDTO> findAll() {
        return service.findAll();
    }

    @PostMapping
    public ExpenseResponseDTO create(@RequestBody @Valid ExpenseRequestDTO dto) {
        return service.create(dto);
    }
}
```

---

### Service
**Responsabilidade:** Lógica de negócio, validações e orquestração.

- É onde as regras da aplicação vivem
- Chama o Repository para aceder aos dados
- Converte Entities em DTOs (e vice-versa)
- Lida com exceções de negócio

```java
@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public List<ExpenseResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(ExpenseResponseDTO::fromEntity)
                .toList();
    }

    public ExpenseResponseDTO create(ExpenseRequestDTO dto) {
        Expense expense = new Expense(dto.description(), dto.amount());
        return ExpenseResponseDTO.fromEntity(repository.save(expense));
    }
}
```

---

### Repository
**Responsabilidade:** Comunicação com o banco de dados.

- Interface que estende `JpaRepository<Entity, ID>`
- O Spring gera a implementação automaticamente em runtime
- Fornece métodos prontos: `findAll()`, `findById()`, `save()`, `delete()`, etc.
- Permite criar queries customizadas com `@Query` ou por convenção de nome

```java
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Query gerada automaticamente pelo nome do método
    List<Expense> findByCategory(String category);

    // Query JPQL manual
    @Query("SELECT e FROM Expense e WHERE e.amount > :value")
    List<Expense> findExpensiveOnes(@Param("value") BigDecimal value);
}
```

> **Resumo:** O Repository é a ponte entre a aplicação e o banco de dados.  
> O Service não faz SQL — pede ao Repository, que faz isso por baixo usando o Hibernate/JPA.

---

### Entity
**Responsabilidade:** Representar uma tabela do banco de dados como classe Java.

- Anotada com `@Entity`
- Cada campo = coluna na tabela
- O Hibernate usa esta classe para gerar/mapear a tabela

```java
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal amount;
    private String category;
    private LocalDate date;
}
```

---

### DTO (Data Transfer Object)
**Responsabilidade:** Filtrar os dados que entram e saem da API.

- Evita expor campos sensíveis da Entity (ex: passwords, campos internos)
- Permite validar dados de entrada com `@Valid`
- Normalmente há dois tipos: **Request** (entrada) e **Response** (saída)

```java
// Entrada — o que o cliente envia
public record ExpenseRequestDTO(
    @NotBlank String description,
    @NotNull BigDecimal amount,
    String category
) {}

// Saída — o que a API devolve
public record ExpenseResponseDTO(Long id, String description, BigDecimal amount) {
    public static ExpenseResponseDTO fromEntity(Expense e) {
        return new ExpenseResponseDTO(e.getId(), e.getDescription(), e.getAmount());
    }
}
```

---

## Tabela Resumo

| Camada | Anotação principal | Fala com | Responsabilidade |
|---|---|---|---|
| Controller | `@RestController` | Service | Rotas HTTP, entrada/saída |
| Service | `@Service` | Repository | Lógica de negócio |
| Repository | `@Repository` | Entity / DB | Queries ao banco |
| Entity | `@Entity` | — | Mapeamento da tabela |
| DTO | — (record/class) | Controller ↔ Service | Filtrar dados da API |

---

## Fluxo de um POST exemplo `/expenses`

```
1. Controller recebe o JSON → converte para ExpenseRequestDTO
2. Controller chama service.create(dto)
3. Service valida os dados
4. Service converte DTO → Entity
5. Service chama repository.save(entity)
6. Repository faz INSERT no PostgreSQL
7. Repository devolve a Entity salva
8. Service converte Entity → ExpenseResponseDTO
9. Controller devolve o JSON ao cliente
```

