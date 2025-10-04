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
