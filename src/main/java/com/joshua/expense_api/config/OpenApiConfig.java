package com.joshua.expense_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Expense API",
                version = "v1",
                description = "API para gestão de despesas pessoais",
                contact = @Contact(
                        name = "Joshua",
                        email = "joshua@example.com"
                )
        )
)
public class OpenApiConfig {}