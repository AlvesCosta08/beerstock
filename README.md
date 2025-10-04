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



🍺 BeerStock API
Uma API RESTful completa para gerenciamento de estoque de cervejas com suíte de testes robusta.

https://img.shields.io/badge/Spring%2520Boot-3.2.0-brightgreen
https://img.shields.io/badge/Java-17-blue
https://img.shields.io/badge/Maven-3.9.x-orange
https://img.shields.io/badge/H2%2520DB-Test%2520%2526%2520Dev-blueviolet
https://img.shields.io/badge/Test%2520Coverage-100%2525-success
https://img.shields.io/badge/License-MIT-green

📖 Índice
🚀 Descrição

🎯 Funcionalidades

🛠️ Tecnologias

📦 Instalação

🔧 Configuração

📚 API Reference

🧪 Testes

🏗️ Arquitetura

🤝 Contribuição

📄 Licença

🚀 Descrição
O BeerStock API é uma aplicação Spring Boot robusta que fornece endpoints RESTful para gerenciamento completo de estoque de cervejas. Desenvolvida seguindo as melhores práticas de desenvolvimento de software, incluindo testes automatizados abrangentes e tratamento elegante de erros.

🎯 Funcionalidades
✅ Gestão de Cervejas
📝 CRUD Completo - Criar, ler, atualizar e deletar cervejas

🔍 Buscas Avançadas - Por ID, nome ou listagem completa

📊 Controle de Estoque - Incremento e decremento com validações

⚡ Validações - Dados obrigatórios, limites de estoque, duplicidades

✅ Características Técnicas
🛡️ Tratamento de Erros - Respostas HTTP padronizadas

📋 Documentação Automática - Swagger/OpenAPI integrado

🧪 Testes Abrangentes - 100% de cobertura das funcionalidades

🗄️ Persistência - Banco H2 em memória (testes) e configuravel para produção

🛠️ Tecnologias
Backend & Framework
https://img.shields.io/badge/Spring%2520Boot-3.2.0-6DB33F?logo=springboot - Framework principal

https://img.shields.io/badge/Java-17-007396?logo=java - Linguagem de programação

https://img.shields.io/badge/Maven-3.9.x-C71A36?logo=apache-maven - Gerenciamento de dependências

Banco de Dados & ORM
https://img.shields.io/badge/H2%2520Database-2.2.224-4479A1?logo=h2 - Banco em memória

https://img.shields.io/badge/Spring%2520Data%2520JPA-3.2.0-6DB33F - Persistência de dados

https://img.shields.io/badge/Hibernate-6.3.1-59666C?logo=hibernate - ORM

Documentação & Testes
https://img.shields.io/badge/Swagger%2520UI-5.2.0-85EA2D?logo=swagger - Documentação interativa

https://img.shields.io/badge/JUnit%25205-5.10.1-25A162?logo=junit5 - Framework de testes

https://img.shields.io/badge/Mockito-5.7.0-78A641 - Mocking para testes

Ferramentas de Desenvolvimento
https://img.shields.io/badge/Lombok-1.18.30-red?logo=lombok - Redução de boilerplate

https://img.shields.io/badge/ModelMapper-3.1.1-blue - Mapeamento DTO/Entity

https://img.shields.io/badge/Validation%2520API-3.0.2-orange - Validações de dados

📦 Instalação
Pré-requisitos
Java 17 ou superior

Maven 3.6+

Git

Clone e Execução

# 1. Clone o repositório
git clone https://github.com/seu-usuario/beerstock.git
cd beerstock

# 2. Execute a aplicação
./mvnw spring-boot:run

# Ou compile e execute
./mvnw clean package
java -jar target/beerstock-0.0.1-SNAPSHOT.jar
Docker (Opcional)

# Build da imagem Docker
docker build -t beerstock-api .

# Executar container
docker run -p 8080:8080 beerstock-api
🔧 Configuração
Arquivo application.properties
properties

# Server Configuration
server.port=8080
spring.application.name=beerstock-api

