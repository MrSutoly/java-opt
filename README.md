# 🏢 DunderSystem API

Bem-vindo ao **DunderSystem**, o sistema definitivo de Gestão de Vendas e Estoque. Este é o projeto final desenvolvido para o curso de Tecnologia em Desenvolvimento de Sistemas da UTFPR Santa Helena.

Desenvolvido pela equipe **House Atreides**:
- 👨‍💻 **João Pedro Gomes** (RA 2651122)

---

## 🎯 O que o projeto faz?
O **DunderSystem API** é o back-end responsável por gerenciar as operações de uma distribuidora (inspirada na filial de Scranton da Dunder Mifflin). Ele permite:
- **Gestão de Funcionários e Clientes:** Vendedores gerenciam suas próprias carteiras de clientes.
- **Controle de Pedidos de Venda:** Fluxo completo de criação e aprovação de pedidos.
- **Gestão de Estoque:** Abate automático de produtos em estoque mediante a confirmação de uma venda.

**Fluxo de Status de um Pedido:**
`RASCUNHO` ➔ `AGUARDANDO APROVAÇÃO` ➔ `APROVADO / RECUSADO` ➔ `CONFIRMADO` (ou `CANCELADO`)

---

## 🚀 Tecnologias Utilizadas (Stack)
A aplicação foi construída com o que há de mais moderno e robusto no ecossistema Java:
- **Java 25 (LTS)**
- **Spring Boot 3.5.16**
- **Spring Data JPA / Hibernate**
- **MySQL 8** (Banco de dados relacional)
- **Flyway** (Versionamento e migração automática de banco de dados)

---

## 🛠️ Estrutura do Projeto
A arquitetura do código segue rigorosamente os padrões de mercado do Spring Boot (Divisão por Camadas):

```text
src/main/java/com/api
├── resource/     # Endpoints REST
├── service/      # Regras de negócio da aplicação
├── repository/   # Interfaces do Spring Data JPA (Comunicação com o banco)
├── model/        # Entidades JPA que representam as tabelas do banco e Enums
├── dto/          # Objetos de Transferência de Dados (Inputs e Outputs)
├── config/       # Configurações globais
├── event/        # Eventos de domínio
└── exceptionhandler/ # Tratamento global de erros e exceções

src/main/resources
├── application.properties    # Configurações de ambiente e banco de dados
└── db/migration/             # Scripts SQL rodados automaticamente pelo Flyway
```

---

## ⚙️ Como executar a aplicação localmente

### 1. Pré-requisitos
Certifique-se de ter os seguintes itens instalados na sua máquina:
- **Java 25** (JDK LTS)
- **Maven**
- **Docker** (e Docker Compose) para rodar o banco de dados facilmente.

### 2. Subindo o Banco de Dados
Na raiz do projeto, execute o comando abaixo no seu terminal para subir o contêiner do MySQL 8 em background:
```bash
docker compose up -d
```
> **Nota:** Não é necessário criar tabelas manualmente. O Flyway (configurado no Spring Boot) se encarregará de criar a estrutura do banco e popular os dados iniciais assim que a API for iniciada.

### 3. Executando a API
Você pode rodar a aplicação através de sua IDE favorita (Eclipse, IntelliJ, STS, VS Code) apenas executando a classe principal `DunderSystemApplication.java`.

Ou, se preferir rodar via terminal, utilize o Maven na raiz do projeto:
```bash
mvn clean test
mvn spring-boot:run
```

A API iniciará e estará disponível em: **`http://localhost:8080`**.

## Endpoints REST

Os endpoints de listagem usam paginação (`page`, `size`, `sort`) e retornam uma página Spring Data. Os filtros textuais aceitam `filtro`; pedidos aceitam `status`.

| Recurso | Endpoints |
|---|---|
| Funcionários | `GET/POST /api/funcionarios`, `GET/PUT/DELETE /api/funcionarios/{codigo}` |
| Clientes | `GET/POST /api/clientes`, `GET/PUT/DELETE /api/clientes/{codigo}` |
| Produtos | `GET/POST /api/produtos`, `GET/PUT/DELETE /api/produtos/{codigo}` |
| Pedidos | `GET/POST /api/pedidos`, `GET/PUT/DELETE /api/pedidos/{codigo}` |
| Status | `GET /api/status` |

