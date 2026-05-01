# 📄 Paginação, Sorting e Filtering com Spring Data

> Devolver tudo de uma vez não é escalável. Este guia mostra como usar `Pageable` e `Page<T>` para construir APIs eficientes com Spring Data.

---

## 🎯 Endpoints Suportados

```http
GET /api/expenses?page=0&size=5&sort=amount,desc
GET /api/expenses?category=Food
GET /api/expenses?category=Food&page=0&size=10&sort=title,asc
```

### Parâmetros disponíveis

| Parâmetro  | Descrição                          | Exemplo         |
|------------|------------------------------------|-----------------|
| `page`     | Número da página (começa em `0`)   | `page=0`        |
| `size`     | Quantidade de itens por página     | `size=10`       |
| `sort`     | Campo e direção de ordenação       | `sort=title,asc`|
| `category` | Filtro simples por categoria       | `category=Food` |

---

## 🧩 Conceitos Fundamentais

### Paginação vs Sorting vs Filtering

```
┌─────────────────────────────────────────────────────────────┐
│  PAGINAÇÃO    →  Divide os resultados em páginas pequenas   │
│  SORTING      →  Ordena por campo (ex: valor, título)       │
│  FILTERING    →  Reduz resultados por critério (ex: Food)   │
└─────────────────────────────────────────────────────────────┘
```

---

## 📦 O que é `Pageable`?

`Pageable` é um objeto que **porta as instruções de paginação**. Ele carrega:

- 📋 Qual página
- 📏 Quantos itens por página
- 🔀 Como ordenar

> **Importante:** O `Pageable` não executa nada sozinho — apenas transporta informação.

### Como o Spring o cria automaticamente

O Spring MVC resolve o `Pageable` diretamente a partir dos query params da request HTTP:

```
GET /api/expenses?page=0&size=10&sort=date,desc
                        ↓
        Pageable pageable  ←  Spring injeta automaticamente no controller
```

```java
@GetMapping("/api/expenses")
public Page<Expense> getExpenses(Pageable pageable) {
    return expenseRepository.findAll(pageable);
}
```

---

## 📃 O que é `Page<T>`?

`Page<T>` **não é uma lista comum**. É um objeto rico que contém:

```
Page<Expense>
├── content          →  Lista de itens da página atual
├── totalElements    →  Total de registos na base de dados
├── totalPages       →  Número total de páginas
├── number           →  Página atual
├── size             →  Tamanho da página
├── first            →  true se for a primeira página
└── last             →  true se for a última página
```

### `List<T>` vs `Page<T>`

| | `List<T>` | `Page<T>` |
|---|---|---|
| Dados | ✅ | ✅ |
| Total de elementos | ❌ | ✅ |
| Total de páginas | ❌ | ✅ |
| Metadados de navegação | ❌ | ✅ |

---

## 🔧 Exemplo Completo

### Repository

```java
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByCategory(String category, Pageable pageable);
}
```

### Controller

```java
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository repository;

    @GetMapping
    public Page<Expense> getExpenses(
            @RequestParam(required = false) String category,
            Pageable pageable) {

        if (category != null) {
            return repository.findByCategory(category, pageable);
        }

        return repository.findAll(pageable);
    }
}
```

### Exemplo de Resposta JSON

```json
{
  "content": [
    { "id": 1, "title": "Almoço", "amount": 12.50, "category": "Food" },
    { "id": 2, "title": "Jantar", "amount": 25.00, "category": "Food" }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 42,
  "totalPages": 5,
  "last": false,
  "first": true
}
```

---

## 🚀 Fluxo Completo

```
HTTP Request
    │
    │  GET /api/expenses?page=0&size=5&sort=amount,desc&category=Food
    ▼
Spring MVC
    │
    │  Resolve Pageable automaticamente a partir dos query params
    ▼
Controller
    │
    │  Passa Pageable + category ao Repository
    ▼
Spring Data JPA
    │
    │  Executa query com LIMIT, OFFSET e ORDER BY
    ▼
Base de Dados
    │
    │  Devolve os registos paginados
    ▼
Page<Expense>
    │
    │  Dados + metadados de paginação
    ▼
JSON Response  →  Cliente
```

---

## ✅ Resumo

- O **Spring resolve `Pageable` automaticamente** a partir dos query params — não precisas fazer nada de especial no controller.
- **`Pageable`** porta as instruções (página, tamanho, ordenação).
- **`Page<T>`** devolve os dados **mais** os metadados de paginação.
- O **filtering** por `category` é feito via `@RequestParam` e passado ao repository.
- Esta abordagem é **escalável**, **flexível** e segue as convenções do ecossistema Spring.