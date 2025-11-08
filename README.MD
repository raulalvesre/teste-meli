## 🛍️ Teste Mercado Livre

Este projeto implementa uma API de busca e comparação de produtos como parte de um teste técnico do Mercado Livre.

O foco principal é:

- Implementar filtros flexíveis para busca de produtos.
- Suportar ordenação por múltiplos campos (**multi-sort**).
- Organizar o código seguindo princípios de **DDD** e separação de camadas (domínio, aplicação e infraestrutura).

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

### 🕵️ Buscar Produto
````text
GET /v1/{productId}
````

Busca um produto pelo seu identificador único.

### Parâmetros de path

| Nome        | Tipo | Obrigatório | Descrição                       |
|-------------|------|-------------|---------------------------------|
| `productId` | Long | sim         | ID do produto a ser consultado. |

### Respostas

| Código | Descrição                                |
|--------|------------------------------------------|
| `200`  | Produto encontrado com sucesso.          |
| `404`  | Nenhum produto encontrado com esse ID.   |

### 🎯 Buscar Produtos

````text
GET /v1/products
````

Retorna uma lista paginada de produtos filtrados e ordenados de acordo com os parâmetros de consulta.

### Parâmetros de query

#### Filtros

| Nome              | Tipo       | Obrigatório | Descrição                                                                         |
|-------------------|------------|-------------|-----------------------------------------------------------------------------------|
| `name`            | String     | não         | Filtra produtos cujo **nome contenha** o texto informado (case-insensitive).      |
| `category`        | String     | não         | Filtra produtos cujo **categoria contenha** o texto informado (case-insensitive). |
| `brand`           | String     | não         | Filtra produtos cujo **marca contenha** o texto informado (case-insensitive).     |
| `condition`       | String     | não         | Filtra pela condição do produto (ex.: `NEW`, `USED`).                             |
| `minPrice`        | BigDecimal | não         | Preço mínimo (inclusivo).                                                         |
| `maxPrice`        | BigDecimal | não         | Preço máximo (inclusivo).                                                         |
| `minRating`       | Long       | não         | Nota mínima (inclusivo).                                                          |
| `maxRating`       | Long       | não         | Nota máxima (inclusivo).                                                          |
| `hasFreeShipping` | Boolean    | não         | Se `true`, retorna apenas produtos com frete grátis.                              |
| `isAvailable`     | Boolean    | não         | Se `true`, retorna apenas produtos disponíveis.                                   |


Todos os filtros são opcionais. Quando um parâmetro não é enviado, ele simplesmente não é considerado no filtro.

#### Paginação

| Nome   | Tipo | Obrigatório | Padrão | Descrição                                |
|--------|------|-------------|--------|------------------------------------------|
| `page` | Int  | não         | `0`    | Índice da página (**baseado em zero**).  |
| `size` | Int  | não         | `10`   | Quantidade de itens por página.          |

#### Ordenação

A ordenação é feita usando dois arrays paralelos:

| Nome        | Tipo          | Obrigatório | Descrição                                                                 |
|-------------|---------------|-------------|---------------------------------------------------------------------------|
| `sortBy`    | List\<String> | não         | Lista de campos para ordenação, em ordem de prioridade (ex.: `NAME`, `PRICE`, `RATING`, `BRAND`). |
| `direction` | List\<String> | não         | Direção da ordenação para cada campo (`ASC` ou `DESC`), casada **por índice** com `sortBy`. |

**Regras:**

- Caso`sortBy` tenha mais elementos do que `direction`, os campos excedentes em sortBy serão ordenados com direção padrão ASC.

**Exemplo:**

