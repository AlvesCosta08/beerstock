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


🧪 Estratégia de Testes - BeerStock API
📋 Visão Geral dos Testes Implementados
✅ Testes de Controlador (BeerController) - COMPLETO
🎯 Cenários de Sucesso
POST /api/v1/beers - Criar cerveja → 201 Created

GET /api/v1/beers/{id} - Buscar por ID → 200 OK

GET /api/v1/beers/name/{name} - Buscar por nome → 200 OK

GET /api/v1/beers - Listar todas → 200 OK

PUT /api/v1/beers/{id} - Atualizar cerveja → 200 OK

DELETE /api/v1/beers/{id} - Deletar cerveja → 204 No Content

PATCH /api/v1/beers/{id}/increment - Incrementar estoque → 200 OK

PATCH /api/v1/beers/{id}/decrement - Decrementar estoque → 200 OK

❌ Cenários de Erro com Tratamento Global
Validação de DTO inválido → 400 Bad Request

Cerveja duplicada → 409 Conflict

Cerveja não encontrada → 404 Not Found

Estoque excedido → 400 Bad Request


@ControllerAdvice
public class GlobalExceptionHandler {
    // Converte exceções para respostas HTTP apropriadas
}


✅ Testes de Serviço (BeerService) - COMPLETO
🎯 Cenários de Sucesso
Criação de cerveja - Salva nova cerveja com dados válidos

Busca por ID/Nome - Retorna cerveja correspondente

Listagem - Retorna lista com todas as cervejas

Incremento/Decremento - Atualiza estoque dentro dos limites

Atualização - Atualiza dados da cerveja existente

Exclusão - Remove cerveja do sistema

❌ Cenários de Erro
Cerveja duplicada → BeerAlreadyRegisteredException

Estoque excedido → BeerStockExceededException

Cerveja não encontrada → BeerNotFoundException


✅ Testes de Repositório (BeerRepository) - COMPLETO
🎯 Operações CRUD Validadas
save() - Persistência correta no H2

findByName() - Busca por nome com case sensitivity

findAll() - Listagem com e sem registros

findById() - Busca por ID existente e inexistente

deleteById() - Remoção de registros

existsById() - Verificação de existência

count() - Contagem de registros

🔒 Validações de Integridade
Nomes duplicados - Impede cadastro duplicado (DataIntegrityViolationException)

Campos obrigatórios - Valida constraints do banco

Transações - Garante consistência nas operações


🧪 Estratégia de Teste Implementada
1. Testes Unitários - @ExtendWith(MockitoExtension.class)


// Isolamento completo com mocks
@Mock
private BeerRepository beerRepository;

@InjectMocks
private BeerService beerService;


2. Testes de Repositório - @DataJpaTest

// Teste com banco H2 em memória
@DataJpaTest
public class BeerRepositoryTest {
    @Autowired
    private BeerRepository beerRepository;
}

3. Testes de Controlador - MockMvc + @ControllerAdvice

// Teste de endpoints REST com tratamento global de exceções
mockMvc = MockMvcBuilders.standaloneSetup(beerController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();


📊 Cobertura de Testes
✅ Funcionalidades Completamente Testadas
CRUD completo de cervejas

Operações de estoque (incremento/decremento)

Validações de negócio

Tratamento de exceções

Persistência no banco de dados

Endpoints REST        

✅ Camadas de Teste
Camada	Framework	Status
Controller	MockMvc + JUnit 5	✅ COMPLETO
Service	Mockito + JUnit 5	✅ COMPLETO
Repository	@DataJpaTest + H2	✅ COMPLETO


🛠️ Frameworks e Ferramentas Utilizadas

<!-- Testes -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Banco em memória para testes -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>


✅ Frameworks de Teste
JUnit 5 - Estrutura principal de testes

Mockito - Mock de dependências

AssertJ/Hamcrest - Asserções fluentes

Spring Test - Suporte a testes de integração

Spring Data JPA Test - Testes de repositório


🎯 Padrões de Teste Implementados

@Test
void whenValidBeerInformedThenItShouldBeCreated() {
    // Arrange
    BeerDTO beerDTO = createValidBeerDTO();
    
    // Act
    BeerDTO createdBeerDTO = beerService.createBeer(beerDTO);
    
    // Assert
    assertThat(createdBeerDTO.getId(), is(equalTo(VALID_BEER_ID)));
}

2. Given-When-Then (BDD)

@Test
void whenPOSTIsCalledThenABeerIsCreated() throws Exception {
    // Given
    when(beerService.createBeer(validBeerDTO)).thenReturn(validBeerDTO);
    
    // When & Then
    mockMvc.perform(post(BEER_API_URL_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validBeerDTO)))
            .andExpect(status().isCreated());
}

3. Test Data Builders

private BeerDTO createValidBeerDTO() {
    return BeerDTO.builder()
            .id(VALID_BEER_ID)
            .name(BEER_NAME)
            .brand(BEER_BRAND)
            .type(BeerType.LAGER)
            .quantity(10)
            .max(100)
            .build();
}


🔄 Fluxo de Teste Completo
Teste de Integração (Controller → Service → Repository)

HTTP Request 
    → BeerController (MockMvc) 
    → BeerService (Mock) 
    → BeerRepository (Mock/H2)
    → HTTP Response
   

   Cobertura de Status HTTP
Status	Cenário
200 OK	Operações bem-sucedidas
201 Created	Criação de recurso
204 No Content	Exclusão bem-sucedida
400 Bad Request	Validação/estoque excedido
404 Not Found	Recurso não encontrado
409 Conflict	Recurso duplicado


📈 Métricas de Qualidade
✅ Critérios Atendidos
Cobertura completa de funcionalidades principais

Testes isolados com mocks apropriados

Validação de exceções e cenários de erro

Testes de integração com banco real (H2)

Padrões consistentes em todos os testes

Manutenibilidade com builders e constantes


✅ Práticas Recomendadas Implementadas
Nomenclatura clara de testes

Organização Arrange-Act-Assert

Dados de teste reutilizáveis

Verificação de comportamentos com Mockito

Limpeza adequada de recursos (@AfterEach)


🚀 Como Executar os Testes


# Executar todos os testes
./mvnw test

# Executar testes específicos
./mvnw test -Dtest=BeerControllerTest
./mvnw test -Dtest=BeerServiceTest
./mvnw test -Dtest=BeerRepositoryTest

# Executar com relatório de cobertura
./mvnw test jacoco:report


📝 Conclusão
A suite de testes implementada oferece cobertura completa das funcionalidades da BeerStock API, garantindo:

✅ Confiança no código através de testes automatizados

✅ Qualidade com validação de cenários positivos e negativos

✅ Manutenibilidade com padrões consistentes e código limpo

✅ Robustez com tratamento adequado de erros e exceções

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




