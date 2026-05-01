Hoje, em projetos Spring Boot com MVC, a forma recomendada é usar springdoc-openapi-starter-webmvc-ui. 

O springdoc gera a descrição OpenAPI automaticamente e expõe a Swagger UI; a documentação indica também os endpoints padrão /swagger-ui.html e /v3/api-docs.

# Open API
- A especificação do contrato da minha API

# Swagger UI
- A interface gráfica que lê essa especificação e a mostra bonita

O springDoc examina os controllers e modelos em runtime e gera documentação OpenAPi em JSON/YAML e uma UI HTMl para exploração da API. 

Documentação de apis servem para 
- frontend perceber como chamar a API
- QA testar sem adivinhar payloads
- eu próprio validar o ocntrato da minha API
- mais tarde gerar cliente ou documentação automática