```text
GET /v1/products?name=iphone&minPrice=2000&sortBy=PRICE&sortBy=RATING&direction=DESC&direction=ASC
````

Esse request vai:

- Filtrar produtos cujo nome contenha "iphone";
- Filtrar produtos com preço ≥ 2000;
- Ordenar primeiro por PRICE em ordem decrescente;
- Usar RATING em ordem crescente como critério de desempate.

## Documentação Swagger

A documentação dos endpoints pode ser acessada em:

http://localhost:8080/swagger-ui/index.html

## 🤔 Decisões de projeto

### Arquitetura

O projeto segue uma arquitetura em camadas inspirada em Clean Architecture / DDD, mas aplicada de forma pragmática, sem purismo. Tomei essa decisão priorizando clareza, simplicidade e velocidade de desenvolvimento.

A organização das camadas busca deixar explícito:

- `domain` – onde vivem as regras de negócio e o modelo de domínio;
- `application` – onde estão os casos de uso e a orquestração do fluxo da aplicação;
- `infrastructure` – onde lidamos com frameworks, detalhes técnicos e integrações externas.

### Persistência

- **Sem banco de dados**  
  Os produtos são carregados a partir do `src/main/resources/data/products.json` durante a inicialização da aplicação.

Essa abordagem simplifica o setup (basta subir a aplicação) e mantém o foco nas regras de negócio, sem a complexidade extra de configurar um banco de dados.


### Filtros de busca

Os filtros de busca foram implementados usando o **Specification Pattern**.

- Cada critério de filtro (nome, categoria, marca, condição, faixa de preço, rating, frete grátis, disponibilidade, etc.) é representado por uma implementação de `Specification<Product>`  
  (por exemplo: `NameSpecification`, `CategorySpecification`, `PriceRangeSpecification`).
- A função `ProductFilter.toSpecification()` compõe essas specifications em cadeia usando `.and(...)`, gerando uma única specification final a partir dos parâmetros informados.
- Quando algum campo do `ProductFilter` é nulo, a specification correspondente se comporta como “neutra” (não restringe o resultado), permitindo montar o filtro dinamicamente sem `ifs` espalhados.

### Ordenação

- **Multi-sort inspirado no DBeaver e Grafana**  
  A ordenação da lista de produtos suporta **múltiplos campos**:
    - A API recebe arrays `sortBy` e `direction`.
    - Cada posição de `sortBy` corresponde à mesma posição em `direction`.
    - Quando há mais campos em `sortBy` do que em `direction`, os campos excedentes usam direção padrão `ASC`.

A ideia veio da experiência com ferramentas como o **DBeaver** e **Grafana**, que permitem definir multi-sort na UI, e quis reproduzir esse comportamento no backend como um exercício de implementação.

### Testes unitários

A aplicação não possui **100% de cobertura**, por escolha. Preferi focar nos pontos mais relevantes do fluxo principal de busca de produtos.

Atualmente, existem testes cobrindo principalmente:

- `ProductService`
    - Orquestração do fluxo de busca de produtos.
    - Chamada do repositório com os parâmetros corretos.

- `JsonProductRepository`
    - Aplicação dos filtros de busca.
    - Lógica de ordenação, incluindo **multi-sort**.

- `Specifications de filtro** (domain.specification)`
    - Cada critério de filtro individual (nome, categoria, faixa de preço, rating, frete grátis, etc.).
    - Composição das specifications para montar o filtro final.

A ideia foi garantir uma boa cobertura do **fluxo principal de consulta**, que é o foco do desafio, em vez de buscar 100% de cobertura em código periférico.

## 👷🏻‍♂️ Como rodar o projeto

### Pré-requisitos

- **Java 17+**

### Passos

```bash
```bash
# Clonar o repositório
git clone <URL_DO_REPOSITORIO>
cd testemeli

# Rodar a aplicação na porta que preferir, no caso estaremos utilizando a 8080
./gradlew bootRun --args='--server.port=8080'
````
Após subir, a API estará disponível em `http://localhost:8080`.

Recomendo utilizar o swagger para explorar e interagir com a API:
http://localhost:8080/swagger-ui/index.html


### Prompts
