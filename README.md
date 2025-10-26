# Desafio Venda XBrain - Sales Management API

## Sobre o Projeto

Este projeto foi desenvolvido como parte do processo seletivo da XBrain, consistindo em uma API REST para gerenciamento de vendas construída com Spring Boot. A aplicação oferece funcionalidades completas para registro de vendas e análise de desempenho de vendedores através de endpoints RESTful bem estruturados.

## Índice

- [Descrição do Desafio](#descrição-do-desafio)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura e Estrutura](#arquitetura-e-estrutura)
- [Funcionalidades Implementadas](#funcionalidades-implementadas)
- [Modelo de Dados](#modelo-de-dados)
- [Endpoints da API](#endpoints-da-api)
- [Validações Implementadas](#validações-implementadas)
- [Testes Automatizados](#testes-automatizados)
- [Como Executar](#como-executar)
- [Decisões Técnicas](#decisões-técnicas)

## Descrição do Desafio

O desafio proposto pela XBrain consistiu em desenvolver um serviço REST capaz de:

1. **Criar uma nova venda** contendo os campos: id, data da venda, valor, id do vendedor e nome do vendedor
2. **Retornar estatísticas de vendedores** incluindo: nome, total de vendas e média de vendas diárias por período

### Requisitos Atendidos

- API desenvolvida com Spring Boot
- Banco de dados em memória H2
- Backend completo com endpoints RESTful
- Histórico de commits detalhado
- Cobertura abrangente de testes automatizados
- Código limpo e bem organizado

## Tecnologias Utilizadas

### Core Framework
- **Java 21**: Linguagem de programação moderna com recursos atualizados
- **Spring Boot 3.5.7**: Framework principal para desenvolvimento da API
- **Spring Data JPA**: Camada de abstração para persistência de dados
- **Hibernate**: Implementação JPA para ORM

### Banco de Dados
- **H2 Database**: Banco de dados em memória para desenvolvimento e testes
- **Console H2**: Interface web para visualização e gerenciamento do banco (disponível em `/h2-console`)

### Validação e Qualidade
- **Jakarta Bean Validation**: Validação declarativa de dados com anotações
- **Lombok**: Redução de código boilerplate através de anotações
- **Apache Commons Lang3**: Utilitários para manipulação de dados

### Testes
- **JUnit 5**: Framework de testes unitários
- **Mockito**: Framework de mocking para testes isolados
- **AssertJ**: Biblioteca para assertions fluentes e expressivas
- **Spring Boot Test**: Testes de integração com contexto completo
- **MockMvc**: Testes de camada web sem servidor

### Build
- **Maven**: Gerenciamento de dependências e build do projeto

## Arquitetura e Estrutura

O projeto segue uma arquitetura em camadas bem definida, promovendo separação de responsabilidades e facilitando manutenção:

```
com.desafiovendaxbrain/
├── SaleChallengeXBrainApplication.java    # Classe principal Spring Boot
├── controller/                             # Camada de apresentação (REST)
│   └── SaleController.java
├── service/                                # Camada de lógica de negócio
│   └── SaleService.java
├── repository/                             # Camada de acesso a dados
│   ├── SaleRepository.java
│   └── projection/
│       └── SellerProjection.java
├── model/                                  # Entidades JPA
│   └── Sale.java
└── dto/                                    # Objetos de transferência de dados
    ├── SaleDTO.java
    └── SellerDTO.java
```

### Estrutura de Testes

```
src/test/java/com/desafiovendaxbrain/
├── controller/
│   └── SaleControllerIT.java              # Testes de integração da API
├── service/
│   └── SaleServiceTest.java               # Testes unitários do serviço
├── repository/
│   └── SaleRepositoryIT.java              # Testes de integração do repositório
└── utils/                                  # Utilitários de teste
    ├── SaleFactory.java                    # Factory para entidades de teste
    └── SaleDTOFactory.java                 # Factory para DTOs de teste
```

## Funcionalidades Implementadas

### 1. Registro de Vendas

O sistema permite criar novas vendas através de um endpoint REST, garantindo a integridade dos dados através de validações robustas:

- Validação de data (não permite datas futuras)
- Validação de valores (apenas valores positivos)
- Validação de campos obrigatórios
- Persistência automática no banco de dados
- Retorno do registro criado com ID gerado

### 2. Análise de Desempenho de Vendedores

O sistema oferece consulta de estatísticas agregadas por vendedor em um período específico:

- **Total de vendas**: Quantidade total de vendas realizadas por cada vendedor
- **Média de vendas diárias**: Valor médio de vendas por dia calculado automaticamente
- **Filtro por período**: Permite especificar data inicial e final (data final opcional, usando momento atual como padrão)
- **Agrupamento por vendedor**: Resultados organizados por nome do vendedor

## Modelo de Dados

### Entidade Sale (Venda)

```java
@Entity
@Table(name = "tb_venda")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant saleDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    private Long sellerId;

    @Column(length = 100)
    private String sellerName;
}
```

**Características técnicas:**
- Uso de `Instant` para timestamps agnósticos a timezone
- `BigDecimal` com precisão controlada (10,2) para valores monetários
- ID auto-incrementado pelo banco de dados
- Informações do vendedor desnormalizadas para performance em consultas

### DTOs (Data Transfer Objects)

#### SaleDTO
Record Java imutável com validações declarativas:

```java
public record SaleDTO (
    Long id,
    @NotNull @PastOrPresent Instant saleDate,
    @Positive @NotNull BigDecimal amount,
    @NotNull Long sellerId,
    @NotBlank @Size(max = 100) String sellerName
) {}
```

#### SellerDTO
Record para retorno de estatísticas agregadas:

```java
public record SellerDTO (
    String sellerName,
    Long totalSales,
    Double averageDailySales
) {}
```

## Endpoints da API

### Base URL
```
http://localhost:8080/api/sales
```

### 1. Criar Nova Venda

**Endpoint:** `POST /api/sales`

**Request Body:**
```json
{
    "saleDate": "2025-10-26T10:30:00Z",
    "amount": 1500.00,
    "sellerId": 1,
    "sellerName": "João Silva"
}
```

**Response:** `201 Created`
```json
{
    "id": 1,
    "saleDate": "2025-10-26T10:30:00Z",
    "amount": 1500.00,
    "sellerId": 1,
    "sellerName": "João Silva"
}
```

**Possíveis erros:**
- `400 Bad Request`: Dados inválidos (violação de validação)

### 2. Consultar Estatísticas de Vendedores

**Endpoint:** `GET /api/sales`

**Parâmetros de Query:**
- `start` (obrigatório): Data/hora inicial do período (formato ISO-8601)
- `end` (opcional): Data/hora final do período (padrão: agora)

**Exemplo de Request:**
```
GET /api/sales?start=2025-10-01T00:00:00Z&end=2025-10-31T23:59:59Z
```

**Response:** `200 OK`
```json
[
    {
        "sellerName": "João Silva",
        "totalSales": 15,
        "averageDailySales": 2500.00
    },
    {
        "sellerName": "Maria Santos",
        "totalSales": 12,
        "averageDailySales": 1800.50
    }
]
```

**Possíveis erros:**
- `404 Not Found`: Nenhuma venda encontrada no período
- `500 Internal Server Error`: Período inválido (data final anterior à inicial)

## Validações Implementadas

### Validações de Campo

| Campo | Validações | Mensagem de Erro |
|-------|-----------|------------------|
| `saleDate` | `@NotNull`, `@PastOrPresent` | "A data da venda não pode ser nula" / "A data da venda não pode ser futura" |
| `amount` | `@NotNull`, `@Positive` | "O valor da venda não pode ser nulo" / "O valor da venda deve ser positivo" |
| `sellerId` | `@NotNull` | "O ID do vendedor não pode ser nulo" |
| `sellerName` | `@NotBlank`, `@Size(max=100)` | "O nome do vendedor não pode estar em branco" / "O nome do vendedor não pode exceder 100 caracteres" |

### Validações de Lógica de Negócio

- **Período de consulta válido**: A data final não pode ser anterior à data inicial
- **Existência de dados**: Retorna erro apropriado quando não há vendas no período consultado
- **Cálculo de média**: Garante que o período tenha duração positiva para cálculo correto

## Testes Automatizados

O projeto possui cobertura abrangente de testes em múltiplas camadas, garantindo qualidade e confiabilidade do código.

### Testes Unitários

#### SaleServiceTest (`src/test/java/.../service/SaleServiceTest.java`)

**Cobertura de 7 cenários:**

1. **Criação de venda válida**: Verifica que vendas válidas são persistidas corretamente com ID gerado
2. **Propagação de exceções**: Garante que exceções do repositório são propagadas corretamente
3. **Estatísticas com dados válidos**: Testa cálculo correto de estatísticas com múltiplos vendedores
4. **Data final padrão**: Verifica que `null` na data final usa o momento atual como padrão
5. **Período de zero dias**: Testa tratamento de edge case onde início e fim são idênticos
6. **Sem vendas no período**: Garante lançamento de `NoSuchElementException` quando não há dados
7. **Período negativo**: Valida que período com data final anterior à inicial lança `ArithmeticException`

**Técnicas utilizadas:**
- Mocking com Mockito para isolamento de dependências
- Assertions fluentes com AssertJ
- Padrão AAA (Arrange-Act-Assert)

### Testes de Integração

#### SaleRepositoryIT (`src/test/java/.../repository/SaleRepositoryIT.java`)

**Cobertura de 3 cenários:**

1. **Consulta de estatísticas por período**: Valida query JPQL customizada com múltiplos vendedores
2. **Resultado vazio**: Testa comportamento quando não há vendas no período
3. **Filtro de período correto**: Garante que apenas vendas dentro do período são consideradas

**Técnicas utilizadas:**
- `@DataJpaTest` para contexto JPA isolado
- Banco H2 em memória para testes
- Fixtures com dados realistas

#### SaleControllerIT (`src/test/java/.../controller/SaleControllerIT.java`)

**Cobertura de 8 cenários:**

1. **Criação bem-sucedida**: Testa POST com dados válidos retorna 201 Created
2. **Nome muito longo**: Valida rejeição de nomes com mais de 100 caracteres
3. **Nome em branco**: Verifica rejeição de nome vazio ou apenas espaços
4. **Data futura**: Garante rejeição de vendas com data no futuro
5. **Data nula**: Testa validação de campo obrigatório
6. **Valor zero ou negativo**: Valida que apenas valores positivos são aceitos
7. **Valor nulo**: Verifica rejeição quando valor não é informado
8. **ID de vendedor nulo**: Testa validação de campo obrigatório

**Técnicas utilizadas:**
- `@SpringBootTest` com contexto completo da aplicação
- `@AutoConfigureMockMvc` para testes de endpoints REST
- Jackson para serialização/deserialização JSON
- Validação de status codes HTTP e corpo das respostas

### Utilitários de Teste

#### Factory Pattern

Implementação de factories para criação consistente de objetos de teste:

- **SaleFactory**: Cria instâncias de `Sale` válidas e inválidas
- **SaleDTOFactory**: Cria instâncias de `SaleDTO` para testes de API

**Benefícios:**
- Redução de duplicação de código nos testes
- Manutenção centralizada de dados de teste
- Facilidade para criar cenários complexos

## Como Executar

### Pré-requisitos

- Java 21 ou superior
- Maven 3.6+

### Passos para Execução

1. **Clone o repositório:**
```bash
git clone <url-do-repositorio>
cd desafio-venda-xbrain
```

2. **Compile o projeto:**
```bash
./mvnw clean install
```

3. **Execute a aplicação:**
```bash
./mvnw spring-boot:run
```

4. **Acesse a aplicação:**
- API: `http://localhost:8080/api/sales`
- Console H2: `http://localhost:8080/h2-console` (veja seção Docker para detalhes)
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (deixe em branco)

### Executando com Docker

O projeto inclui um Dockerfile para facilitar o deploy em containers.

**1. Build do JAR:**
```bash
./mvnw clean package -DskipTests
```

**2. Copie o JAR para o diretório raiz:**
```bash
cp target/desafio-venda-xbrain-0.0.1-SNAPSHOT.jar .
```

**3. Build da imagem Docker:**
```bash
docker build -t desafio-venda-xbrain .
```

**4. Execute o container:**
```bash
docker run -p 8080:8080 desafio-venda-xbrain
```

**5. Acesse a aplicação:**
- API: `http://localhost:8080/api/sales`
- Console H2: `http://localhost:8080/h2-console`

### Configuração do H2 Console

O projeto inclui uma configuração especial (`H2ConsoleConfig`) que permite acesso remoto ao console H2, essencial para ambientes Docker.

**Acessando o H2 Console:**

Quando acessar `http://localhost:8080/h2-console`, use as seguintes configurações de conexão:

| Campo | Valor |
|-------|-------|
| **Saved Settings** | Generic H2 (Embedded) |
| **Driver Class** | `org.h2.Driver` |
| **JDBC URL** | `jdbc:h2:mem:testdb` |
| **User Name** | `sa` |
| **Password** | (deixe em branco) |

**Importante:**
- O banco de dados H2 é **in-memory**, existindo apenas enquanto a aplicação estiver rodando
- A JDBC URL **deve ser exatamente** `jdbc:h2:mem:testdb` (não use caminhos de arquivo como `~/test`)
- Certifique-se de que a aplicação Spring Boot está rodando antes de conectar

### Executando os Testes

**Todos os testes:**
```bash
./mvnw test
```

**Apenas testes unitários:**
```bash
./mvnw test -Dtest=*Test
```

**Apenas testes de integração:**
```bash
./mvnw test -Dtest=*IT
```

**Com relatório de cobertura:**
```bash
./mvnw clean test jacoco:report
```

## Decisões Técnicas

### 1. Uso de Java Records para DTOs

**Decisão:** Utilizar Java Records em vez de classes tradicionais para DTOs.

**Justificativa:**
- Imutabilidade por padrão, aumentando segurança thread-safe
- Código mais conciso e legível
- Equals, hashCode e toString gerados automaticamente
- Semântica clara de "valor" em vez de "objeto"

### 2. Instant para Timestamps

**Decisão:** Usar `java.time.Instant` em vez de `Date` ou `LocalDateTime`.

**Justificativa:**
- Agnóstico a timezone, evitando problemas de conversão
- Padrão ISO-8601 para interoperabilidade
- API moderna e mais segura do Java Time
- Precisão em nanosegundos quando necessário

### 3. BigDecimal para Valores Monetários

**Decisão:** Utilizar `BigDecimal` com precisão definida (10,2) para valores monetários.

**Justificativa:**
- Evita problemas de arredondamento de ponto flutuante
- Precisão decimal exata para cálculos financeiros
- Controle explícito de escala e arredondamento
- Padrão da indústria para aplicações financeiras

### 4. Validação Declarativa com Bean Validation

**Decisão:** Usar anotações Jakarta Bean Validation em vez de validação manual.

**Justificativa:**
- Código mais limpo e autodocumentado
- Validações centralizadas no modelo de dados
- Mensagens de erro consistentes e customizáveis
- Integração nativa com Spring MVC
- Facilita manutenção e evolução das regras

### 5. Arquitetura em Camadas

**Decisão:** Separar responsabilidades em Controller, Service e Repository.

**Justificativa:**
- Separação clara de responsabilidades (SRP)
- Facilita testes unitários e de integração
- Permite reutilização de lógica de negócio
- Manutenibilidade e escalabilidade
- Padrão amplamente reconhecido na comunidade

### 6. Query JPQL Customizada com Projection

**Decisão:** Usar query JPQL com interface projection para estatísticas.

**Justificativa:**
- Performance: apenas campos necessários são buscados
- Agregação realizada no banco de dados (mais eficiente)
- Type-safe através de interface projection
- Facilita manutenção da query
- Evita N+1 queries

### 7. H2 Database em Memória

**Decisão:** Utilizar H2 em modo in-memory para desenvolvimento e testes.

**Justificativa:**
- Cumprimento do requisito do desafio
- Setup zero - não requer instalação externa
- Rápido para testes
- Schema criado automaticamente via JPA
- Console web integrado para debug
- Facilita onboarding de novos desenvolvedores

### 8. Factory Pattern nos Testes

**Decisão:** Implementar factories para criação de objetos de teste.

**Justificativa:**
- DRY (Don't Repeat Yourself) nos testes
- Manutenção centralizada de dados de teste
- Facilita criação de cenários complexos
- Melhora legibilidade dos testes
- Reduz acoplamento entre testes

### 9. Testes em Múltiplas Camadas

**Decisão:** Implementar testes unitários, de integração de repositório e de API.

**Justificativa:**
- Cobertura abrangente de cenários
- Testes unitários rápidos para TDD
- Testes de integração validam comportamento real
- Detecta problemas em diferentes níveis
- Aumenta confiança nas refatorações

### 10. Mensagens de Erro Descritivas

**Decisão:** Customizar mensagens de validação em português.

**Justificativa:**
- Melhor experiência para desenvolvedores brasileiros
- Facilita debugging
- Mensagens claras sobre o que está errado
- Alinhamento com requisitos de negócio locais

## Estrutura de Commits

O projeto mantém histórico de commits organizado e descritivo, seguindo convenções:

```
feat: adiciona funcionalidade X
fix: corrige problema Y
```
**Desenvolvido com dedicação utilizando Spring Boot, Java 21 e boas práticas de engenharia de software.**