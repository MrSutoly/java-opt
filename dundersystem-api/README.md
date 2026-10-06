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
- **Java 17**
- **Spring Boot 3.3.5**
- **Spring Data JPA / Hibernate**
- **MySQL 8** (Banco de dados relacional)
- **Flyway** (Versionamento e migração automática de banco de dados)

---

## 🛠️ Estrutura do Projeto
A arquitetura do código segue rigorosamente os padrões de mercado do Spring Boot (Divisão por Camadas):

```text
src/main/java/br/edu/utfpr/dundersystem
├── controller/   # Endpoints REST (A porta de entrada da API)
├── service/      # Regras de negócio da aplicação
├── repository/   # Interfaces do Spring Data JPA (Comunicação com o banco)
├── model/        # Entidades JPA que representam as tabelas do banco e Enums
├── dto/          # Objetos de Transferência de Dados (Inputs e Outputs)
├── config/       # Configurações globais (Segurança, CORS, etc.)
└── exception/    # Handlers para tratamento global de erros e exceções

src/main/resources
├── application.properties    # Configurações de ambiente e banco de dados
└── db/migration/             # Scripts SQL rodados automaticamente pelo Flyway
```

---

## ⚙️ Como executar a aplicação localmente

### 1. Pré-requisitos
Certifique-se de ter os seguintes itens instalados na sua máquina:
- **Java 17** (JDK)
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
mvn spring-boot:run
```

A API iniciará e estará disponível em: **`http://localhost:8080`**.

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
- [ ] Construção dos CRUDs completos de Produto e Cliente.
- [ ] Lógica para o fluxo complexo de criação, alteração e aprovação de pedidos.
- [ ] Desenvolvimento do front-end consumindo esta API utilizando **Angular**.
