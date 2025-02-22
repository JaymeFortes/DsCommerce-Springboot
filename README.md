# DSCommerce - Sistema de E-commerce

Este repositório contém a implementação do **DSCommerce**, um sistema de e-commerce desenvolvido como parte do curso da [DevSuperior](https://devsuperior.com.br), sob a orientação do **Prof. Dr. Nelio Alves**.

## 📋 Sumário

1. [Visão Geral do Sistema](#-visão-geral-do-sistema)
2. [Funcionalidades Principais](#-funcionalidades-principais)
3. [Modelo Conceitual](#-modelo-conceitual)
4. [Casos de Uso](#-casos-de-uso)
5. [Tecnologias Utilizadas](#-tecnologias-utilizadas)
6. [Como Executar o Projeto](#-como-executar-o-projeto)
7. [Endpoints Principais](#-endpoints-principais)
8. [Licença](#-licença)

---

## 📊 Visão Geral do Sistema

O DSCommerce é um sistema de e-commerce que permite:

- Cadastro e gerenciamento de produtos, categorias e usuários.
- Navegação em um catálogo de produtos com opção de filtragem.
- Gerenciamento de carrinho de compras e finalização de pedidos.
- Controle de acesso por papéis (cliente e administrador).
- Registro e acompanhamento de pedidos e status.

### 📌 Requisitos

- O sistema deve conter um modelo de domínio abrangente, incluindo relacionamentos como "muitos-para-um" e "muitos-para-muitos".
- Implementar fluxos comuns em sistemas de e-commerce, como cadastro, login, e gerenciamento de carrinho.

## 🛠️ Funcionalidades Principais

| Funcionalidade          | Descrição                                              | Acesso         |
|-------------------------|-------------------------------------------------------|----------------|
| **Cadastro de Usuário** | Permite novos usuários se cadastrarem.                | Público       |
| **Login**               | Autenticação de usuários e geração de token JWT.      | Público       |
| **Consultar Catálogo**  | Listagem e filtragem de produtos.                     | Público       |
| **Gerenciar Carrinho**  | Adicionar, atualizar e remover itens do carrinho.     | Usuário logado|
| **Registrar Pedido**    | Finalizar o pedido com os itens do carrinho.          | Usuário logado|
| **Gerenciar Produtos**  | CRUD completo para produtos e categorias.             | Admin         |
| **Gerenciar Usuários**  | CRUD completo para usuários e controle de papéis.     | Admin         |
| **Relatório de Pedidos**| Consulta e geração de relatórios de pedidos.          | Admin         |

## 📐 Modelo Conceitual

O modelo conceitual do sistema é baseado em entidades principais:

- **User**: Representa os usuários, com os campos: `id`, `nome`, `email`, `telefone`, `data de nascimento`, `senha` e `roles`.
- **Product**: Representa os produtos, com os campos: `id`, `nome`, `preço`, `descrição` e `imagem`.
- **Category**: Representa as categorias dos produtos.
- **Order**: Representa um pedido, incluindo os itens do carrinho e seu status.
- **OrderItem**: Associa um produto a um pedido com a quantidade e preço no momento da compra.

### 📊 Status do Pedido

Um pedido pode ter os seguintes status:

- **Aguardando pagamento**
- **Pago**
- **Enviado**
- **Entregue**
- **Cancelado**

## 📖 Casos de Uso

### 1. Consultar Catálogo

- **Atores**: Usuário anônimo, Cliente, Admin
- **Descrição**: Permite listar e filtrar produtos por nome.
- **Fluxo principal**:
    1. O usuário acessa a listagem de produtos paginada (12 itens por página).
    2. O usuário pode informar um nome para filtrar os produtos.

### 2. Gerenciar Carrinho

- **Atores**: Usuário logado
- **Descrição**: Permite adicionar, atualizar e remover itens do carrinho.
- **Fluxo principal**:
    1. O usuário seleciona um produto para adicionar ao carrinho.
    2. O sistema atualiza os itens no carrinho e calcula o total.

### 3. Registrar Pedido

- **Atores**: Cliente (Usuário logado)
- **Descrição**: Finaliza a compra e gera um pedido com status "Aguardando pagamento".
- **Fluxo principal**:
    1. O usuário confirma os itens do carrinho.
    2. O sistema registra o pedido e limpa o carrinho.

### 4. Manter Produtos

- **Atores**: Admin
- **Descrição**: Permite criar, atualizar e deletar produtos.
- **Validações**:
    - Nome: 3 a 80 caracteres.
    - Preço: Deve ser positivo.
    - Descrição: No mínimo 10 caracteres.

## 🧰 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.4.3** (Security, JPA, OAuth2 Authorization Server)
- **H2 Database** (Ambiente de desenvolvimento)
- **JWT** para autenticação
- **Maven** para gerenciamento de dependências

## ▶️ Como Executar o Projeto

### Pré-requisitos

- Java 17+
- Maven 3.8+

### Passos para rodar a aplicação

1. Clone o repositório:

```bash
    https://github.com/JaymeFortes/DsCommerce-Springboot.git
```

2. Acesse o diretório do projeto:

```bash
    cd dscommerce
```

3. Configure as variáveis de ambiente no `application.properties`:

```properties
    security.client-id=seu-client-id
    security.client-secret=seu-client-secret
    security.jwt.duration=3600
```

4. Compile e execute o projeto:

```bash
    mvn spring-boot:run
```

5. Acesse a aplicação em:

```
    http://localhost:8080
```

## 📊 Endpoints Principais

### Autenticação

- `POST /oauth2/token`: Gera um token de acesso JWT.

### Produtos

- `GET /products`: Lista os produtos com suporte a paginação e filtro por nome.
- `POST /products`: Cria um novo produto (Apenas Admin).

### Carrinho de Compras

- `POST /cart`: Adiciona um item ao carrinho.
- `DELETE /cart/{id}`: Remove um item do carrinho.

### Pedidos

- `POST /orders`: Finaliza um pedido.
- `GET /orders`: Lista os pedidos do usuário autenticado.