Exemplos de listagem:
```http
GET http://localhost:8080/api/produtos?filtro=sulfite&page=0&size=10
GET http://localhost:8080/api/pedidos?status=RASCUNHO&page=0&size=10
```

### Exemplos de criação e atualização

Crie um cliente (vendedor precisa ser um funcionário ativo):
```http
POST /api/clientes
Content-Type: application/json

{
  "razaoSocial": "Empresa Exemplo",
  "documento": "12.345.678/0001-90",
  "email": "compras@exemplo.com",
  "telefone": "(570) 555-0110",
  "cidade": "Scranton",
  "estado": "PA",
  "ativo": true,
  "vendedor": { "id": 2 }
}
```

Atualize um cliente (o `PUT` usa o mesmo formato e preserva o ID do caminho):
```http
PUT /api/clientes/1
Content-Type: application/json

{
  "razaoSocial": "Empresa Exemplo Atualizada",
  "documento": "12.345.678/0001-90",
  "email": "novo-email@exemplo.com",
  "ativo": true,
  "vendedor": { "id": 2 }
}
```

Crie um funcionário (a senha é armazenada com hash BCrypt e não aparece nas respostas):
```http
POST /api/funcionarios
Content-Type: application/json

{
  "nome": "Pam Beesly",
  "email": "pam.beesly@dundermifflin.com",
  "senha": "troque-esta-senha",
  "perfil": "USER",
  "ativo": true
}
```

Atualize o preço e o estoque de um produto (use o mesmo formato no `PUT /api/produtos/{codigo}`):
```http
POST /api/produtos
Content-Type: application/json

{
  "nome": "Sulfite Branco 75g A4",
  "tipo": "Sulfite",
  "gramatura": 75,
  "formato": "A4",
  "precoUnitario": 26.90,
  "quantidadeEstoque": 500,
  "estoqueMinimo": 100,
  "ativo": true
}
```

Crie um pedido com os IDs das entidades relacionadas. Os preços dos itens e totais são calculados pela API; cliente, vendedor e produtos inativos são rejeitados:
```http
POST /api/pedidos
Content-Type: application/json

{
  "cliente": { "id": 1 },
  "vendedor": { "id": 2 },
  "status": "RASCUNHO",
  "descontoPercentual": 0,
  "observacao": "Entrega na próxima semana",
  "itens": [
    { "produto": { "id": 1 }, "quantidade": 10 }
  ]
}
```

Para atualizar, envie o mesmo corpo ao endpoint `PUT` correspondente com o código no caminho. Exclusões respondem com `204`; registros inexistentes com `404`; erros de validação com `400`. A senha de funcionário é aceita na escrita e nunca é devolvida nas respostas. Para atualizar funcionário sem trocar a senha, omita o campo `senha`.

---

## 🧪 Testando a Aplicação
Com a API rodando, você pode testar o endpoint de status da aplicação. Acesse pelo navegador ou utilizando uma ferramenta como Postman / Insomnia:

**Requisição:**
```http
GET http://localhost:8080/api/status
```
**Resposta Esperada:** Você verá um JSON retornando as estatísticas atuais do banco de dados (ex: quantidade de funcionários, clientes e produtos pré-cadastrados).

### 👥 Usuários Padrão para Testes
O banco já vem com dados populados (Seed) contendo funcionários e clientes para facilitar os testes das rotas de negócio.  
*(A senha padrão para todos os usuários é: `123456`)*

| Perfil | Personagem | E-mail |
|---|---|---|
| **ADMIN** *(Gerente)* | Michael Scott | `michael.scott@dundermifflin.com` |
| **USER** *(Vendedor)* | Dwight Schrute | `dwight.schrute@dundermifflin.com` |
| **USER** *(Vendedor)* | Jim Halpert | `jim.halpert@dundermifflin.com` |
| **ESTOQUE** *(Logística)*| Darryl Philbin | `darryl.philbin@dundermifflin.com` |

---

## 🚧 Próximos Passos (Roadmap)
O desenvolvimento continua! Aqui estão as próximas features a serem incorporadas:
- [ ] Implementação de **Spring Security + JWT** para autenticação e autorização das rotas.
- [ ] Lógica para o fluxo de aprovação de pedidos e baixa de estoque na confirmação.
- [ ] Desenvolvimento do front-end consumindo esta API utilizando **Angular**.
