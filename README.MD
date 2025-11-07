### 🛍️ Teste Mercado Livre
Este projeto é uma aplicação desenvolvida como parte de um teste técnico para o Mercado Livre.

### 📋 Descrição
Aplicação desenvolvida em Kotlin com Spring Boot para gerenciamento de produtos com funcionalidades de busca, filtragem e paginação.

### 🚀 Tecnologias Utilizadas
- Java 17
- Kotlin 1.9.25
- Spring Boot 3.5.5

### Estrutura do Projeto

```cmd
└──src
    └──main
        ├───kotlin
        │   └───raulalvesre
        │       └───testemeli
        │           │
        │           ├───application
        │           │   └───usecase
        │           │       └───dto
        │           │
        │           ├───domain
        │           │   ├───entity
        │           │   ├───enums
        │           │   ├───exception
        │           │   ├───repository
        │           │   └───specification
        │           │
        │           └───infrastructure
        │               ├───api
        │               │   ├───controller
        │               │   ├───exception
        │               │   ├───helper
        │               │   ├───mapper
        │               │   └───response
        │               │
        │               └───persistence
        │                   ├───config
        │                   └───repository
        │
        └───resources
            │   application.properties
            │
            └───data
                    products.json
````

## Endpoints Principais

### 🎯 Buscar Produtos

````text
GET /api/v1/products
````
### Parâmetros:

- page (opcional): Número da página (padrão: 0)
- size (opcional): Tamanho da página (padrão: 10)
- sort (opcional): Campo para ordenação
- search (opcional): Termo para busca

Exemplo:

bash
GET http://localhost:8080/api/products?page=0&size=5&search=smartphone
📊 Estatísticas
text
GET /api/products/statistics
Retorna estatísticas sobre os produtos cadastrados.
