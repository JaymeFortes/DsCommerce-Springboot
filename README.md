# DsCommerce - E-Commerce Backend API

Um projeto de backend de e-commerce desenvolvido com **Spring Boot 3.1.0** e **Java 21**, implementando uma API REST com segurança OAuth2, autenticação JWT, e padrão de camadas com validações robustas.

## 📋 Tecnologias Utilizadas

### Core Framework
- **Spring Boot 3.1.0** - Framework web e data access
- **Java 21** - Linguagem de programação
- **Maven** - Gerenciador de dependências e build

### Segurança & Autenticação
- **Spring Security** - Autenticação e autorização
- **OAuth2 Authorization Server** - Servidor de autorização OAuth2
- **OAuth2 Resource Server** - Servidor de recursos
- **JWT (JSON Web Tokens)** - Tokens de autenticação

### Persistência de Dados
- **Spring Data JPA** - ORM e acesso a dados
- **Hibernate Validator** - Validação de dados
- **H2 Database** - Banco de dados em memória (desenvolvimento/testes)

### Testes & Qualidade de Código
- **JUnit 5** - Framework de testes unitários
- **Spring Boot Test** - Testes de integração
- **Spring Security Test** - Testes de segurança
- **JaCoCo** - Cobertura de testes (relatórios gerados em `target/jacoco-report/`)

## 🏗️ Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/DsCommerce/
│   │   ├── config/              # Configurações da aplicação (Spring Security, OAuth2)
│   │   ├── controlls/           # Controllers REST
│   │   ├── service/             # Lógica de negócio
│   │   ├── repository/          # Acesso a dados (JPA)
│   │   ├── entities/            # Entidades JPA
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── enums/               # Enumerações
│   │   ├── exceptions/          # Exceções customizadas
│   │   ├── projections/         # Projeções JPA
│   │   ├── util/                # Utilitários
│   │   └── DsCommerceApplication.java
│   └── resources/
│       ├── application.properties       # Configurações padrão
│       ├── application-test.properties  # Configurações de testes
│       └── import.sql                   # Script de inicialização do banco
└── test/
    └── java/com/DsCommerce/
        └── services/            # Testes unitários dos serviços
```

## 🚀 Como Executar

### Pré-requisitos
- Java 21 ou superior
- Maven 3.6.0 ou superior
- Git

### 1. Clonar o Repositório
```bash
git clone <URL_DO_REPOSITORIO>
cd DsCommerce-Springboot
```

### 2. Compilar o Projeto
```bash
mvn clean compile
```

### 3. Executar a Aplicação
```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

### 4. Executar os Testes
```bash
mvn test
```

### 5. Gerar Relatório de Cobertura de Testes
```bash
mvn clean test
```
O relatório será gerado em `target/jacoco-report/index.html`

## 📦 Endpoints Principais

A API oferece os seguintes grupos de endpoints:

- **CategoryController** - Gerenciamento de categorias de produtos
- **ProductController** - Gerenciamento de produtos
- **OrderController** - Gerenciamento de pedidos
- **UserController** - Gerenciamento de usuários
- **AuthController** - Autenticação e autorização

> Para detalhes completos dos endpoints, consulte a documentação gerada ou execute a aplicação.

## 🔒 Segurança

### Autenticação
- Autenticação via OAuth2 com JWT
- Validação de credenciais no AuthService

### Autorização
- Controle de acesso baseado em roles
- Proteção de recursos com Spring Security

### Validação
- Validação de dados com Hibernate Validator
- Conformidade com Jakarta Validation API 3.0.2

## 🧪 Testes

O projeto inclui testes unitários e integração para os principais serviços:

- `AuthServiceTests` - Testes de autenticação
- `UserServiceTests` - Testes de usuários
- `ProductServiceTests` - Testes de produtos
- `CategoryServiceTests` - Testes de categorias
- `OrderServiceTests` - Testes de pedidos

Executar todos os testes:
```bash
mvn clean test
```

Executar testes com cobertura:
```bash
mvn clean test jacoco:report
```

## 📊 Cobertura de Testes

O projeto utiliza **JaCoCo** para medir a cobertura de código. As seguintes classes estão excluídas do relatório:

- `DsCommerceApplication.class` - Classe principal
- `config/**` - Classes de configuração
- `entities/**` - Entidades
- `dto/**` - DTOs
- `controlls/handlers/**` - Handlers de exceção
- `exceptions/**` - Exceções customizadas
- `enums/**` - Enumerações

## ⚙️ Configuração

### application.properties

Configure as seguintes propriedades conforme necessário:

```properties
# Servidor
server.port=8080

# Base de dados
spring.datasource.url=...
spring.datasource.username=...
spring.datasource.password=...

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=...
```

### application-test.properties

Configurações específicas para ambiente de testes.

## 📚 Referências

- [Spring Boot 3.1.0 Documentation](https://docs.spring.io/spring-boot/docs/3.1.0/reference/)
- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/)
- [OAuth2 Authorization Server](https://spring.io/projects/spring-authorization-server)

## 📖 Documentação Adicional

Consulte os documentos na pasta `Business rules/`:
- `Documento de requisitos DSCommerce.pdf` - Requisitos do projeto
- `02 Modelo de domínio e ORM (slides).pdf` - Modelo de dados
- `03 API REST, camadas, CRUD, exceções, validações (slides).pdf` - Arquitetura e padrões

## 👤 Autor

Desenvolvido como projeto educacional de e-commerce com Spring Boot.

## 📝 Licença

Este projeto é fornecido como está. Consulte o arquivo LICENSE para mais informações.

---

**Versão do Projeto**: 0.0.1-SNAPSHOT  
**Última Atualização**: Março 2026
