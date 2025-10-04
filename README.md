# Beerstock API

> Uma API RESTful para gerenciamento de estoque de cervejas.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Maven](https://img.shields.io/badge/Maven-3.9.x-orange)](https://maven.apache.org/)
[![H2 Database](https://img.shields.io/badge/H2%20DB-In%20Memory-blueviolet)](https://www.h2database.com/)
[![Lombok](https://img.shields.io/badge/Lombok-1.18.30-red)](https://projectlombok.org/)
[![License](https://img.shields.io/github/license/seu-usuario/beerstock)](LICENSE)

---

## 🚀 Descrição

O **Beerstock API** é uma aplicação Spring Boot que fornece endpoints REST para gerenciar estoque de cervejas. A API permite:

- Cadastrar novas cervejas
- Listar, buscar por ID ou nome
- Atualizar informações
- Incrementar ou decrementar estoque
- Validações de limite de estoque e duplicidade

---

## 📋 Requisitos

- Java 17
- Maven 3.6+
- (Opcional) Docker (para banco de dados externo)

---

## 🧪 Tecnologias Utilizadas

- **Spring Boot** (Web, Data JPA, Validation)
- **Spring Web MVC**
- **Spring Data JPA**
- **Hibernate**
- **H2 Database** (em memória)
- **Lombok**
- **ModelMapper** (opcional)
- **Validation API**
- **Swagger UI (OpenAPI 3)**

---

## 📦 Dependências

As dependências principais estão listadas no `pom.xml`:

- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-validation`
- `h2`
- `lombok`
- `modelmapper`
- `springdoc-openapi-starter-webmvc-ui`

---

## 🔧 Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/beerstock.git


## 🧪 Testes

Este projeto inclui testes unitários e de integração para garantir a qualidade, confiabilidade e correta funcionalidade da API.

### 🎯 Cenários de Teste Cobertos

A seguir estão os principais cenários de teste que devem ser executados para validar o comportamento da aplicação.

---

### 1. **Testes de Serviço (`BeerService`)**

#### ✅ Cenários de Sucesso
- **Criação de cerveja**: deve salvar uma nova cerveja com dados válidos.
- **Busca por ID**: deve retornar a cerveja correspondente.
- **Busca por nome**: deve retornar a cerveja correspondente.
- **Listagem de cervejas**: deve retornar uma lista com todas as cervejas cadastradas.
- **Incremento de estoque**: deve atualizar a quantidade de estoque corretamente, dentro do limite.
- **Decremento de estoque**: deve atualizar a quantidade de estoque, sem deixar negativo.
- **Atualização de cerveja**: deve atualizar os dados de uma cerveja existente.

#### ❌ Cenários de Erro
- **Cerveja duplicada**: deve lançar `BeerAlreadyRegisteredException` ao tentar salvar nome duplicado.
- **Incremento acima do limite**: deve lançar `BeerStockExceededException` se exceder o `max`.
- **Decremento acima do disponível**: deve lançar `BeerStockExceededException` se estoque ficar negativo.
- **Busca por cerveja inexistente**: deve lançar `BeerNotFoundException`.

---

### 2. **Testes de Repositório (`BeerRepository`)**

- **Salvar cerveja no banco**: deve persistir corretamente no H2.
- **Buscar cerveja por nome**: deve retornar a cerveja correta.
- **Buscar todas as cervejas**: deve retornar uma lista não vazia (se houver registros).
- **Verificar duplicidade de nome**: deve impedir nomes duplicados no banco de dados.

---

### 3. **Testes de Controlador (`BeerController`)**

- **Retornar status HTTP correto**: ex: `201 Created`, `200 OK`, `400 Bad Request`, `404 Not Found`.
- **Validação de DTO**: deve retornar `400` para campos inválidos.
- **Mapeamento de exceções**: deve converter exceções para respostas HTTP apropriadas (com `@ControllerAdvice`).
- **Autenticação/autorização**: (opcional, se for adicionada no futuro).

---

### 🧪 Estratégia de Teste

- **Testes unitários**: usam `@MockBean` para isolar componentes e testar lógica de negócio.
- **Testes de integração**: usam `@SpringBootTest` para testar o fluxo completo (request → controller → service → repository → response).
- **Testes de repositório**: usam `@DataJpaTest` para validar operações no banco de dados com H2 em memória.

---

### ✅ Frameworks Utilizados

- **JUnit 5**: para escrita dos testes.
- **Mockito**: para simular dependências.
- **AssertJ**: para asserções fluentes.
- **Spring Test**: para testes de integração e mock de beans.

---

### 📌 Como Executar os Testes

No terminal, execute:

```bash
mvn test

Para executar os testes com cobertura:
mvn clean test jacoco:report

O relatório de cobertura será gerado em: target/site/jacoco/

---

### ✅ Dica

Você pode incluir esta seção como parte do `README.md`, ou criar um arquivo separado chamado `TESTING.md` e referenciá-lo no `README.md`:

```markdown
- [Documentação de Testes](TESTING.md)