# Database Configuration (H2 - Development)
spring.datasource.url=jdbc:h2:mem:beerstockdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console (Access: http://localhost:8080/h2-console)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Swagger Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
Variáveis de Ambiente
bash
export SERVER_PORT=8080
export DB_URL=jdbc:h2:mem:beerstockdb
export DB_USERNAME=sa
export DB_PASSWORD=
📚 API Reference

# Base URL

```
http://localhost:8080/api/v1

📋 Endpoints Principais

Cervejas (/beers)
Método	Endpoint	Descrição	Status de Sucesso
POST	/beers	Criar nova cerveja	201 Created
GET	/beers	Listar todas as cervejas	200 OK
GET	/beers/{id}	Buscar cerveja por ID	200 OK
GET	/beers/name/{name}	Buscar cerveja por nome	200 OK
PUT	/beers/{id}	Atualizar cerveja	200 OK
DELETE	/beers/{id}	Deletar cerveja	204 No Content
PATCH	/beers/{id}/increment	Incrementar estoque	200 OK
PATCH	/beers/{id}/decrement	Decrementar estoque	200 OK

📝 Exemplos de Uso
Criar Cerveja



POST /api/v1/beers
Content-Type: application/json

{
  "name": "Heineken",
  "brand": "Heineken",
  "max": 100,
  "quantity": 50,
  "type": "LAGER"
}
Resposta:

json
{
  "id": 1,
  "name": "Heineken",
  "brand": "Heineken",
  "max": 100,
  "quantity": 50,
  "type": "LAGER"
}

Incrementar Estoque

PATCH /api/v1/beers/1/increment?quantityToIncrement=10

🔄 Códigos de Status HTTP

Status	Descrição
200 OK	Requisição bem-sucedida
201 Created	Recurso criado com sucesso
204 No Content	Recurso deletado com sucesso
400 Bad Request	Dados inválidos ou estoque excedido
404 Not Found	Cerveja não encontrada
409 Conflict	Cerveja já cadastrada

🧪 Testes

📊 Estratégia de Testes

A aplicação possui cobertura completa de testes em todas as camadas:

✅ Testes de Controlador (BeerController)
Cenários de Sucesso: Todos os endpoints REST

Tratamento de Erros: Validações e exceções mapeadas

Status HTTP: Respostas apropriadas para cada cenário

✅ Testes de Serviço (BeerService)
Lógica de Negócio: Regras de estoque e validações

Casos de Erro: Exceções específicas do domínio

Integração: Comunicação com repositório

✅ Testes de Repositório (BeerRepository)
Operações CRUD: Persistência no banco H2

Consultas: Buscas por nome, ID e listagens

Integridade: Validações de constraints

🚀 Executando os Testes


# Executar todos os testes
./mvnw test

# Executar testes com relatório de cobertura
./mvnw clean test jacoco:report

# Executar testes específicos
./mvnw test -Dtest=BeerControllerTest
./mvnw test -Dtest=BeerServiceTest
./mvnw test -Dtest=BeerRepositoryTest

# Executar testes de integração

./mvnw test -Dtest="*IntegrationTest"


📈 Relatórios de Cobertura
Após executar os testes, os relatórios estarão disponíveis em:

Jacoco: target/site/jacoco/index.html

Surefire: target/surefire-reports/

🏗️ Arquitetura
📐 Padrão Arquitetural


```
┌─────────────────┐    ┌──────────────────┐    ┌──────────────────┐
│   Controller    │ →  │     Service      │ →  │   Repository     │
│   (REST API)    │    │  (Business Logic)│    │   (Data Access)  │
└─────────────────┘    └──────────────────┘    └──────────────────┘
         ↓                       ↓                       ↓
┌─────────────────┐    ┌──────────────────┐    ┌──────────────────┐
│      DTOs       │    │    Entities      │    │   Database       │
│ (Data Transfer) │    │  (Domain Model)  │    │    (H2/Prod)     │
└─────────────────┘    └──────────────────┘    └──────────────────┘




🎯 Camadas da Aplicação
Controller Layer: Endpoints REST e tratamento de requests

Service Layer: Lógica de negócio e regras de domínio

Repository Layer: Acesso a dados e operações de persistência

Model Layer: Entidades de domínio e DTOs

🔗 Dependências entre Camadas
text
Controller → Service → Repository → Database
    ↓          ↓          ↓
   DTOs     Entities   Entities
🤝 Contribuição
Contribuições são sempre bem-vindas! Para contribuir:

Fork o projeto

Crie uma branch para sua feature (git checkout -b feature/AmazingFeature)

Commit suas mudanças (git commit -m 'Add some AmazingFeature')

Push para a branch (git push origin feature/AmazingFeature)

Abra um Pull Request

📋 Guidelines de Contribuição
Siga o padrão de código existente

Adicione testes para novas funcionalidades

Atualize a documentação quando necessário

Use mensagens de commit descritivas

📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para detalhes.

👥 Autores
Seu Nome - Desenvolvimento inicial - seu-usuario

🙏 Agradecimentos
Equipe Spring Boot pelo excelente framework

Comunidade Java por todas as contribuições

Todos os contribuidores que ajudaram no projeto

📞 Suporte
Se você tiver qualquer dúvida ou problema, sinta-se à vontade para:

📧 Email: seu.email@example.com

🐛 Issues: GitHub Issues

💬 Discussions: GitHub Discussions



