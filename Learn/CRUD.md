# O que é um CRUD ? 

- create: criar
- read: ler
- update: atualizar 
- delete: apagar

#### Our case: 

* POST -> criar despesa
* GET -> ler despesa
* PUT -> atualizar despesa
* DELETE -> apagar despesa

Isto é o núcleo de quase qualquer API.


## Pequena nota: 

- APIs não bastam só devolver dados, devem também responder com significado certo.

Exemplos: 

* criar com sucesso ≥ 201 Created
* buscar com sucesso ≥ 200 OK
* apagar sem devolver corpo ≥ 204 No Content
* id inexistente ≥ 404 Not Found

Backend não é apenas lógica, é também contrato HTTP.